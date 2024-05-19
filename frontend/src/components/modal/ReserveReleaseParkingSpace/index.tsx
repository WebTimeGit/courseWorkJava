import React, {FC, useState} from 'react';
import {ParkingSpace} from "@/services/api/parking_spaces";
import {loggedAxios} from "@/services/axios";
import {API} from "@/services/api/api";
import {mutate} from "swr";
import styles from './ReserveReleaseParkingSpace.module.scss';
import {toast} from "react-toastify";


type TProps = {
	space?: ParkingSpace
	onClose?: () => void
}


export const ReserveReleaseParkingSpace: FC<TProps> = ({space, onClose}) => {
	const [isLoading, setIsLoading] = useState<boolean>(false)
	const isSpaceFree = space?.status === 'FREE'

	const onHandleReserve = async () => {
		setIsLoading(true)

		try {
			if (!space) return

			await loggedAxios.post(`${API.PARKING.reserve(space.id)}`)
			mutate(API.PARKING_SPACES.getAllForUser).then()
			mutate(API.PARKING_SPACES.count).then()
			toast.success('Parking space reserved successfully!')
			onClose && onClose()
		} catch (error: any) {
			console.error('Error:', error)
			toast.error(error.response?.data?.message || error.message)
		} finally {
			setIsLoading(false)
		}
	}

	const handleRelease = async () => {
		setIsLoading(true)

		try {
			if (!space) return

			await loggedAxios.post(`${API.PARKING.release(space.id)}`)
			mutate(API.PARKING_SPACES.getAllForUser).then()
			mutate(API.PARKING_SPACES.count).then()
			toast.success('Parking space released successfully!')
			onClose && onClose()
		} catch (error: any) {
			console.error('Error:', error)
			toast.error(error.response?.data?.message || error.message)
		} finally {
			setIsLoading(false)
		}
	}

	const onHandlerAccept = async () => {
		if (isSpaceFree) {
			await onHandleReserve()
		} else {
			await handleRelease()
		}
	}


	return (
		<div className={styles.wrapper}>
			<h3>Parking space manager</h3>
			<p>
				Ви обрали паркомісце № {space?.id}, зараз паркомісце {space?.status}. Ви
				хочете {isSpaceFree ? 'зарезервувати' : 'звільнити'} його зараз?
			</p>

			<div className={styles.btns}>
				<button onClick={onHandlerAccept}
				        disabled={isLoading}
				        className="bg-green-500 hover:bg-green-700 text-white font-bold py-2 px-4 rounded focus:outline-none focus:shadow-outline">
					{isSpaceFree ? 'Зарезервувати' : 'Звільнити'}
				</button>

				<button onClick={onClose}
				        disabled={isLoading}
				        className="bg-purple-500 hover:bg-purple-700 text-white font-bold py-2 px-4 rounded focus:outline-none focus:shadow-outline">
					Закрити
				</button>
			</div>
		</div>
	)
}
