import React from "react";
import { useNavigate } from "react-router-dom";
import { ShieldLock } from "react-bootstrap-icons";

const NotAuthorized = () => {
    const navigate = useNavigate();

    return (
        <div style={{
            height: "100vh",
            display: "flex",
            justifyContent: "center",
            alignItems: "center",
            backgroundColor: "#f8fafc",
            padding: "20px"
        }}>
            <div style={{
                background: "white",
                padding: "40px",
                borderRadius: "16px",
                boxShadow: "0 10px 25px rgba(0,0,0,0.1)",
                textAlign: "center",
                maxWidth: "500px",
                width: "100%"
            }}>
                <ShieldLock size={64} color="#d32f2f" />
                <h2 style={{ marginTop: "20px", color: "#d32f2f", fontWeight: "bold" }}>
                    Access Denied
                </h2>
                <p style={{ marginTop: "10px", color: "#555", fontSize: "18px" }}>
                    You are not authorized to access this page.
                </p>
                <button
                    onClick={() => navigate("/")}
                    style={{
                        marginTop: "20px",
                        padding: "10px 20px",
                        backgroundColor: "#1976d2",
                        color: "#fff",
                        border: "none",
                        borderRadius: "8px",
                        fontSize: "16px",
                        cursor: "pointer",
                        transition: "background-color 0.3s"
                    }}
                    onMouseOver={(e) => e.target.style.backgroundColor = "#0d47a1"}
                    onMouseOut={(e) => e.target.style.backgroundColor = "#1976d2"}
                >
                    Go Back Home
                </button>
            </div>
        </div>
    );
};

export default NotAuthorized;
