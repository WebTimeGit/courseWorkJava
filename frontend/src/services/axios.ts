import axios from "axios";
import {appConfig} from "@/config";

export const api = axios.create({
	baseURL: appConfig.apiUrl,
	headers: {
		accept: 'application/json',
		'Content-Type': 'application/json',
	}
});

export  const loggedAxios = axios.create({
	baseURL: appConfig.apiUrl,
	headers: {
		accept: 'application/json',
		'Content-Type': 'application/json',
	}
});

loggedAxios.interceptors.request.use((config) => {
	const token = localStorage.getItem("token");
	if (token) {
		config.headers.Authorization = `Bearer ${token}`; // Использование префикса Bearer
	}
	return config;
});


export const fetcher = (url:string) => loggedAxios.get(url).then(res => res.data)