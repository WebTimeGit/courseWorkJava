
export const USERS = {
	userInfo: '/api/users/userInfo/',
	profile: (id: number) => `/api/users/profile${id ? `/${id}` : "/"}`,
	userHistory: '/api/admin/users/history',
}