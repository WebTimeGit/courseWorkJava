import React, { ChangeEvent, FormEvent, useState } from 'react';
import { useAuthContext } from "@/context/authContext";
import { loggedAxios } from "@/services/axios";
import { API } from "@/services/api/api";
import {mutate} from "swr";

type FormData = {
	email: string | undefined;
	username: string | undefined;
};

export const UpdateUserProfileForm = () => {
	const { userData } = useAuthContext()

	const [formData, setFormData] = useState<FormData>({
		email: userData?.email,
		username: userData?.username,
	});

	const [isLoading, setIsLoading] = useState<boolean>(false);
	const [error, setError] = useState<string | null>(null);

	const handleChange = (e: ChangeEvent<HTMLInputElement>) => {
		const { name, value } = e.target;
		setFormData(prevState => ({
			...prevState,
			[name]: value
		}));
	};

	const onUpdateProfile = async (id: number, formData: FormData) => {
		setIsLoading(true);
		setError(null);
		try {
			const data = {
				email: formData.email,
				username: formData.username
			};

			const response = await loggedAxios.patch(API.USERS?.profile(id), data);
			mutate(API?.USERS?.userInfo)
			alert('Profile updated successfully!');
		} catch (error: any) {
			console.error("Error:", error);
			setError('Failed to update profile. ' + (error.response?.data?.message || error.message));
		} finally {
			setIsLoading(false);
		}
	}


	const handleSubmit = (e: FormEvent) => {
		e.preventDefault();
		if (!userData?.id) return;

		onUpdateProfile(userData.id, formData);
	};

	return (
		<form onSubmit={handleSubmit} className="max-w-md mx-auto my-10">
			<div className="mb-4">
				<label htmlFor="email" className="block text-sm font-bold mb-2">Email:</label>
				<input
					type="email"
					id="email"
					name="email"
					value={formData.email || ''}
					onChange={handleChange}
					className="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
				/>
			</div>
			<div className="mb-4">
				<label htmlFor="username" className="block text-sm font-bold mb-2">Username:</label>
				<input
					type="text"
					id="username"
					name="username"
					value={formData.username || ''}
					onChange={handleChange}
					className="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 mb-3 leading-tight focus:outline-none focus:shadow-outline"
				/>
			</div>
			{error && <p className="text-red-500 text-xs italic">{error}</p>}
			<button type="submit"
			        className="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded focus:outline-none focus:shadow-outline"
			        disabled={isLoading}>
				{isLoading ? 'Updating...' : 'Update'}
			</button>
		</form>
	);
};
