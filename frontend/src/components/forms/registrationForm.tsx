import React, {useState, ChangeEvent, FormEvent} from 'react';
import {useAuthContext} from "@/context/authContext";
import {useRouter} from 'next/router';
import {handleHomeRedirect} from "@/services/helpers";

type FormData = {
	email: string;
	username: string;
	password: string;
	passwordRepeat: string;
};

export const RegistrationForm = () => {
	const {register} = useAuthContext()
	const {push} = useRouter();

// Внутри компонента
	const router = useRouter();
	const [formData, setFormData] = useState<FormData>({
		email: '',
		username: '',
		password: '',
		passwordRepeat: '',
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
		if (formData.password !== formData.passwordRepeat) {
			alert("Passwords don't match");
			return;
		}

		const { passwordRepeat, ...payload } = formData;

		register(payload, () => {
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
				<label htmlFor="username" className="block text-sm font-bold mb-2">Username:</label>
				<input
					type="text"
					id="username"
					name="username"
					value={formData.username}
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
			<div className="mb-6">
				<label htmlFor="passwordRepeat" className="block text-sm font-bold mb-2">Repeat Password:</label>
				<input
					type="password"
					id="passwordRepeat"
					name="passwordRepeat"
					value={formData.passwordRepeat}
					onChange={handleChange}
					required
					className="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 mb-3 leading-tight focus:outline-none focus:shadow-outline"
				/>
			</div>
			<button type="submit"
			        className="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded focus:outline-none focus:shadow-outline">
				Register
			</button>
		</form>
	);
};

