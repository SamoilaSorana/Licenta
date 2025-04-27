import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios";
import image from "../images/image.jpg";
import "./style.css";
import { useWindowSize } from "./useWindowSize";

const Register = () => {
    const [width, height] = useWindowSize();
    const navigate = useNavigate();

    // DEFINIM TOATE CÂMPURILE NECESARE
    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");
    const [email, setEmail] = useState("");
    const [nume, setNume] = useState("");
    const [prenume, setPrenume] = useState("");
    const [role, setRole] = useState("user"); // implicit: "user"


    const handleSubmit = async (e) => {
        e.preventDefault();

        try {
            const response = await axios.post("http://localhost:4000/idm/user/register", {
                username,
                parola: password,   // backend-ul tău așteaptă probabil "parola"
                email,
                nume,
                prenume,
                rolInput: role            // backend-ul așteaptă "rol", dar tu ai "role" în React
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
            backgroundImage: `url(${image})`,
            backgroundSize: "cover",
            height: height - 57,
            width: width,
            display: 'flex',
            justifyContent: 'center',
            alignItems: 'center'
        }}>
            <div className="form-content">
                <div className="form-items" style={{
                    backgroundColor: "rgb(205, 238, 255)",
                    padding: "20px",
                    borderRadius: "10px",
                    boxShadow: "0px 0px 10px rgba(0, 0, 0, 0.1)"
                }}>
                    <h3>Register</h3>
                    <form onSubmit={handleSubmit}>
                        <div className="form-floating col-md-12">
                            <input type="text" className="form-control" placeholder="Enter Username"
                                   value={username} onChange={(e) => setUsername(e.target.value)} required />
                            <label>Username</label>
                        </div>

                        <div className="form-floating col-md-12">
                            <input type="password" className="form-control" placeholder="Enter Password"
                                   value={password} onChange={(e) => setPassword(e.target.value)} required />
                            <label>Password</label>
                        </div>

                        <div className="form-floating col-md-12">
                            <input type="email" className="form-control" placeholder="Enter Email"
                                   value={email} onChange={(e) => setEmail(e.target.value)} required />
                            <label>Email</label>
                        </div>

                        <div className="form-floating col-md-12">
                            <input type="text" className="form-control" placeholder="Nume"
                                   value={nume} onChange={(e) => setNume(e.target.value)} required />
                            <label>Nume</label>
                        </div>

                        <div className="form-floating col-md-12">
                            <input type="text" className="form-control" placeholder="Prenume"
                                   value={prenume} onChange={(e) => setPrenume(e.target.value)} required />
                            <label>Prenume</label>
                        </div>

                        <div className="form-floating col-md-12">
                            <select className="form-control" value={role} onChange={(e) => setRole(e.target.value)}>
                                <option value="user">Elev</option>
                                <option value="profesor">Profesor</option>
                                <option value="admin">Admin</option>
                            </select>
                            <label>Rol</label>
                        </div>

                        <div className="form-button m-4" style={{ display: 'flex', justifyContent: 'center' }}>
                            <button type="submit" className="btn btn-primary" style={{ padding: '10px 30px' }}>
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
