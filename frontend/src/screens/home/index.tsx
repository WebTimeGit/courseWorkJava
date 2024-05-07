'use client'
import React from 'react';
import {Container} from "@/components/container";
import Image from "next/image";
import Link from "next/link";
import {ROUTES} from "@/routes/routes";

export const Home = () => {


	return (
		<Container>
			<div className=" flex flex-col items-center p-4">
				<h1 className="text-3xl font-bold mt-8 mb-6">Ласкаво просимо до головної сторінки!</h1>
				<p className="mb-8 text-center max-w-md">
					Це проста заглушка домашньої сторінки, створена за допомогою Tailwind CSS з невеликими цікавими зображеннями.
				</p>

				<div className="grid grid-cols-2 md:grid-cols-3 gap-4 mb-8">
					{Array.from({length: 6}).map((_, i) => (
						<div key={i} className="overflow-hidden rounded-lg shadow-lg">
							<Image
								className="w-full h-auto object-cover transform hover:scale-105 transition-transform duration-200"
								width={300}
								height={200}
								src={`https://source.unsplash.com/random/300x200?sig=${Date.now()}-${i}`}
								alt={`Random Unsplash ${i}`}
							/>
						</div>
					))}
				</div>
				<Link href={ROUTES.home}>
					Generate new
				</Link>

			</div>
		</Container>
	);
};
