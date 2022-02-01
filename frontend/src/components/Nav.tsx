import {useState} from "react";
import {Button, Container, Menu, Segment} from "semantic-ui-react";
import {Link} from "react-router-dom";
import AuthService from "../services/AuthService";

const Nav = () => {
	const [active, setActive] = useState("home");
	const handleItemClick = (_: React.MouseEvent, {name}: any) => setActive(name);
	const logout = () => AuthService.logout();
	const getProfileOrLogin = () => {
		if (AuthService.isUserLoggedIn()) {
			return <Menu.Item position="right"
				onClick={handleItemClick}>
				<Button
					onClick={logout}
					inverted>
					Logout
				</Button>
			</Menu.Item>
		} else {
			return <Menu.Item position="right"
				active={active === "login"}
				onClick={handleItemClick}>
				<Button
					as={Link}
					inverted
					to="login"
					name="login">
					Log in
				</Button>
			</Menu.Item>
		}
	}
	return (
		<Segment
			inverted
			textAlign="center"
			style={{padding: "1em 0em"}}
			vertical
		>
			<Menu inverted pointing secondary size="large">
				<Container>
					<Menu.Item
						as={Link}
						name="home"
						to="/"
						active={active === "home"}
						onClick={handleItemClick}>
						Home
					</Menu.Item>
					<Menu.Item as={Link}
						name="route"
						to="route"
						active={active === "route"}
						onClick={handleItemClick}>
						Calculate route
					</Menu.Item>
					{getProfileOrLogin()}
				</Container>
			</Menu>
		</Segment>
	);
};

export default Nav;
