import React, { useState, ChangeEvent, FormEvent } from 'react';
import { loggedAxios } from '@/services/axios';
import { API } from '@/services/api/api';
import { mutate } from 'swr';
import {ParkingSpace} from "@/services/api/parking_spaces";

export const ManageParkingSpacesForm: React.FC = () => {
	const [status, setStatus] = useState<ParkingSpace['status']>('FREE');
	const [parkingSpaceId, setParkingSpaceId] = useState<number | string>('');
	const [isLoading, setIsLoading] = useState<boolean>(false);
	const [error, setError] = useState<string | null>(null);
	const [success, setSuccess] = useState<string | null>(null);

	const handleCreate = async (e: FormEvent) => {
		e.preventDefault();
		setIsLoading(true);
		setError(null);
		setSuccess(null);

		try {
			const data: Partial<ParkingSpace> = {
				status,
			};

			const response = await loggedAxios.post<ParkingSpace>(API.PARKING_SPACES.create, data);
			console.log('Response:', response);
			mutate(API.PARKING_SPACES.getAll); // Оновлення списку паркомісць
			setSuccess('Parking space created successfully!');
		} catch (error: any) {
			console.error('Error:', error);
			setError('Failed to create parking space. ' + (error.response?.data?.message || error.message));
		} finally {
			setIsLoading(false);
		}
	};

	const handleDelete = async (e: FormEvent) => {
		e.preventDefault();
		setIsLoading(true);
		setError(null);
		setSuccess(null);

		try {
			await loggedAxios.delete(`${API.PARKING_SPACES.delete}/${parkingSpaceId}`);
			console.log('Deleted parking space with ID:', parkingSpaceId);
			mutate(API.PARKING_SPACES.getAll); // Оновлення списку паркомісць
			setSuccess('Parking space deleted successfully!');
		} catch (error: any) {
			console.error('Error:', error);
			setError('Failed to delete parking space. ' + (error.response?.data?.message || error.message));
		} finally {
			setIsLoading(false);
		}
	};

	const handleUpdate = async (e: FormEvent) => {
		e.preventDefault();
		setIsLoading(true);
		setError(null);
		setSuccess(null);

		try {
			const data: Partial<ParkingSpace> = {
				status,
			};

			await loggedAxios.put<ParkingSpace>(`${API.PARKING_SPACES.update}/${parkingSpaceId}`, data);
			console.log('Updated parking space with ID:', parkingSpaceId);
			mutate(API.PARKING_SPACES.getAll); // Оновлення списку паркомісць
			setSuccess('Parking space updated successfully!');
		} catch (error: any) {
			console.error('Error:', error);
			setError('Failed to update parking space. ' + (error.response?.data?.message || error.message));
		} finally {
			setIsLoading(false);
		}
	};

	const handleChangeStatus = (e: ChangeEvent<HTMLSelectElement>) => {
		setStatus(e.target.value as ParkingSpace['status']);
	};

	const handleChangeId = (e: ChangeEvent<HTMLInputElement>) => {
		const value = e.target.value;
		setParkingSpaceId(value === '' ? '' : Number(value));
	};

	return (
		<div className="max-w-md mx-auto my-10">
			<form onSubmit={handleCreate} className="mb-6">
				<div className="mb-4">
					<label htmlFor="status" className="block text-sm font-bold mb-2">Status:</label>
					<select
						id="status"
						name="status"
						value={status}
						onChange={handleChangeStatus}
						className="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
					>
						<option value="FREE">Free</option>
						<option value="OCCUPIED">Occupied</option>
					</select>
				</div>
				<button type="submit"
				        className="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded focus:outline-none focus:shadow-outline"
				        disabled={isLoading}>
					{isLoading ? 'Creating...' : 'Create Parking Space'}
				</button>
			</form>

			<form onSubmit={handleUpdate} className="mb-6">
				<div className="mb-4">
					<label htmlFor="parkingSpaceId" className="block text-sm font-bold mb-2">Parking Space ID:</label>
					<input
						type="number"
						id="parkingSpaceId"
						name="parkingSpaceId"
						value={parkingSpaceId}
						onChange={handleChangeId}
						className="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 mb-3 leading-tight focus:outline-none focus:shadow-outline"
					/>
				</div>
				<button type="submit"
				        className="bg-yellow-500 hover:bg-yellow-700 text-white font-bold py-2 px-4 rounded focus:outline-none focus:shadow-outline"
				        disabled={isLoading}>
					{isLoading ? 'Updating...' : 'Update Parking Space'}
				</button>
			</form>

			<form onSubmit={handleDelete}>
				<div className="mb-4">
					<label htmlFor="parkingSpaceId" className="block text-sm font-bold mb-2">Parking Space ID:</label>
					<input
						type="number"
						id="parkingSpaceId"
						name="parkingSpaceId"
						value={parkingSpaceId}
						onChange={handleChangeId}
						className="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 mb-3 leading-tight focus:outline-none focus:shadow-outline"
					/>
				</div>
				<button type="submit"
				        className="bg-red-500 hover:bg-red-700 text-white font-bold py-2 px-4 rounded focus:outline-none focus:shadow-outline"
				        disabled={isLoading}>
					{isLoading ? 'Deleting...' : 'Delete Parking Space'}
				</button>
			</form>

			{error && <p className="text-red-500 text-xs italic">{error}</p>}
			{success && <p className="text-green-500 text-xs italic">{success}</p>}
		</div>
	);
};

