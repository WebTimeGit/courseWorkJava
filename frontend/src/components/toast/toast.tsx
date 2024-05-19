import React from "react";

import styles from './toast.module.scss';

type ToastMessageProps = {
    title: string;
    message: string;
}

export const ToastMessage: React.FC<ToastMessageProps> = ({ title, message }) => (
    <div className={styles.toast}>
     <h4> {title}</h4>
     <p>{message}</p>
    </div>
);