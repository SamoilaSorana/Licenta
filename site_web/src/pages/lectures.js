import React, { useEffect, useState } from "react";
import Cookies from "js-cookie";
import axios from "axios";
import { useNavigate } from "react-router-dom";

const Lectures = () => {
    const [permisiuni, setPermisiuni] = useState([]);
    const [lectures, setLectures] = useState([]);
    const [loading, setLoading] = useState(true);
    const navigate = useNavigate();

    const hasPermisiuni = (permisiune) => {
        return permisiuni.includes(permisiune);
    };

    useEffect(() => {
        const token = Cookies.get("token");

        if (token) {
            axios.get("http://localhost:4000/idm/api/auth/check-token", {
                headers: {
                    Authorization: `Bearer ${token}`,
                },
            })
                .then((response) => {
                    console.log("✅ Token valid:", response.data);
                    setPermisiuni(response.data.permisiuni);
                })
                .catch((error) => {
                    console.log("❌ Token invalid:", error.response.data);
                    Cookies.remove("token");
                    setPermisiuni([]);
                })
                .finally(() => setLoading(false));
        } else {
            setPermisiuni([]);
            setLoading(false);
        }
    }, []);

    useEffect(() => {
        if (hasPermisiuni("VIEW_LECTURES")) {
            axios.get("http://localhost:4000/logic/lecture/all", {
                headers: {
                    Authorization: `Bearer ${Cookies.get("token")}`,
                },
            })
                .then((response) => {
                    setLectures(response.data);
                })
                .catch((error) => {
                    console.log("Eroare la preluarea lecțiilor:", error);
                });
        }
    }, [permisiuni]);

    if (loading) {
        return <h2>Se încarcă...</h2>;
    }

    if (!hasPermisiuni("VIEW_LECTURES")) {
        return <h2>Nu ai permisiunea să vezi lecțiile.</h2>;
    }

    const handleStartLecture = (lectureId) => {
        navigate(`/lecture/${lectureId}`);
    };

    // Stiluri directe
    const styles = {
        page: {
            padding: "20px",
            maxWidth: "1200px",
            margin: "0 auto",
        },
        container: {
            display: "grid",
            gridTemplateColumns: "repeat(auto-fill, minmax(300px, 1fr))",
            gap: "20px",
        },
        card: {
            backgroundColor: "#f9f9f9",
            border: "1px solid #ddd",
            padding: "20px",
            borderRadius: "12px",
            boxShadow: "0 2px 8px rgba(0, 0, 0, 0.1)",
            display: "flex",
            flexDirection: "column",
            justifyContent: "space-between",
        },
        button: {
            marginTop: "15px",
            padding: "10px 20px",
            border: "none",
            backgroundColor: "#4CAF50",
            color: "white",
            fontSize: "16px",
            borderRadius: "8px",
            cursor: "pointer",
            transition: "background-color 0.3s ease",
        },
        buttonHover: {
            backgroundColor: "#45a049",
        }
    };

    return (
        <div style={styles.page}>
            <h1>Lista Lecțiilor</h1>
            <div style={styles.container}>
                {lectures.map((lecture) => (
                    <div key={lecture.lectureId} style={styles.card}>
                        <h2>{lecture.titlu}</h2>
                        <p><strong>Dificultate:</strong> {lecture.dificultate}</p>
                        <p>{lecture.rezumat}</p>

                        <button
                            style={styles.button}
                            onClick={() => handleStartLecture(lecture.lectureId)}
                            onMouseOver={(e) => e.target.style.backgroundColor = styles.buttonHover.backgroundColor}
                            onMouseOut={(e) => e.target.style.backgroundColor = styles.button.backgroundColor}
                        >
                            Începe lecția
                        </button>
                    </div>
                ))}
            </div>
        </div>
    );
};

export default Lectures;
