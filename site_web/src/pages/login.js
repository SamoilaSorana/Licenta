import React, { useState, useEffect } from "react";
import Cookies from 'js-cookie';
import imagess from '../images/imagess.png';
import { useWindowSize } from "./useWindowSize";
import { useNavigate } from 'react-router-dom';

const Login = () => {
    const [width, height] = useWindowSize();
    const navigate = useNavigate();

    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");
    const [loading, setLoading] = useState(false);
    const [alreadyLoggedIn, setAlreadyLoggedIn] = useState(false);

    useEffect(() => {
        const token = Cookies.get('token');
        if (token) {
            setAlreadyLoggedIn(true);
        }
    }, []);

    const handleSubmit = async (e) => {
        e.preventDefault();
        setLoading(true);

        try {
            const response = await fetch(`http://localhost:4000/idm/user/login`, {
                method: "POST",
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ username, password }),
            });

            const data = await response.json();

            if (response.status === 200 && data.token) {
                Cookies.set('token', data.token);
                console.log();
                window.location.href = "/";
            } else if (response.status === 404) {
                alert('❌ Username or password incorrect');
            } else {
                alert('⚠️ An error occurred');
            }
        } catch (err) {
            console.error('❌ Request failed', err.message);
            alert('Network error. Please try again.');
        } finally {
            setLoading(false);
        }
    };

    return (
        <div style={{
            position: "relative",
            width: width,
            height: height,
            overflow: "hidden",
        }}>
            <div style={{
                backgroundImage: `url(${imagess})`,
                backgroundSize: "cover",
                backgroundRepeat: "no-repeat",
                backgroundPosition: "center",
                filter: "brightness(0.6)",
                position: "absolute",
                top: 0,
                left: 0,
                width: "100%",
                height: "100%",
                zIndex: 1,
            }} />

            <div style={{
                position: "relative",
                zIndex: 2,
                display: "flex",
                justifyContent: "center",
                alignItems: "center",
                height: "100%",
            }}>
                <div style={{
                    backgroundColor: "rgba(255,255,255,0.95)",
                    padding: "2rem 3rem",
                    borderRadius: "20px",
                    maxWidth: "400px",
                    width: "100%",
                    boxShadow: "0 10px 30px rgba(0,0,0,0.3)",
                    textAlign: "center"
                }}>
                    <h3 style={{ fontWeight: "bold", color: "#0f172a", marginBottom: "2rem" }}>
                        Welcome to <span style={{ color: "#007bff" }}>MathClub</span>
                    </h3>

                    {alreadyLoggedIn ? (
                        <p style={{ color: "#d32f2f", fontWeight: "500" }}>
                            You are already logged in. Please log out first to access the login page.
                        </p>
                    ) : (
                        <form onSubmit={handleSubmit}>
                            <div className="form-floating mb-3">
                                <input
                                    type="text"
                                    className="form-control"
                                    id="username"
                                    placeholder="Enter Username"
                                    value={username}
                                    onChange={(e) => setUsername(e.target.value)}
                                    required
                                />
                                <label htmlFor="username">Username</label>
                            </div>

                            <div className="form-floating mb-4">
                                <input
                                    type="password"
                                    className="form-control"
                                    id="password"
                                    placeholder="Enter Password"
                                    value={password}
                                    onChange={(e) => setPassword(e.target.value)}
                                    required
                                />
                                <label htmlFor="password">Password</label>
                            </div>

                            <div className="d-grid">
                                <button type="submit" className="btn btn-primary" disabled={loading}>
                                    {loading ? "Logging in..." : "Login"}
                                </button>
                            </div>
                        </form>
                    )}
                </div>
            </div>
        </div>
    );
};

export default Login;
