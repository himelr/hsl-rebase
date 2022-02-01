import axios from 'axios';

const axiosClient = axios.create({
	baseURL: 'http://localhost:8080/api/auth',
	headers: {
		//Authorization: 'Test fYUCmbtGzHdLudI41CLgZdnnnpbwFpeAch3btpJjeus'
	}
})

class AuthService {

	login(email: string, password: string) {
		return axiosClient
			.post('login', {
				email,
				password
			})
			.then(response => {
				if (response.data.accessToken) {
					localStorage.setItem('authentication', JSON.stringify(response.data))
				}
				return response.data
			});
	}

	signup(email: string, password: string) {
		return axiosClient
			.post('sign-up', {
				email,
				password
			})
			.then(response => {
				return response.data
			});
	}

	logout() {
		localStorage.removeItem('authentication')
	}

	getCurrentUser() {
		const userStr = localStorage.getItem('authentication')
		return userStr ? JSON.parse(userStr) : null
	}

	isUserLoggedIn() {
		return !!localStorage.getItem('authentication')
	}
}

export default new AuthService()