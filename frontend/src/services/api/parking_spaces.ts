export const PARKING_SPACES = {
	create: '/api/parkingspaces',
	delete: '/api/parkingspaces',
	update: '/api/parkingspaces',
	getAll: '/api/parkingspaces',
	getCount: '/api/parkingspaces/count',
	getById: (id: number) => `/api/parkingspaces/${id}`,
}


export interface ParkingSpace {
	id: number;
	status: 'FREE' | 'OCCUPIED';
}