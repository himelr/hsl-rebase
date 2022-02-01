class StringUtil {
	convertStringToArray = (object: any) => {
		return (typeof object === 'string') ? Array(object) : object
	}
}

export default new StringUtil();