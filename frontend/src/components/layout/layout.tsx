import {Header} from "@/components/header";
import {Footer} from "@/components/footer";
import styles from './publicLayout.module.scss'


export const Layout = ({children}: Readonly<{ children: React.ReactNode; }>) => {


	return (
		<div className={styles.layout}>
			<Header />
			<main className={styles.main}>
				{children}
			</main>
			<Footer />
		</div>
	)
}
