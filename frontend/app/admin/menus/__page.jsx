
"use client";

import { useEffect } from "react";
import { Menu } from "@/types";
import { useState } from "react";
import CategoryList from "./_components/CategoryList";

export default function MenusPage() {
    // fetch menus
    const [menus, setMenus] = useState([]);
    const [category, setCategory] = useState(null);

    console.log("MenusPage");

    useEffect(() => {

<<<<<<< HEAD
        // ${process.env.NEXT_PUBLIC_API_URL}/admin/menus
        const fetchMenus = async () => {

            const baseUrl = '';
            const url = new URL(`${baseUrl}/api/v1/admin/menus`, window.location.origin);
=======
        const fetchMenus = async () => {

            const url = new URL("/api/admin/menus", window.location.origin);
>>>>>>> acd0828dfdf61b419e0c5a38f70f4ab06fe7708e

            const params = url.searchParams;
            if (category) {
                params.set("cid", category.id);
            }

            const response = await fetch(url);
            const data = await response.json();
            // menus = data;
            setMenus(data);
        };

        fetchMenus(); // 여기서 fatch 하는 것이 올바른 곳일까요?
        console.log("MenusPage useEffect");

        return () => {
            console.log("MenusPage useEffect cleanup");
        };
    }, [category]);


    const categoryChangeHandler = (category) => {
        console.log("categoryChangeHandler" + category);
        setCategory(category);
    };

    return (
        <main>
            {/*
                카테고리 블록
                메뉴 목록 
            */}

            <CategoryList x={1} onCategoryChange={categoryChangeHandler} />

            <section>
                <h1>메뉴 목록</h1>
                <div>
                    {menus.map((menu) => (
                        <div key={menu.id}>
                            <h2>{menu.name}</h2>
                            <p>{menu.description}</p>
                            <p>{menu.price}</p>
                        </div>
                    ))}
                </div>
            </section>
        </main>
    );
}