import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios";
import imagess from '../images/imagess.png';
import "./style.css";
import { useWindowSize } from "./useWindowSize";
import Cookies from "js-cookie";

const Register = () => {
    const [width, height] = useWindowSize();
    const navigate = useNavigate();

    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");
    const [email, setEmail] = useState("");
    const [nume, setNume] = useState("");
    const [prenume, setPrenume] = useState("");

    useEffect(() => {
        const token = Cookies.get("token");
        if (token) {
            axios.get("http://localhost:4000/idm/api/auth/check-token", {
                headers: { Authorization: `Bearer ${token}` }
            })
                .then(() => navigate("/"))
                .catch(() => {}); // token invalid -> rămâne pe pagină
        }
    }, [navigate]);

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            const response = await axios.post("http://localhost:4000/idm/user/register", {
                username,
                parola: password,
                email,
                nume,
                prenume
            });

            if (response.status === 201) {
                alert("Registration successful! You can now login.");
                navigate("/login");
            } else {
                alert("Registration failed. Please try again.");
            }
        } catch (error) {
            console.error("Registration error:", error);
            alert("An error occurred while registering.");
        }
    };

    return (
        <div style={{
            position: "relative",
            width: "100vw",
            height: "100vh",
            overflow: "hidden",
            backgroundColor: "#0f172a"
        }}>
            <img
                src={imagess}
                alt="background"
                style={{
                    width: "100%",
                    height: "100%",
                    objectFit: "cover",
                    position: "absolute",
                    top: 0,
                    left: 0,
                    zIndex: 0,
                    filter: "brightness(0.7)"
                }}
            />

            <div
                style={{
                    position: "relative",
                    zIndex: 2,
                    height: "110%",
                    width: "100%",
                    display: "flex",
                    justifyContent: "flex-end",
                    alignItems: "center",
                    paddingRight: "33%",
                    marginTop: "80px",
                }}
            >
                <div
                    style={{
                        backgroundColor: "rgba(255,255,255,0.95)",
                        padding: "2rem 2.5rem",
                        borderRadius: "20px",
                        width: "110%",
                        maxWidth: "450px",
                        maxHeight: "90vh",
                        overflowY: "auto",
                        boxShadow: "0 10px 30px rgba(0,0,0,0.3)"
                    }}
                >
                    <h3 style={{ textAlign: "center", marginBottom: "1.5rem", fontWeight: "bold", color: "#0f172a" }}>
                        Create Account
                    </h3>

                    <form onSubmit={handleSubmit}>
                        {[
                            { label: "Username", value: username, set: setUsername, type: "text" },
                            { label: "Password", value: password, set: setPassword, type: "password" },
                            { label: "Email", value: email, set: setEmail, type: "email" },
                            { label: "Nume", value: nume, set: setNume, type: "text" },
                            { label: "Prenume", value: prenume, set: setPrenume, type: "text" }
                        ].map((field, idx) => (
                            <div className="form-floating mb-3" key={idx}>
                                <input
                                    type={field.type}
                                    className="form-control"
                                    placeholder={field.label}
                                    value={field.value}
                                    onChange={(e) => field.set(e.target.value)}
                                    required
                                />
                                <label>{field.label}</label>
                            </div>
                        ))}

                        <div className="d-grid">
                            <button type="submit" className="btn btn-primary">
                                Register
                            </button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    );
};

export default Register;
