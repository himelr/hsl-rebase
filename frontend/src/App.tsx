import {BrowserRouter as Router, Route, Routes} from "react-router-dom"
import './App.css';
import Home from "./components/Home";
import Login from "./components/Login";
import Nav from './components/Nav';
import RoutePlan from "./components/route/RoutePlan";
import SignUp from "./components/Signup";
import 'leaflet/dist/leaflet.css';

const App = () => {

	const styleLink = document.createElement("link");
	styleLink.rel = "stylesheet";
	styleLink.href = "https://cdn.jsdelivr.net/npm/semantic-ui/dist/semantic.min.css";
	document.head.appendChild(styleLink);

	return (
		<div className="App">
			<Router >
				<Nav />
				<Routes>
					<Route path="/" element={<Home />} />
					<Route path="/route" element={<RoutePlan />} />
					<Route path="/login" element={<Login />} />
					<Route path="/signup" element={<SignUp />} />
				</Routes>
			</Router>
		</div>
	);
}

export default App;
