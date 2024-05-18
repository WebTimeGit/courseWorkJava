export const PARKING_SPACES = {
	getAllForUser: '/api/parkingspaces/getAll',
	count: '/api/parkingspaces/count',
	getAllForAdmin: '/api/admin/parkingspaces/getAll',
	create: '/api/admin/parkingspaces/create',
	update: '/api/admin/parkingspaces/update',
	delete: '/api/admin/parkingspaces/delete',
	spaceHistory: '/api/admin/parkingspaces/history/space',
}


export interface ParkingSpace {
	id: number;
	status: 'FREE' | 'OCCUPIED' | 'SERVICE';
	serviceReason?: string
}

export interface ParkingCount {
	total: number;
	free: number;
}