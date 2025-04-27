import React, {useEffect, useState} from "react";
import Cookies from 'js-cookie';
import image from '../images/image.jpg';
import './style.css';
import {useWindowSize} from "./useWindowSize";
import axios from "axios";
import { useNavigate } from 'react-router-dom';

const Login = () => {
    // const navigate = useNavigate();
    const [width, height] = useWindowSize();
    const navigate = useNavigate();


    const [username, usernameChange] = useState("");
    const [password, passwordChange] = useState("");

    const handleSubmit = async (e) => {
        e.preventDefault();

        try {
            const response = await fetch(`http://localhost:4000/idm/user/login`, {
                method: "POST",
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({
                    username: username,
                    password: password
                })
            });

            if (response.status === 200) {
                const data = await response.json();
                if (data.token) {
                    Cookies.set('token', data.token);
                    console.log("Login successful, token saved!");
                    navigate('/');
                } else {
                    alert('Token not received');
                }
            } else if (response.status === 404) {
                alert('Username or password incorrect');
            } else {
                alert('An error occurred');
            }

        } catch (err) {
            console.log(err.message);
            alert('Request failed');
        }
    };

    return (
        <div style={{backgroundImage:`url(${image})`, backgroundSize:"contain",
            height:height - 57,width:width}}>
            <div style={{ display: 'flex' , justifyContent: 'center', alignItems: 'center'}}>
                {/*<div style={{ flex: '1' }}>*/}
                <div className="form-content">
                    <div className="form-items" style={{backgroundColor: "rgb(205, 238, 255)"}}>
                        <h3>Login</h3>
                        <form onSubmit={handleSubmit}>

                            <div className="form-floating col-md-12">
                                <input type="text" className="form-control" id="username" placeholder="Enter Username"
                                       value={username} onChange={e => usernameChange(e.target.value)}/>
                                <label>Username</label>
                            </div>

                            <div className="form-floating col-md-12">
                                <input type="password" className="form-control" id="password" placeholder="Enter password"
                                       value={password} onChange={e => passwordChange(e.target.value)}/>
                                <label>Password</label>
                            </div>

                            <div className="form-button m-4" style={{ display: 'flex', justifyContent: 'center', alignItems: 'center' }}>
                                <button id="submit" type="submit" className="btn btn-primary" style={{ padding: '10px 30px', display: 'flex', justifyContent: 'center', alignItems: 'center' }}>
                                    Login
                                </button>
                            </div>

                        </form>
                        <br/>
                    </div>
                </div>
            </div>
        </div>
    )
}

export default Login;