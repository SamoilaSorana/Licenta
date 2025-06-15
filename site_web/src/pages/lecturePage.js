import React, { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import Cookies from "js-cookie";
import axios from "axios";
import './style.css';

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
                    navigate("/lectures");
                })
                .finally(() => setLoading(false));
        } else {
            navigate("/");
        }
    }, [id, navigate]);

    const formatContent = (text) => {
        if (!text) return "";


        if (!text.includes("<") && !text.includes("</")) {
            return text
                .split("\n")
                .map(line => `<p>${line.trim()}</p>`)
                .join("");
        }


        return text;
    };

    if (loading) {
        return <h2 style={{ textAlign: "center" }}>Se încarcă lecția...</h2>;
    }

    if (!lecture) {
        return <h2 style={{ textAlign: "center", color: "red" }}>Lecția nu a fost găsită.</h2>;
    }

    const styles = {
        button: {
            marginTop: "20px",
            padding: "12px 24px",
            backgroundColor: "#1e3799",
            color: "white",
            border: "none",
            borderRadius: "10px",
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
        <div className="lecture-wrapper">
            <div className="lecture-card">
                <h1 className="lecture-title">{lecture.titlu}</h1>

                <div className="lecture-label">Dificultate:</div>
                <p className="lecture-text">{lecture.dificultate}</p>

                <h3 style={styles.subtitle}>Rezumat:</h3>
                <div style={styles.content} dangerouslySetInnerHTML={{ __html: lecture.rezumat }}></div>


                <div className="lecture-label">Conținut detaliat:</div>
                <div
                    className="lecture-content-html"
                    dangerouslySetInnerHTML={{ __html: formatContent(lecture.continut) }}
                ></div>

                <div className="lecture-buttons">
                    <button
                        className="lecture-button"
                        style={styles.button}
                        onClick={() => navigate("/lectures")}
                        onMouseOver={(e) => e.target.style.backgroundColor = "#0c2461"}
                        onMouseOut={(e) => e.target.style.backgroundColor = "#1e3799"}
                    >
                        Înapoi la Lecții
                    </button>

                    <button
                        className="lecture-button"
                        style={styles.button}
                        onClick={handleStartTest}
                        onMouseOver={(e) => e.target.style.backgroundColor = "#0c2461"}
                        onMouseOut={(e) => e.target.style.backgroundColor = "#1e3799"}
                    >
                        Începe Testul
                    </button>
                </div>
            </div>
        </div>
    );
};

export default LecturePage;
