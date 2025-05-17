import React, { useEffect, useState } from "react";
import axios from "axios";
import Cookies from "js-cookie";
import { useNavigate } from "react-router-dom";

const ProfilePage = () => {
    const [userInfo, setUserInfo] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const navigate = useNavigate();

    useEffect(() => {
        const token = Cookies.get("token");

        if (!token) {
            setError("No token found. Please log in.");
            setLoading(false);
            return;
        }

        axios.get("http://localhost:4000/logic/user/get-info", {
            headers: {
                Authorization: `Bearer ${token}`
            }
        })
            .then(res => {
                setUserInfo(res.data);
            })
            .catch(() => {
                setError("Failed to load user information.");
            })
            .finally(() => setLoading(false));
    }, []);

    if (loading) return <h2>Loading profile...</h2>;
    if (error) return <h2 style={{ color: 'red' }}>{error}</h2>;

    const isAdmin = userInfo.rol?.rol_name === "Admin";

    return (
        <div style={{ padding: "30px", maxWidth: "600px", margin: "0 auto" }}>
            <h1>User Profile</h1>
            <p><strong>First Name:</strong> {userInfo.prenume}</p>
            <p><strong>Last Name:</strong> {userInfo.nume}</p>
            <p><strong>Email:</strong> {userInfo.email}</p>
            <p><strong>Username:</strong> {userInfo.username}</p>
            <p><strong>Role:</strong> {userInfo.rol?.rol_name}</p>

            {isAdmin && userInfo.rol?.permisiuniString && (
                <div>
                    <h3>Permissions:</h3>
                    <ul>
                        {userInfo.rol.permisiuniString.map((perm, index) => (
                            <li key={index}>{perm}</li>
                        ))}
                    </ul>
                </div>
            )}

            <button
                onClick={() => navigate("/graded-tests")}
                style={{
                    marginTop: "20px",
                    padding: "10px 20px",
                    fontSize: "16px",
                    backgroundColor: "#1976d2",
                    color: "white",
                    border: "none",
                    borderRadius: "6px",
                    cursor: "pointer"
                }}
            >
                View My Tests
            </button>
        </div>
    );
};

export default ProfilePage;