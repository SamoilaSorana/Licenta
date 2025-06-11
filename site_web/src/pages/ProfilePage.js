import React, { useEffect, useState } from "react";
import axios from "axios";
import Cookies from "js-cookie";
import { useNavigate } from "react-router-dom";
import "../pages/style.css";

const ProfilePage = () => {
    const [userInfo, setUserInfo] = useState(null);
    const navigate = useNavigate();

    useEffect(() => {
        const token = Cookies.get("token");
        if (!token) return;

        axios.get("http://localhost:4000/logic/user/get-info", {
            headers: { Authorization: `Bearer ${token}` }
        })
            .then(res => setUserInfo(res.data))
            .catch(err => console.log(err));
    }, []);

    if (!userInfo) return <div className="loading">Loading...</div>;

    const hasPermission = (permName) => {
        return userInfo.rol?.permisiuniString?.includes(permName);
    };

    return (
        <div className="profile-modern">
            <div className="profile-box">
                <h2 className="profile-name">{userInfo.prenume} {userInfo.nume}</h2>
                <p className="profile-role">{userInfo.rol?.rol_name}</p>

                <div className="profile-info">
                    <p><strong>Username:</strong> {userInfo.username}</p>
                    <p><strong>Email:</strong> {userInfo.email}</p>
                </div>

                {hasPermission("SEE_PERMISSIONS") && userInfo.rol.permisiuniString?.length > 0 && (
                    <div className="profile-card-perms">
                        <h3>Permissions:</h3>
                        <ul>
                            {userInfo.rol.permisiuniString.map((perm, idx) => (
                                <li key={idx}>{perm}</li>
                            ))}
                        </ul>
                    </div>
                )}

                <button className="profile-button" onClick={() => navigate("/graded-tests")}>
                    View My Tests
                </button>
            </div>
        </div>
    );
};

export default ProfilePage;
