import React, { useState, ChangeEvent, FormEvent } from 'react';
import { loggedAxios } from '@/services/axios';
import { API } from '@/services/api/api';
import { mutate } from 'swr';
import {ParkingSpace} from "@/services/api/parking_spaces";
import {ParkingHistory} from "@/services/api/parking";

export const ManageParkingSpacesForm: React.FC = () => {
	const [status, setStatus] = useState<ParkingSpace['status']>('FREE');
	const [updateStatusReason, setUpdateStatusReason] = useState('');
	const [parkingSpaceId, setParkingSpaceId] = useState<number | string>('');
	const [isLoading, setIsLoading] = useState<boolean>(false);
	const [error, setError] = useState<string | null>(null);
	const [success, setSuccess] = useState<string | null>(null);
	const [parkingHistory, setParkingHistory] = useState<ParkingHistory[] | []>([]);

	const handleChangeStatus = (e: ChangeEvent<HTMLSelectElement>) => {
		setStatus(e.target.value as ParkingSpace['status']);
	};
	const handleChangeUpdateStatusReason = (e: ChangeEvent<HTMLTextAreaElement>) => {
		setUpdateStatusReason(e.target.value);
	};
	const handleChangeId = (e: ChangeEvent<HTMLInputElement>) => {
		const value = e.target.value;
		setParkingSpaceId(value === '' ? '' : Number(value));
	};

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
			mutate(API.PARKING_SPACES.getAllForAdmin);
			mutate(API.PARKING_SPACES.count);
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
			mutate(API.PARKING_SPACES.getAllForAdmin);
			mutate(API.PARKING_SPACES.count);
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
				serviceReason: status === 'SERVICE' ? updateStatusReason : undefined,
			};

			await loggedAxios.put<ParkingSpace>(`${API.PARKING_SPACES.update}/${parkingSpaceId}`, data);
			mutate(API.PARKING_SPACES.getAllForAdmin);
			mutate(API.PARKING_SPACES.count);
			setSuccess('Parking space updated successfully!');
		} catch (error: any) {
			console.error('Error:', error);
			setError('Failed to update parking space. ' + (error.response?.data?.message || error.message));
		} finally {
			setIsLoading(false);
		}
	};

	const handleHistory = async (e: FormEvent) => {
		e.preventDefault();
		setIsLoading(true);
		setError(null);
		setSuccess(null);

		try {
			const response = await loggedAxios.get(`${API.PARKING.userHistory}`);
			setParkingHistory(response?.data)
			setSuccess('Get your history parking successfully!');
		} catch (error: any) {
			console.error('Error:', error);
			setError('Failed to Get your history parking. ' + (error.response?.data?.message || error.message));
		} finally {
			setIsLoading(false);
		}
	};
	const handleHistorySpace = async (e: FormEvent) => {
		e.preventDefault();
		setIsLoading(true);
		setError(null);
		setSuccess(null);

		try {
			const response = await loggedAxios.get(`${API.PARKING_SPACES.spaceHistory}/${parkingSpaceId}`);
			setParkingHistory(response?.data)
			setSuccess('Get history space parking successfully!');
		} catch (error: any) {
			console.error('Error:', error);
			setError('Failed to Get history space parking. ' + (error.response?.data?.message || error.message));
		} finally {
			setIsLoading(false);
		}
	};
	const handleHistoryUser = async (e: FormEvent) => {
		e.preventDefault();
		setIsLoading(true);
		setError(null);
		setSuccess(null);

		try {
			const response = await loggedAxios.get(`${API.USERS.userHistory}/${parkingSpaceId}`);
			setParkingHistory(response?.data)
			setSuccess('Get history space parking successfully!');
		} catch (error: any) {
			console.error('Error:', error);
			setError('Failed to Get history space parking. ' + (error.response?.data?.message || error.message));
		} finally {
			setIsLoading(false);
		}
	};

	return (
		<div className="max-w-md mx-auto my-10">
			<form className="mb-6">
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
						<option value="SERVICE">Service</option>
					</select>
					{status === 'SERVICE' && (
						<div className="mb-4">
							<label htmlFor="updateStatusReason" className="block text-sm font-bold mb-2">Reason for Service:</label>
							<textarea
								id="updateStatusReason"
								name="updateStatusReason"
								value={updateStatusReason}
								onChange={handleChangeUpdateStatusReason}
								className="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
							/>
						</div>
					)}
				</div>

				<button onClick={(e) => handleCreate(e)}
				        className="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded focus:outline-none focus:shadow-outline"
				        disabled={isLoading}>
					{isLoading ? 'Creating...' : 'Create Parking Space'}
				</button>

				<div className="mb-4">
					<label htmlFor="parkingSpaceId" className="block text-sm font-bold mb-2">Parking Space ID / User ID:</label>
					<input
						type="number"
						id="parkingSpaceId"
						name="parkingSpaceId"
						value={parkingSpaceId}
						onChange={handleChangeId}
						className="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 mb-3 leading-tight focus:outline-none focus:shadow-outline"
					/>
				</div>

				<button onClick={(e) => handleUpdate(e)}
				        className="bg-yellow-500 hover:bg-yellow-700 text-white font-bold py-2 px-4 rounded focus:outline-none focus:shadow-outline"
				        disabled={isLoading}>
					{isLoading ? 'Updating...' : 'Update Parking Space'}
				</button>

				<button onClick={(e) => handleDelete(e)}
				        className="bg-red-500 hover:bg-red-700 text-white font-bold py-2 px-4 rounded focus:outline-none focus:shadow-outline"
				        disabled={isLoading}>
					{isLoading ? 'Deleting...' : 'Delete Parking Space'}
				</button>

				<button onClick={(e) => handleHistorySpace(e)}
				        className="bg-amber-500 hover:bg-amber-700 text-white font-bold py-2 px-4 rounded focus:outline-none focus:shadow-outline"
				        disabled={isLoading}>
					{isLoading ? 'Getting history parking space...' : 'Get history parking space'}
				</button>

				<button onClick={(e) => handleHistoryUser(e)}
				        className="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded focus:outline-none focus:shadow-outline"
				        disabled={isLoading}>
					{isLoading ? 'Getting history parking user...' : 'Get history parking user'}
				</button>
			</form>

			{error && <p className="text-red-500 text-xs italic">{error}</p>}
			{success && <p className="text-green-500 text-xs italic">{success}</p>}

			<button onClick={(e) => handleHistory(e)}
			        className="bg-purple-500 hover:bg-purple-700 text-white font-bold py-2 px-4 rounded focus:outline-none focus:shadow-outline"
			        disabled={isLoading}>
				{isLoading ? 'Getting my history parking...' : 'Get my history parking'}
			</button>
			<ul>
				{
					parkingHistory?.length > 0 &&
					parkingHistory?.map(parking => {

						return (
							<li key={parking?.id}>
								<span>parking space: {parking?.parkingSpaceId}</span> <br/>
								<span>User ID: {parking?.userId}</span> <br/>
								<span>startTime parking space: {parking?.startTime}</span> <br/>
								<span>endTime parking space: {parking?.endTime}</span> <br/>
							</li>
						)
					})
				}
			</ul>

		</div>
	);
};
