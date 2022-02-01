import {useState} from "react";
import {
	Button,
	Form,
	Grid,
	Header,
	Message,
	Segment,
} from "semantic-ui-react";
import AuthService from "../services/AuthService";

const Login = () => {

	const [email, setEmail] = useState('');
	const [password, setPassword] = useState('');

	const doLogin = () => AuthService.login(email, password).then(_ => window.location.href = '/');

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
						Log in to App
					</Header>
					<Form size="large" onSubmit={doLogin}>
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
								Login
							</Button>
						</Segment>
					</Form>
					<Message>
						<a href='/signup'>Sign Up</a>
					</Message>
				</Grid.Column>
			</Grid>
		</Segment>
	);
}
export default Login;
