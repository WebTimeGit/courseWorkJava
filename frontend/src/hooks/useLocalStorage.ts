import { useState } from "react"

const useLocalStorage = (key:string, initialValue:string) => {
	const [storedValue, setStoredValue] = useState<string>(() => {
		try {
			const item = window.localStorage.getItem(key)
			return item  || initialValue
		} catch (e) {
			return initialValue
		}
	})

	const setValue = (value:string) => {
		try {
			window.localStorage.setItem(key,  value )
			setStoredValue(value)
		} catch (error) {
			console.error(error)
		}
	}

	return [storedValue, setValue] as const
}

export default useLocalStorage
