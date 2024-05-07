import React, { useEffect } from 'react';
import { useRouter } from 'next/router';
import { useAuthContext } from '@/context/authContext';
import {openRoutes, ROUTES, RouteType} from "@/routes/routes";

export const AuthRedirect: React.FC<{
	children: React.ReactElement;
}> = ({ children }) => {
	const { isAuth } = useAuthContext()
	const router = useRouter()

	useEffect(() => {

		const isClosedRoute = !openRoutes.some(route => {
			if (route.type === RouteType.exact) {
				return route.path === router.pathname
			} else if (route.type === RouteType.withId) {
				return new RegExp(route.path).test(router.pathname)
			}
			return false
		})

		if (!isAuth && isClosedRoute) {
			router.push(ROUTES.auth.login).then()
		}
	}, [isAuth, router])

	return children
}
