import React from 'react';
import useSWR from 'swr';
import { fetcher } from '@/services/axios';
import { API } from '@/services/api/api';
import {ParkingSpace} from "@/services/api/parking_spaces";


export const ParkingSpacesList: React.FC = () => {
	const { data, error } = useSWR<ParkingSpace[]>(API.PARKING_SPACES.getAll, fetcher);

	if (error) return <div>Failed to load parking spaces</div>;
	if (!data) return <div>Loading...</div>;

	return (
		<div className="max-w-md mx-auto my-10">
			<h2 className="text-xl font-bold mb-4">Parking Spaces</h2>
			<ul>
				{data.map((space) => (
					<li key={space.id} className="mb-2">
						<div className="shadow appearance-none border rounded py-2 px-3 text-gray-700 leading-tight">
							<p>ID: {space.id}</p>
							<p>Status: {space.status}</p>
						</div>
					</li>
				))}
			</ul>
		</div>
	);
};
