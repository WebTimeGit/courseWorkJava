import React, {useCallback, useContext, useEffect, useState} from "react";
import useLocalStorage from "@/hooks/useLocalStorage";
import {isAxiosError} from 'axios';
import {API} from "@/services/api/api";
import {api} from "@/services/axios";
import {useCheckLogin} from "@/hooks/useCheckLogin";




type AuthContextProviderProps = {
	children: React.ReactNode;
};

export type loginDTO = {
	email: string;
	password: string;
};

export type registerDTO = {
	email: string;
	username: string;
	password: string;
};

export type TUserInfo = {
	id: number
	date: string
	email: string
	username: string
}

export type TUserData = {
	message: string
	status: string
	userInfo: TUserInfo
}

const initialValues = {
	token: '',
	setToken: (token: string) => {},
	isAuth: false,
	loading: false,
	setLoading: (loading: boolean) => {},
	error: {email: Array(), password: Array()},
	login: async (data: loginDTO, onSuccess?: () => void) => {},
	register: async (data: registerDTO, onSuccess?: () => void) => {},
	logout: () => {},
	userData: null as TUserInfo | null
}

export type AuthContextProps = typeof initialValues;
export const AuthContext = React.createContext<AuthContextProps>(initialValues);

export const useAuthContext = () => {
	const context = useContext(AuthContext)
	if (!context) throw new Error("AuthContextProvider must be used within a AuthContextProvider")

	return context
}

export const AuthContextProvider: React.FC<AuthContextProviderProps> = ({children}) => {
	const [error, setError] = useState<{ email: string[]; password: string[]; }>({
		email: [],
		password: []
	})
	const [token, setToken] = useLocalStorage("token", "");
	const [isAuth, setIsAuth] = useState<boolean>(false);
	const [loading, setLoading] = useState<boolean>(true);


	const { statusError, userInfo } = useCheckLogin();
	const [userData, setUserData] = useState<TUserInfo | null>(null);

	useEffect(() => {

		if (statusError === 'success' && userInfo) {
			setIsAuth(true)
			setUserData(userInfo)
		} else if (statusError === 'error') {
			setIsAuth(false)
			logout();
		}
	}, [statusError, userInfo, setIsAuth]);


	const login = useCallback(
		async (data: loginDTO, onSuccess?: () => void) => {
			try {
				if (loading) return;
				setLoading(true);
				const response = await api.post(API.signin, data);
				setToken(response?.data?.token);
				setIsAuth(true);

				// toast.success('Successful login');
				onSuccess && await onSuccess();
			} catch (error_) {
				if (isAxiosError(error_)) {
					// toast.error(<ToastMessage title='Error' message={error_.response?.data?.detail}/>);

					if (error_) {
						setError({
							email: error_.response?.data?.detail === 'User not found' ? ["User not found"] : [],
							password: error_.response?.data?.detail === 'Wrong password' ? ["Wrong password"] : [],
						});
					}
				}

				console.log(error_);
			} finally {
				setLoading(false);
			}
		},
		[loading],
	);


	const logout = useCallback(() => {
		setToken("");
		setIsAuth(false);

		localStorage.removeItem('token');
	}, []);


	const register = useCallback(
		async (data: registerDTO, onSuccess?: () => void) => {
			try {
				if (loading) return
				setLoading(true)

				const response = await api.post(API.signup, data)

				localStorage.setItem('token', response?.data?.token)
				setToken(response?.data?.token)
				setIsAuth(true)
				onSuccess && await onSuccess()
			} catch (error_) {
				// if (isAxiosError(error_)) {
				// 	const fieldErrors = error_.response?.data?.fields;
				//
				// 	if (fieldErrors) {
				// 		setError({
				// 			email: fieldErrors.email || [],
				// 			password: fieldErrors.password || []
				// 		})
				// 	}
				// }
				console.log(error_)
			} finally {
				setLoading(false)
			}
		},
		[loading],
	)


	const values = React.useMemo(
		() => ({
			token,
			setToken,
			isAuth,
			loading,
			setLoading,
			error,
			login,
			register,
			userData,
			logout
		}),
		[token, setToken, isAuth, login, register, userData, logout, setLoading]
	);

	return <AuthContext.Provider value={values}>{children}</AuthContext.Provider>;
}
