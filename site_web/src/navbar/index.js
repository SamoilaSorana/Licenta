import React, { useState, useEffect } from "react";
import Cookies from "js-cookie";
import { PersonCircle, Bell } from "react-bootstrap-icons";
import axios from "axios";
import './style.css';

function Navbar() {
    const [isDropdownOpen, setIsDropdownOpen] = useState(false);
    const [isNotifOpen, setIsNotifOpen] = useState(false);
    const [notifications, setNotifications] = useState([]);
    const [permissions, setPermissions] = useState([]);

    const toggleDropdown = () => setIsDropdownOpen(!isDropdownOpen);
    const toggleNotifDropdown = () => setIsNotifOpen(!isNotifOpen);

    const logOut = () => {
        Cookies.remove('token');
        window.location.href = '/';
    };

    const userLoggedIn = Cookies.get('token');

    useEffect(() => {
        const token = Cookies.get("token");
        if (token) {
            axios.get("http://localhost:4000/idm/api/auth/check-token", {
                headers: { Authorization: `Bearer ${token}` },
            })
                .then((res) => setPermissions(res.data.permisiuni))
                .catch(() => {
                    Cookies.remove("token");
                    setPermissions([]);
                });
        }
    }, []);

    useEffect(() => {
        const token = Cookies.get("token");
        if (token && permissions.includes("SEE_NOTIFICATIONS")) {
            axios.get("http://localhost:4000/logic/help", {
                headers: { Authorization: `Bearer ${token}` },
            })
                .then(res => {
                    const unread = res.data.filter(n => n.read === 0);
                    setNotifications(unread);
                })
                .catch(err => console.error("Error loading notifications:", err));
        }
    }, [permissions]);

    return (
        <nav className="navbar navbar-expand-lg navbar-dark custom-navbar">
            <div className="container">
                <a className="navbar-brand" href="/">About</a>
                <button className="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
                    <span className="navbar-toggler-icon"></span>
                </button>

                <div className="collapse navbar-collapse" id="navbarNav">
                    <ul className="navbar-nav me-auto mb-2 mb-lg-0">
                        {permissions.includes("VIEW_LECTURES") && (
                            <li className="nav-item">
                                <a className="nav-link" href="/lectures">Lectures</a>
                            </li>
                        )}
                        <li className="nav-item">
                            <a className="nav-link" href="/contact">Contact</a>
                        </li>
                    </ul>

                    <ul className="navbar-nav">
                        {permissions.includes("SEE_NOTIFICATIONS") && (
                            <li className="nav-item dropdown">
                                <button className="btn nav-profile-icon" onClick={toggleNotifDropdown}>
                                    <Bell className="nav-icon" />
                                </button>
                                {isNotifOpen && (
                                    <ul className="dropdown-menu dropdown-menu-end show profile-dropdown">
                                        {notifications.length === 0 ? (
                                            <li className="dropdown-item text-muted">No new notifications</li>
                                        ) : (
                                            notifications.map((notif) => (
                                                <li key={notif.id} className="dropdown-item">
                                                    {notif.studentname} – {new Date(notif.date).toLocaleString()}
                                                </li>
                                            ))
                                        )}
                                        <li><hr className="dropdown-divider" /></li>
                                        <li>
                                            <button
                                                className="dropdown-item text-primary"
                                                onClick={() => {
                                                    if (permissions.includes("SEE_NOTIFICATIONS")) {
                                                        window.location.href = "/notifications";
                                                    } else {
                                                        window.location.href = "/not-authorized";
                                                    }
                                                }}
                                            >
                                                Vezi toate notificarile
                                            </button>
                                        </li>
                                    </ul>
                                )}
                            </li>
                        )}

                        {userLoggedIn ? (
                            <li className="nav-item dropdown">
                                <button className="btn nav-profile-icon" onClick={toggleDropdown}>
                                    <PersonCircle className="nav-icon" />
                                </button>
                                {isDropdownOpen && (
                                    <ul className="dropdown-menu dropdown-menu-end show profile-dropdown">
                                        <li>
                                            <button className="dropdown-item" onClick={logOut}>Log Out</button>
                                        </li>
                                        <li>
                                            <button className="dropdown-item" onClick={() => window.location.href = "/profile"}>
                                                Profile
                                            </button>
                                        </li>
                                    </ul>
                                )}
                            </li>
                        ) : (
                            <>
                                <li className="nav-item">
                                    <a className="nav-link" href="/login">Login</a>
                                </li>
                                <li className="nav-item">
                                    <a className="nav-link" href="/register">Register</a>
                                </li>
                            </>
                        )}
                    </ul>
                </div>
            </div>
        </nav>
    );
}

export default Navbar;
