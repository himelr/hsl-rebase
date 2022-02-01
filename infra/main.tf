terraform {
	backend "s3" {
		bucket         = "hsl-rebase-state"
		key            = "global/s3/terraform.tfstate"
		region         = "eu-north-1"
		dynamodb_table = "hsl-rebase-sl"
	}

 required_providers {
   aws = {
     source = "hashicorp/aws"
     version = "~> 3.8.0"
   }
 }
}

provider "aws" {
 region = "eu-north-1"
}

resource "aws_s3_bucket" "hsl_rebase_state" {
  bucket   = "hsl-rebase-state"
  acl      = "private"
  versioning {
    enabled = true
  }
  lifecycle {
    prevent_destroy = false
  }

  #Enable server-side encryption by default
  server_side_encryption_configuration {
    rule {
      apply_server_side_encryption_by_default {
        sse_algorithm = "AES256"
      }
    }
  }
}

# Deny public acls and policies for Terraform state bucket
resource "aws_s3_bucket_public_access_block" "hsl_rebase_ab" {
  bucket = aws_s3_bucket.hsl_rebase_state.id

  block_public_acls       = true
  ignore_public_acls      = true
  block_public_policy     = true
  restrict_public_buckets = true
}


# Create a dynamodb table for locking the state file
resource "aws_dynamodb_table" "hsl_rebase_sl" {
  name           = "hsl-rebase-sl"
  hash_key       = "LockID"
  read_capacity  = 20
  write_capacity = 20

  attribute {
    name = "LockID"
    type = "S"
  }

  tags = {
    Name = "DynamoDB Terraform State Lock Table"
  }
}

resource "aws_ecr_repository" "hsl_rebase_webapp" {
  name = "hsl-rebase-webapp"
}

resource "aws_ecs_cluster" "hsl_rebase_cluster" {
  name = "hsl-rebase-cluster"
}

resource "aws_iam_role" "ecsTaskExecutionRole" {
  name               = "ecsTaskExecutionRole"
  assume_role_policy = "${data.aws_iam_policy_document.assume_role_policy.json}"
}

data "aws_iam_policy_document" "assume_role_policy" {
  statement {
    actions = ["sts:AssumeRole"]

    principals {
      type        = "Service"
      identifiers = ["ecs-tasks.amazonaws.com"]
    }
  }
}

resource "aws_iam_role_policy_attachment" "ecsTaskExecutionRole_policy" {
  role       = "${aws_iam_role.ecsTaskExecutionRole.name}"
  policy_arn = "arn:aws:iam::aws:policy/service-role/AmazonECSTaskExecutionRolePolicy"
}

resource "aws_ecs_task_definition" "webapp_task_definition" {
  family                   = "hsl-rebase-webapp"
  container_definitions    = <<DEFINITION
  [
    {
      "name": "hsl-rebase-webapp",
      "image": "${aws_ecr_repository.hsl_rebase_webapp.repository_url}:latest",
      "essential": true,
      "portMappings": [
        {
          "containerPort": 8080,
          "hostPort": 8080
        }
      ],
      "memory": 512,
      "cpu": 256,
	  "logConfiguration": {
			"logDriver": "awslogs",
			"options": {
				"awslogs-region" : "eu-north-1",
				"awslogs-group" : "${aws_cloudwatch_log_group.hsl_rebase_webapp_logs.name}",
				"awslogs-stream-prefix" : "hslfd"
			}
		}
    }
  ]
  DEFINITION

  requires_compatibilities = ["FARGATE"] # Stating that we are using ECS Fargate
  network_mode             = "awsvpc"    # Using awsvpc as our network mode as this is required for Fargate
  memory                   = 1024        # Specifying the memory our container requires
  cpu                      = 512         # Specifying the CPU our container requires
  execution_role_arn       = "${aws_iam_role.ecsTaskExecutionRole.arn}"
}

resource "aws_ecs_service" "webapp_service" {
  name            = "webapp-service"
  cluster         = "${aws_ecs_cluster.hsl_rebase_cluster.id}"
  task_definition = "${aws_ecs_task_definition.webapp_task_definition.arn}"
  launch_type     = "FARGATE"
  desired_count   = 1

  load_balancer {
    target_group_arn = "${aws_lb_target_group.target_group.arn}"
    container_name   = "${aws_ecs_task_definition.webapp_task_definition.family}"
    container_port   = 8080 # Specifying the container port
  }

  network_configuration {
    subnets = ["${aws_default_subnet.default_subnet_a.id}", "${aws_default_subnet.default_subnet_b.id}", "${aws_default_subnet.default_subnet_c.id}"]
    assign_public_ip = true
	security_groups  = ["${aws_security_group.webapp_security_group.id}"]
  }
}

resource "aws_default_vpc" "default_vpc" {
}

resource "aws_default_subnet" "default_subnet_a" {
  availability_zone = "eu-north-1a"
}

resource "aws_default_subnet" "default_subnet_b" {
  availability_zone = "eu-north-1b"
}

resource "aws_default_subnet" "default_subnet_c" {
  availability_zone = "eu-north-1c"
}

resource "aws_cloudwatch_log_group" "hsl_rebase_webapp_logs" {
  name              = "/aws/ecs/hsl-rebase-webapp-logs"
  retention_in_days = 7
}

resource "aws_alb" "application_load_balancer" {
  name               = "hsl-rebase-lb" # Naming our load balancer
  load_balancer_type = "application"
  subnets = [ # Referencing the default subnets
    "${aws_default_subnet.default_subnet_a.id}",
    "${aws_default_subnet.default_subnet_b.id}",
    "${aws_default_subnet.default_subnet_c.id}"
  ]
  # Referencing the security group
  security_groups = ["${aws_security_group.load_balancer_security_group.id}"]
}

resource "aws_lb_target_group" "target_group" {
  name        = "target-group"
  port        = 80
  protocol    = "HTTP"
  target_type = "ip"
  vpc_id      = "${aws_default_vpc.default_vpc.id}"
  health_check {
    matcher = "200,301,302"
    path = "/"
  }
}

resource "aws_lb_listener" "listener" {
  load_balancer_arn = "${aws_alb.application_load_balancer.arn}"
  port              = "80"
  protocol          = "HTTP"
  default_action {
    type             = "forward"
    target_group_arn = "${aws_lb_target_group.target_group.arn}"
  }
}

# Creating a security group for the load balancer:
resource "aws_security_group" "load_balancer_security_group" {
  name = "lb-security-group"
  vpc_id = "${aws_default_vpc.default_vpc.id}"
  ingress {
    from_port   = 80 # Allowing traffic in from port 80
    to_port     = 80
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }
}

resource "aws_security_group" "webapp_security_group" {
  name = "webapp-security-group"
  vpc_id = "${aws_default_vpc.default_vpc.id}"
  ingress {
    from_port = 0
    to_port   = 0
    protocol  = "-1"
    # Only allowing traffic in from the load balancer security group
    security_groups = ["${aws_security_group.load_balancer_security_group.id}"]
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }
}

resource "aws_security_group" "database_security_group" {
  name = "database-security-group"
  vpc_id = "${aws_default_vpc.default_vpc.id}"
  ingress {
    from_port = 0
    to_port   = 0
    protocol  = "-1"
    security_groups = ["${aws_security_group.webapp_security_group.id}"]
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }
}

resource "aws_db_subnet_group" "default_subnet_group" {
  name        = "default-subnet-group"
  description = "Private subnets for RDS instance"
  subnet_ids  = [
    "${aws_default_subnet.default_subnet_a.id}",
    "${aws_default_subnet.default_subnet_b.id}",
    "${aws_default_subnet.default_subnet_c.id}"
  ]
}

variable "db_password" {
   description = "RDS root user password"
   type        = string
   default     = "testtest"
   sensitive   = true
   }

resource "aws_db_instance" "hsl_rebase_database" {
    identifier             = "hsl-rebase-database"
    instance_class         = "db.t3.micro"
    allocated_storage      = 20
    engine                 = "postgres"
    engine_version         = "13.4"
    username               = "demo"
    password               = var.db_password
    db_subnet_group_name   = aws_db_subnet_group.default_subnet_group.name
    vpc_security_group_ids = [aws_security_group.database_security_group.id]
    parameter_group_name   = "default.postgres13"
    skip_final_snapshot    = true
    publicly_accessible    = true
    port                   = 5432
    name                   = "hsl_rebase"
    }