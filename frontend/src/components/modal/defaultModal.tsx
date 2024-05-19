import React from 'react';
import Modal from 'react-modal';
import styles from './defaultModal.module.scss'
import closeIcon from '../../../public/close.svg'
import Image from "next/image";

interface CustomModalProps {
	isOpen: boolean
	onRequestClose: () => void
	contentLabel: string
	children: React.ReactNode
	customStyles?: { content: React.CSSProperties }
}

export const DefaultModal: React.FC<CustomModalProps> = (
	{
		isOpen,
		onRequestClose,
		contentLabel,
		children,
		customStyles
	}) => {


	return (
		<Modal
			isOpen={isOpen}
			onRequestClose={onRequestClose}
			style={customStyles}
			contentLabel={contentLabel}
			className={styles.modals}
			overlayClassName={styles.overlay}
		>
			<button className={styles.close} onClick={onRequestClose}>
				<Image src={closeIcon} width={40} height={40} alt="close" />
			</button>
			{children}
		</Modal>
	)
}
