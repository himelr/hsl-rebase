import axios from 'axios';
import CalculatedItem from '../models/CalculatedItem';
import RouteQueryItem from '../models/RouteQueryItem';

const axiosClient = axios.create({
	baseURL: 'http://localhost:8080/api/public/route',
	headers: {
		//Authorization: 'Test fYUCmbtGzHdLudI41CLgZdnnnpbwFpeAch3btpJjeus'
	}
})

class RouteService {

	searchAddress(searchTerm: string) {
		return axiosClient
			.get('/address-search', {
				params: {
					searchTerm: searchTerm
				}
			})
	}

	getCalculatedRoute(routeQueryItem: RouteQueryItem) {
		return axiosClient
			.post<CalculatedItem>('/calculated', routeQueryItem)
			.then(response => response.data)
	}
}

export default new RouteService();