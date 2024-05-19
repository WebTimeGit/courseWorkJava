import React, {useState} from 'react';
import useSWR from 'swr';
import {fetcher} from '@/services/axios';
import { API } from '@/services/api/api';
import {ParkingSpace, ParkingCount} from "@/services/api/parking_spaces";
import {useAuthContext} from "@/context/authContext";
import {useOpen} from "@/hooks/useOpen";
import {DefaultModal} from "@/components/modal/defaultModal";
import {ReserveReleaseParkingSpace} from "@/components/modal/ReserveReleaseParkingSpace";


export const ParkingSpacesList: React.FC = () => {
	const { userData } = useAuthContext()
	const {isOpen, onOpen, onClose} = useOpen()

	const { data: parkingSpaceCount, error: parkingSpaceCountError } = useSWR<ParkingCount>(API.PARKING_SPACES.count, fetcher);
	const { data: parkingSpace, error: parkingSpaceError } = useSWR<ParkingSpace[]>(
		userData?.role === 'USER' ? API.PARKING_SPACES.getAllForUser : API.PARKING_SPACES.getAllForAdmin, fetcher
	);

	const [currentParkingPlace, setCurrentParkingPlace] = useState<ParkingSpace | undefined>()

	const onHandlePopUp = (space: ParkingSpace) => {
		onOpen()
		setCurrentParkingPlace(space)
	}

	if (parkingSpaceError) return <div>Failed to load parking spaces</div>;
	if (!parkingSpace) return <div>Loading...</div>;

	return (
		<>
			<div className="max-w-md mx-auto my-10">
				<h2 className="text-xl font-bold mb-4">
					Parking Spaces
					<span> total: {parkingSpaceCount?.total}</span>
					<span> free: {parkingSpaceCount?.free}</span>
				</h2>
				{parkingSpaceCountError && 'error count space'}
				<ul>
					{parkingSpace.map((space) => (
						<li key={space.id} className="mb-2" onClick={() => onHandlePopUp(space)}>
							<div className="shadow appearance-none border rounded py-2 px-3 text-gray-700 leading-tight">
								<p>ID: {space.id}</p>
								<p>Status: {space.status}</p>
							</div>
						</li>
					))}
				</ul>
			</div>
			<DefaultModal isOpen={isOpen} onRequestClose={onClose} contentLabel={'reserveReleaseParkingModal'}>
				<ReserveReleaseParkingSpace space={currentParkingPlace} onClose={onClose}/>
			</DefaultModal>
		</>
	);
};
