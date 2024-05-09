import React, { useEffect } from 'react';
import { useRouter } from 'next/router';
import { useAuthContext } from '@/context/authContext';
import {openRoutes, ROUTES, RouteType} from "@/routes/routes";

export const AuthRedirect: React.FC<{
	children: React.ReactElement;
}> = ({ children }) => {
	const { isAuth } = useAuthContext()
	const router = useRouter()

	const checkDynamicPathMatch = (routPath: string, dynamicPath: string) => {
		return (
			router.pathname.includes(routPath) &&
			router.pathname.split('/')[3] === dynamicPath
		)
	}

	useEffect(() => {
		const isOpenPath = openRoutes.some(route => {
			if (route.type === RouteType.exact) return route.path === router.pathname
			else if (route.type === RouteType.withId) return checkDynamicPathMatch(route.path, '[userId]');
			return false
		})

		if (!isAuth && !isOpenPath) {
			router.push(ROUTES.auth.login).then()
		}
	}, [isAuth, router])

	return children
}
