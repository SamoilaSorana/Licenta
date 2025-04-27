import React, { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import Cookies from "js-cookie";
import axios from "axios";

const LecturePage = () => {
    const { id } = useParams(); // lectureId din URL
    const [lecture, setLecture] = useState(null);
    const [loading, setLoading] = useState(true);
    const navigate = useNavigate();

    useEffect(() => {
        const token = Cookies.get("token");

        if (token) {
            axios.get(`http://localhost:4000/test/lecture/${id}`, {
                headers: {
                    Authorization: `Bearer ${token}`,
                },
            })
                .then((response) => {
                    setLecture(response.data);
                })
                .catch((error) => {
                    console.log("Eroare la preluarea lecției:", error);
                    navigate("/lectures"); // Dacă dă eroare, întoarce utilizatorul la lista de lecții
                })
                .finally(() => setLoading(false));
        } else {
            navigate("/"); // Dacă nu există token, du-l acasă
        }
    }, [id, navigate]);

    if (loading) {
        return <h2>Se încarcă lecția...</h2>;
    }

    if (!lecture) {
        return <h2>Lecția nu a fost găsită.</h2>;
    }

    const styles = {
        page: {
            padding: "20px",
            maxWidth: "800px",
            margin: "0 auto",
            backgroundColor: "#f9f9f9",
            border: "1px solid #ddd",
            borderRadius: "12px",
            boxShadow: "0 2px 8px rgba(0, 0, 0, 0.1)",
        },
        title: {
            fontSize: "28px",
            marginBottom: "10px",
        },
        subtitle: {
            fontSize: "20px",
            marginBottom: "10px",
            color: "#555",
        },
        content: {
            fontSize: "18px",
            lineHeight: "1.6",
        },
        button: {
            marginTop: "20px",
            padding: "10px 20px",
            backgroundColor: "#4CAF50",
            color: "white",
            border: "none",
            borderRadius: "8px",
            fontSize: "16px",
            cursor: "pointer",
            transition: "background-color 0.3s ease",
            marginRight: "10px",
        },
    };

    const handleStartTest = () => {
        navigate(`/lecture/${id}/test`);
    };

    return (
        <div style={styles.page}>
            <h1 style={styles.title}>{lecture.titlu}</h1>
            <h3 style={styles.subtitle}>Dificultate: {lecture.dificultate}</h3>
            <h3 style={styles.subtitle}>Rezumat:</h3>
            <p style={styles.content}>{lecture.rezumat}</p>
            <h3 style={styles.subtitle}>Conținut detaliat:</h3>
            <p style={styles.content}>{lecture.continut}</p>

            {/* Butoane */}
            <div>
                <button
                    style={styles.button}
                    onClick={() => navigate("/lectures")}
                    onMouseOver={(e) => e.target.style.backgroundColor = "#45a049"}
                    onMouseOut={(e) => e.target.style.backgroundColor = "#4CAF50"}
                >
                    Înapoi la Lecții
                </button>

                <button
                    style={styles.button}
                    onClick={handleStartTest}
                    onMouseOver={(e) => e.target.style.backgroundColor = "#45a049"}
                    onMouseOut={(e) => e.target.style.backgroundColor = "#4CAF50"}
                >
                    Începe Testul
                </button>
            </div>
        </div>
    );
};

export default LecturePage;