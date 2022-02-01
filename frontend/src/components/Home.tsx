import {Container, Header, Segment} from "semantic-ui-react";

const Home = () => {
	return (
		<Segment
			inverted
			textAlign="center"
			style={{minHeight: 900, padding: "1em 0em"}}
			vertical
		>
			<Container text>
				<Header
					as="h1"
					content="Project"
					inverted
					style={{
						fontSize: "4em",
						fontWeight: "normal",
						marginBottom: 0,
						marginTop: "3em",
					}}
				/>
				<Header
					as="h2"
					content="HSL Rebase"
					inverted
					style={{
						fontSize: "1.7em",
						fontWeight: "normal",
						marginTop: "1.5em",
					}}
				/>
			</Container>
		</Segment>
	);
};

export default Home;
