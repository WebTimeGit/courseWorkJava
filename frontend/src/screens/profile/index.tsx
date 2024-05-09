import styles from './profile.module.scss';
import {useAuthContext} from "@/context/authContext";
import {UpdateUserProfileForm} from "@/components/forms/updateUserProfileForm";

export const Profile = () => {
	const {userData: userInfo} = useAuthContext();

	if (!userInfo) {
		return <></>;
	}


	return (
		<div className={styles.profileWrapper}>
			<p>Ім'я {userInfo?.username}</p>
			<p>Ви {userInfo?.role}</p>
			<p>ID {userInfo?.id}</p>
			<p>Пошта {userInfo?.email}</p>
			<p>Дата реєстрації {userInfo?.registrationDate}</p>
			<UpdateUserProfileForm />
		</div>
	)
}
