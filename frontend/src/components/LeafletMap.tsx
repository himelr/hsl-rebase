import L, {LatLngExpression} from "leaflet";
import React from "react";
import {MapContainer, Marker, TileLayer} from "react-leaflet";
import icon from 'leaflet/dist/images/marker-icon.png';
import iconShadow from 'leaflet/dist/images/marker-shadow.png';

type LeafletProps = {
	markers: LatLngExpression[]
}

const LeafletMap = (props: LeafletProps) => {
	const DefaultIcon = L.icon({
		iconUrl: icon,
		shadowUrl: iconShadow
	});
	L.Marker.prototype.options.icon = DefaultIcon;
	const position = [60.192059, 24.945831] as LatLngExpression;
	const markers = () => props.markers.map((latLng, id) => <Marker key={id} position={latLng}> </Marker>);
	return (
		<MapContainer center={position} zoom={11} scrollWheelZoom={false}>
			<TileLayer
				url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
				attribution='&copy; <a href="http://osm.org/copyright">OpenStreetMap</a> contributors'
			/>
			{markers()}
		</MapContainer>
	)
}

export default LeafletMap;