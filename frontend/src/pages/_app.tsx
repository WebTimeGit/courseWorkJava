import "@/styles/globals.scss";
import nextCookies from "next-cookies";
import type {AppProps} from "next/app";
import React from "react";
import {Layout} from "@/components/layout/layout";
import {AuthRedirect} from "@/routes/AuthRedirect";
import {AuthContextProvider} from "@/context/authContext";
import Modal from "react-modal";
import {ToastContainers} from "@/components/toast/toastContainers";


Modal.setAppElement('#__next');

export default function App({Component, pageProps}: AppProps) {
	return (
		<>
			<AuthContextProvider>
				<AuthRedirect>
					<Layout>
						<Component {...pageProps} />
					</Layout>
				</AuthRedirect>
			</AuthContextProvider>
			<ToastContainers/>
		</>
	)
}

export async function getServerSideProps(context: any) {
	const allCookies = nextCookies(context);
	const cookieValue = allCookies['token'] || '';

	return {
		props: {
			cookieValue,
			test: "test"
		},
	};
}
