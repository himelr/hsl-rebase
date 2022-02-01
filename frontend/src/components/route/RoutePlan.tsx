import {LatLngExpression} from "leaflet";
import {useEffect, useState} from "react";
import {Button, Form, Grid, Segment} from "semantic-ui-react";
import CalculatedItem from "../../models/CalculatedItem";
import LatLonItem from "../../models/LatLonItem";
import RouteService from "../../services/RouteService";
import LeafletMap from "../LeafletMap";
import AddressInput from "./AddressInput";
import RouteDetails from "./RouteDetails";

const RoutePlan = () => {

	const emptyLatLonItem: LatLonItem = {latitude: 0, longitude: 0}
	const [fromItem, setFromItem] = useState<LatLonItem>(emptyLatLonItem);
	const [toItem, setToItem] = useState<LatLonItem>(emptyLatLonItem);
	const [markers, setMarkers] = useState<LatLngExpression[]>([]);
	const [calculatedItem, setCalculatedItem] = useState<CalculatedItem>();

	const getRoute = () => {
		RouteService.getCalculatedRoute({from: fromItem, to: toItem}).then(data => setCalculatedItem(data))
	}

	useEffect(() => {
		setMarkers([
			[fromItem.latitude, fromItem.longitude] as LatLngExpression,
			[toItem.latitude, toItem.longitude] as LatLngExpression
		])
	}, [fromItem, toItem]);

	return (
		<Segment
			inverted
			textAlign="center"
			style={{minHeight: 900, padding: "1em 0em"}}
			vertical>
			<Grid>
				<Grid.Row centered >
					<LeafletMap markers={markers}></LeafletMap>
				</Grid.Row>
				<Grid.Row centered>
					<Grid.Column width={6}>
						<Form onSubmit={getRoute}>
							<AddressInput key='from' onSelectChange={setFromItem} label="From"></AddressInput>
							<AddressInput key='to' onSelectChange={setToItem} label="To"></AddressInput>
							<Button type='submit'>Search</Button>
						</Form>
						<RouteDetails calculatedItem={calculatedItem}></RouteDetails>
					</Grid.Column>
				</Grid.Row>
			</Grid>
		</Segment>
	)
}

export default RoutePlan;