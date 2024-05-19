import {AUTH} from "@/services/api/auth";
import {USERS} from "@/services/api/users";
import {PARKING_SPACES} from "@/services/api/parking_spaces";
import {PARKING} from "@/services/api/parking";

export const API = {
	home: '/',
	AUTH,
	USERS,
	PARKING_SPACES,
	PARKING
}


export interface ErrorResponseDTO {
	status: number;
	message: string;
	timestamp: number;
}
