import React, { useEffect, useState } from "react";
import Cookies from "js-cookie";
import axios from "axios";
import despreImg from "../images/Despre.png"; // asigură-te că ai salvat imaginea acolo
import "./style.css";

const Home = () => {
    const [permisiuni, setPermisiuni] = useState([]);

    useEffect(() => {
        const token = Cookies.get("token");
        if (token) {
            axios
                .get("http://localhost:4000/idm/api/auth/check-token", {
                    headers: {
                        Authorization: `Bearer ${token}`,
                    },
                })
                .then((response) => {
                    console.log("✅ Token valid:", response.data);
                    setPermisiuni(response.data.permisiuni);
                })
                .catch((error) => {
                    console.log("❌ Token invalid:", error.response?.data);
                    Cookies.remove("token");
                    setPermisiuni([]);
                });
        } else {
            setPermisiuni([]);
        }
    }, []);

    return (
        <div style={{ textAlign: "center", marginTop: "30px" }}>
            {permisiuni.length > 0 ? (
                <img
                    src={despreImg}
                    alt="Despre MathClub"
                    style={{ width: "100%", maxWidth: "1200px" }}
                />
            ) : (
                <h2 style={{ marginTop: "50px" }}>Welcome! Please login to access the platform.</h2>
            )}
        </div>
    );
};

export default Home;
