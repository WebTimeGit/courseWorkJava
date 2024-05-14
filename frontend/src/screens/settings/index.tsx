import React from 'react';
import {ManageParkingSpacesForm} from "@/components/forms/ManageParkingSpacesForm"
import {ParkingSpacesList} from "@/components/parkingSpacesList";

export const Settings = () => {
	return (
		<>
			<ParkingSpacesList/>
			<ManageParkingSpacesForm/>
		</>

	)
}