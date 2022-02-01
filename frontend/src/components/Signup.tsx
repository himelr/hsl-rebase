import {useState} from "react";
import {useNavigate} from "react-router";
import {Button, Form, Grid, Header, Segment} from "semantic-ui-react";
import AuthService from "../services/AuthService";

const SignUp = () => {
	const navigate = useNavigate();
	const [email, setEmail] = useState('');
	const [password, setPassword] = useState('');
	const signup = () => AuthService.signup(email, password).then(_ => navigate('/login', {replace: true}));

	return (
		<Segment inverted
			textAlign="center"
			style={{padding: "1em 0em", minHeight: 900}}
			vertical>
			<Grid
				textAlign="center"
				style={{height: "30vh"}}
				verticalAlign="middle"
			>
				<Grid.Column style={{maxWidth: 450}}>
					<Header
						as="h2"
						color="blue"
						textAlign="center"
					>
						Sign up
					</Header>
					<Form size="large" onSubmit={signup}>
						<Segment stacked inverted>
							<Form.Input
								fluid
								icon="user"
								iconPosition="left"
								placeholder="E-mail"
								value={email}
								onChange={(_, data) => setEmail(data.value)}
							/>
							<Form.Input
								fluid
								icon="lock"
								iconPosition="left"
								placeholder="Password"
								type="password"
								value={password}
								onChange={(_, data) => setPassword(data.value)}
							/>
							<Button type="submit" color="blue" fluid size="large">
								Sign up
							</Button>
						</Segment>
					</Form>
				</Grid.Column>
			</Grid>
		</Segment>
	);
}

export default SignUp;