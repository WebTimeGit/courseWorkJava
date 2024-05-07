import React, {ChangeEvent, FormEvent, useState} from 'react';
import {useAuthContext} from "@/context/authContext";
import {useRouter} from "next/router";
import {handleHomeRedirect} from "@/services/helpers";

type FormData = {
	email: string;
	password: string;
};

export const LoginForm = () => {
	const {login} = useAuthContext()
	const {push} = useRouter();

// Внутри компонента
	const router = useRouter();
	const [formData, setFormData] = useState<FormData>({
		email: '',
		password: '',
	});

	const handleChange = (e: ChangeEvent<HTMLInputElement>) => {
		const {name, value} = e.target;
		setFormData(prevState => ({
			...prevState,
			[name]: value
		}));
	};

	const handleSubmit = (e: FormEvent) => {
		e.preventDefault();

		login(formData, () => {
			handleHomeRedirect(push);
		}).then()
	};

	return (
		<form onSubmit={handleSubmit} className="max-w-md mx-auto my-10">
			<div className="mb-4">
				<label htmlFor="email" className="block text-sm font-bold mb-2">Email:</label>
				<input
					type="email"
					id="email"
					name="email"
					value={formData.email}
					onChange={handleChange}
					required
					className="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
				/>
			</div>
			<div className="mb-4">
				<label htmlFor="password" className="block text-sm font-bold mb-2">Password:</label>
				<input
					type="password"
					id="password"
					name="password"
					value={formData.password}
					onChange={handleChange}
					required
					className="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 mb-3 leading-tight focus:outline-none focus:shadow-outline"
				/>
			</div>
			<button type="submit"
			        className="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded focus:outline-none focus:shadow-outline">
				login
			</button>
		</form>
	);
};
