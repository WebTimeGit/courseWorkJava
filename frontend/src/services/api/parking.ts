
export const PARKING = {
	reserve: (parkingSpaceId: number) => `/api/parkingspaces/reserve/${parkingSpaceId ? parkingSpaceId : ''}`,
	release: (parkingSpaceId: number) => `/api/parkingspaces/release/${parkingSpaceId ? parkingSpaceId : ''}`,
	userHistory: '/api/parkingspaces/history/user',
}

export interface ParkingHistory {
	id: string;
	userId: number;
	parkingSpaceId: number;
	startTime: string;
	endTime: string | null;
}
