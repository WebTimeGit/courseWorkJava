

export enum RouteType {
	root,
	withId,
	exact,
}

export const ROUTES = {
	home: '/',
	auth : {
		login: '/login',
		registration: '/registration',
	},
	users: {
		profile: (id: string | number | undefined) =>
			id ? '/profile/' + id : '/profile',
	}
}


export const openRoutes = [
	{ path: ROUTES.home, type: RouteType.exact },
	{ path: ROUTES.auth.login, type: RouteType.exact },
	{ path: ROUTES.auth.registration, type: RouteType.exact },
	{ path: 'profile/[userId]', type: RouteType.withId },
];