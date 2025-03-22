import React, {useState} from "react";
import Cookies from "js-cookie";
import {PersonCircle} from "react-bootstrap-icons";


import './style.css';

function Navbar() {

    const [isDropdownOpen, setIsDropdownOpen] = useState(false);

    const toggleDropdown = () => {
        setIsDropdownOpen(!isDropdownOpen);
    };
    const logOut = () => {
        Cookies.remove('googleID')
        Cookies.remove('user_id')

    };

    const myPetsPage = () => {

    }

    return (
        <nav className="navbar navbar-expand-lg navbar-dark bg-dark">
            <button className="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav"
                    aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
                <span className="navbar-toggler-icon"></span>
            </button>
            <div className="collapse navbar-collapse" id="navbarNav">
                <ul className="navbar-nav">
                    <li className="nav-item active">
                        <a className="nav-link" href="/">Home</a>
                    </li>
                    <li className="nav-item active">
                        <a className="nav-link" href="/pets">Pets</a>
                    </li>
                    <li className="nav-item active">
                        <a className="nav-link" href="/donations">Donations</a>
                    </li>
                    <li className="nav-item active">
                        <a className="nav-link" href="/contact">Contact</a>
                    </li>
                </ul>
                <ul className="navbar-nav ms-auto">
                    {Cookies.get('user_id') ? (
                            <li className="nav-item active">
                                <button className="button btn btn-dark btn-xl dropdown" tabIndex="0" onClick={toggleDropdown}>
                                    <PersonCircle className="btn-icon"></PersonCircle>
                                </button>
                                <div className={`dropdown-menu ${isDropdownOpen ? "show" : ""}`}>
                                    <a className="dropdown-item" href="#" onClick={logOut}>Log Out</a>
                                    <a className="dropdown-item" href="/adoptionRequests">My Pets</a>
                                    <a className="dropdown-item" href="/requests">Applied for...</a>
                                </div>
                            </li>
                        ) :
                        (
                            <li className="nav-item active">
                                <a className="nav-link" href="/login">Login</a>
                            </li>
                        )
                    }
                    {Cookies.get('user_id') ? (
                            <div/>
                        ) :
                        (
                            <li className="nav-item active">
                                <a className="nav-link" href="/register">Register</a>
                            </li>
                        )
                    }
                </ul>
            </div>
        </nav>
    );
};

export default Navbar;