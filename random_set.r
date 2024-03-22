# Generate a random dataset
set.seed(123)  # Setting seed for reproducibility
num_rows <- 100
data <- data.frame(
  ID = 1:num_rows,
  Age = sample(18:65, num_rows, replace = TRUE),
  Height = round(rnorm(num_rows, mean = 170, sd = 10), 2),
  Weight = round(rnorm(num_rows, mean = 70, sd = 12), 2)
)

# Display the first few rows of the dataset
print(head(data))

# Summary statistics
summary(data)

# Plotting
par(mfrow=c(2, 2))  # Divide the plotting area into a 2x2 grid
hist(data$Age, main="Histogram of Age", xlab="Age", col="lightblue")
hist(data$Height, main="Histogram of Height", xlab="Height", col="lightgreen")
hist(data$Weight, main="Histogram of Weight", xlab="Weight", col="lightpink")
plot(data$Height, data$Weight, main="Scatterplot of Height vs Weight", xlab="Height", ylab="Weight", pch=16)
