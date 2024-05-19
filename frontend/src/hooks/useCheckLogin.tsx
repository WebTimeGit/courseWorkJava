import useSWR from "swr";
import {fetcher} from "@/services/axios";
import { API } from "@/services/api/api";
import { TUserInfo } from "@/context/authContext";

export const useCheckLogin = () => {
	const token = typeof window !== "undefined" ? localStorage.getItem("token") : null;

	const { data, isLoading, error } = useSWR<TUserInfo>(
		token ? API?.USERS?.userInfo : null,
		fetcher,
		{ dedupingInterval: 5000 }
	);


	if (!token && !isLoading) {
		return { statusError: 'no-token', userInfo: null };
	}

	if (error && !isLoading) {
		return { statusError: 'error', userInfo: null };
	}

	const userInfo = data ? data : null;
	return { statusError: userInfo ? 'success' : 'loading', userInfo };

}
