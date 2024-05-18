export const PARKING_SPACES = {
	getAll: '/api/parkingspaces/getAll',
	count: '/api/parkingspaces/count',
	create: '/api/admin/parkingspaces/create',
	update: '/api/admin/parkingspaces/update',
	delete: '/api/admin/parkingspaces/delete',
	spaceHistory: '/api/admin/parkingspaces/history/space',
}


export interface ParkingSpace {
	id: number;
	status: 'FREE' | 'OCCUPIED';
}

export interface ParkingCount {
	total: number;
	free: number;
}