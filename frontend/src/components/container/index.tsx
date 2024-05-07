import React from 'react';

export const Container = ({children}: {
	children: React.ReactNode
}) => {
	return (
		<div className='container mx-auto pl-[15px] pr-[15px]'>
			{children}
		</div>
	);
};
