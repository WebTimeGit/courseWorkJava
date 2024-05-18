
export const PARKING = {
	reserve: '/api/parkingspaces/reserve',
	release: '/api/parkingspaces/release',
	userHistory: '/api/parkingspaces/history/user',
}

export interface ParkingHistory {
	id: string;
	userId: number;
	parkingSpaceId: number;
	startTime: string;
	endTime: string | null;
}
