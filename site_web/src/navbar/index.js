import React, { useState } from "react";
import Cookies from "js-cookie";
import { PersonCircle } from "react-bootstrap-icons";
import './style.css';


function Navbar() {
    const [isDropdownOpen, setIsDropdownOpen] = useState(false);

    const toggleDropdown = () => setIsDropdownOpen(!isDropdownOpen);

    const logOut = () => {
        Cookies.remove('googleID');
        Cookies.remove('user_id');
        window.location.href = '/';
    };

    const userLoggedIn = Cookies.get('user_id');

    return (
        <nav className="navbar navbar-expand-lg navbar-dark custom-navbar">
            <div className="container">
                <a className="navbar-brand" href="/">About</a>
                <button className="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
                    <span className="navbar-toggler-icon"></span>
                </button>

                <div className="collapse navbar-collapse" id="navbarNav">
                    <ul className="navbar-nav me-auto mb-2 mb-lg-0">
                        <li className="nav-item">
                            <a className="nav-link" href="/lectures">Lectures</a>
                        </li>
                        <li className="nav-item">
                            <a className="nav-link" href="/donations">Donations</a>
                        </li>
                        <li className="nav-item">
                            <a className="nav-link" href="/contact">Contact</a>
                        </li>
                    </ul>

                    <ul className="navbar-nav">
                        {userLoggedIn ? (
                            <li className="nav-item dropdown">
                                <button className="btn nav-profile-icon" onClick={toggleDropdown}>
                                    <PersonCircle size={28} />
                                </button>
                                {isDropdownOpen && (
                                    <ul className="dropdown-menu dropdown-menu-end show profile-dropdown">
                                        <li><button className="dropdown-item" onClick={logOut}>Log Out</button></li>
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
