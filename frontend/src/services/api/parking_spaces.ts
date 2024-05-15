export const PARKING_SPACES = {
	create: '/api/admin/parkingspaces/create',
	delete: '/api/admin/parkingspaces/delete',
	update: '/api/parkingspaces/update',
	getAll: '/api/parkingspaces/getAll',
	getCount: '/api/parkingspaces/count',
	getById: (id: number) => `/api/parkingspaces/${id}`,
}


export interface ParkingSpace {
	id: number;
	status: 'FREE' | 'OCCUPIED';
}