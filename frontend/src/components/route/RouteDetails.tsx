import React from "react";
import {Rail, Segment} from "semantic-ui-react";
import CalculatedItem from "../../models/CalculatedItem";

type RouteDetailsProps = {
	calculatedItem?: CalculatedItem
}

const RouteDetails = (props: RouteDetailsProps) => {
	const rail = () => {
		if (props.calculatedItem) {
			return <React.Fragment>
				<Rail position='left'>
					<Segment>Distance: {props.calculatedItem.distance} km</Segment>
					<Segment>Co2 emissions: {props.calculatedItem.co2Emissions}</Segment>
					<Segment>Total calories burned: {props.calculatedItem.totalCaloriesBurned}</Segment>
				</Rail>
			</React.Fragment>
		}
	};
	return (
		<React.Fragment>
			{rail()}
		</React.Fragment>
	)
}

export default RouteDetails;