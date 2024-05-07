import React from 'react';
import styles from './footer.module.scss';
import classNames from "classnames";
import {Container} from "@/components/container";
export const Footer = () => {

	const currentYear = new Date().getFullYear();


	return (
		<footer className={classNames('text-center p-4 mt-8', styles.footer)}>
			<Container >
				<p className="text-sm">
					&copy; {currentYear} WebTime. Всі права захищені.
				</p>
			</Container>
		</footer>
	);
};
