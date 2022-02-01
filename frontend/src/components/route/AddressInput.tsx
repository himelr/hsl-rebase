import {Dispatch, useEffect, useState} from "react";
import {Dropdown, DropdownItemProps, Form} from "semantic-ui-react";
import LatLonItem from "../../models/LatLonItem";
import RouteService from "../../services/RouteService";
import StringUtil from "../../util/StringUtil";

type AddressInputProps = {
	label: string;
	onSelectChange: Dispatch<React.SetStateAction<LatLonItem>>
}

const AddressInput = (props: AddressInputProps) => {
	const [search, setSearch] = useState('');
	const [debouncedSearch, setDebouncedSearch] = useState('');
	const [dropdownOptions, setDropdownOptions] = useState<DropdownItemProps[]>();

	useEffect(() => {
		if (!!debouncedSearch) {
			RouteService.searchAddress(debouncedSearch)
				.then(res =>
					setDropdownOptions(res.data.features.map((feature: any) => {
						return {text: `${feature.properties.name}, ${feature.properties.postalcode}`, value: feature.geometry.coordinates}
					})));
		}
	}, [debouncedSearch]);

	useEffect(() => {
		const timeoutId: NodeJS.Timeout = setTimeout(() => {
			setDebouncedSearch(search);
		}, 800);
		return () => clearTimeout(timeoutId);
	}, [search]);

	return (
		<Form.Field>
			<div className="field">
				<label style={{color: "white"}}>{props.label}</label>
				<Dropdown
					placeholder={props.label}
					fluid
					search
					selection
					options={dropdownOptions}
					on
					onChange={(_, {value}) => {
						const arr = StringUtil.convertStringToArray(value);
						props.onSelectChange({latitude: arr[1], longitude: arr[0]})
					}
					}
					onSearchChange={(_, data) => setSearch(data?.searchQuery ?? '')}
				/>
			</div>
		</Form.Field>
	)
}

export default AddressInput;