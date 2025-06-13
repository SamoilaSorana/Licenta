import React, { useEffect, useState } from "react";
import Cookies from "js-cookie";
import axios from "axios";
import { useNavigate } from "react-router-dom";
import background from "../images/imagess.png";
//import getUrl from "./geturl";


const Lectures = () => {
    const [permissions, setPermissions] = useState([]);
    const [chaptersWithLectures, setChaptersWithLectures] = useState([]);
    const [loading, setLoading] = useState(true);
    const [expandedChapters, setExpandedChapters] = useState(new Set());
    const [progress, setProgress] = useState({ completed: 0, total: 0 });
    const [animatedPercent, setAnimatedPercent] = useState(0);
    const [selectedDifficulty, setSelectedDifficulty] = useState(null); // ⭐️ Nou
    const navigate = useNavigate();

    const hasPermission = (perm) => permissions.includes(perm);

    useEffect(() => {
        const token = Cookies.get("token");
        if (token) {
            axios.get(`http://localhost:4000/idm/api/auth/check-token`, {
                headers: { Authorization: `Bearer ${token}` },
            })
                .then((res) => {
                    const perms = res.data.permisiuni;
                    setPermissions(perms);
                    if (!perms.includes("VIEW_LECTURES")) {
                        navigate("/not-authorized");
                    }
                })
                .catch(() => {
                    Cookies.remove("token");
                    setPermissions([]);
                    navigate("/login");
                })
                .finally(() => setLoading(false));
        } else {
            setPermissions([]);
            setLoading(false);
            navigate("/login");
        }
    }, []);

    useEffect(() => {
        if (hasPermission("VIEW_LECTURES")) {
            const token = Cookies.get("token");
            const endpoint = hasPermission("SEE_UNFILTERED_LECTURES")
                ? `http://localhost:4000/logic/lecture/all_unfiltered`
                : `http://localhost:4000/logic/lecture/all`;

            axios.get(endpoint, {
                headers: { Authorization: `Bearer ${token}` },
            })
                .then((res) => {
                    setChaptersWithLectures(res.data);
                    const initialExpanded = new Set(res.data.map(item => item.chapter.id));
                    setExpandedChapters(initialExpanded);
                })
                .catch((err) => console.log("Eroare:", err));

            axios.get("http://localhost:4000/logic/lecture/count", {
                headers: { Authorization: `Bearer ${token}` },
            })
                .then((res) => setProgress(res.data))
                .catch((err) => console.error("Eroare progres:", err));
        }
    }, [permissions]);

    useEffect(() => {
        let i = 0;
        const interval = setInterval(() => {
            const target = progress.total > 0 ? (progress.completed / progress.total) * 100 : 0;
            setAnimatedPercent(prev => prev >= target ? target : Math.min(prev + 1, target));
            if (animatedPercent >= target) clearInterval(interval);
        }, 30);
        return () => clearInterval(interval);
    }, [progress]);

    const handleStartLecture = (id) => navigate(`/lecture/${id}`);

    const toggleChapter = (chapterId) => {
        setExpandedChapters(prev => {
            const newSet = new Set(prev);
            newSet.has(chapterId) ? newSet.delete(chapterId) : newSet.add(chapterId);
            return newSet;
        });
    };

    const getDifficultyLabel = (val) => {
        switch (val) {
            case 1: return "Ușor";
            case 2: return "Mediu";
            case 3: return "Greu";
            default: return "Toate";
        }
    };

    if (loading) return <h2>Se încarcă...</h2>;

    return (
        <div
            style={{
                backgroundImage: `url(${background})`,
                backgroundSize: "cover",
                backgroundRepeat: "no-repeat",
                backgroundPosition: "top center",
                minHeight: "100vh",
                paddingTop: "200px",
                paddingLeft: "40px",
                paddingRight: "40px",
                paddingBottom: "60px"
            }}
        >
            <div style={{
                backgroundColor: "#fff",
                borderRadius: "10px",
                padding: "20px",
                marginBottom: "40px",
                boxShadow: "0 4px 12px rgba(0,0,0,0.1)"
            }}>
                <h2 style={{ marginBottom: "10px" }}>Progress: {progress.completed}/{progress.total} ({Math.round(animatedPercent)}%)</h2>
                <div style={{
                    width: "100%",
                    height: "25px",
                    backgroundColor: "#eee",
                    borderRadius: "15px",
                    overflow: "hidden",
                    position: "relative",
                    border: "1px solid #ccc"
                }}>
                    <div style={{
                        width: `${animatedPercent}%`,
                        height: "100%",
                        backgroundImage: "linear-gradient(135deg, #64b5f6 25%, #1e88e5 25%, #1e88e5 50%, #64b5f6 50%, #64b5f6 75%, #1e88e5 75%, #1e88e5)",
                        backgroundSize: "40px 40px",
                        animation: "movePattern 2s linear infinite"
                    }} />
                </div>
            </div>

            <style>
                {`
                @keyframes movePattern {
                    0% { background-position: 0 0; }
                    100% { background-position: 40px 0; }
                }
                `}
            </style>

            {/* Butoane de filtrare după dificultate */}
            <div style={{ marginBottom: "30px", display: "flex", gap: "10px" }}>
                {[1, 2, 3].map((val) => (
                    <button
                        key={val}
                        onClick={() => setSelectedDifficulty(val)}
                        style={{
                            backgroundColor: selectedDifficulty === val ? "#1565c0" : "#90caf9",
                            color: selectedDifficulty === val ? "#fff" : "#000",
                            padding: "10px 20px",
                            borderRadius: "8px",
                            border: "none",
                            cursor: "pointer",
                            fontWeight: "bold",
                            fontSize: "16px",
                            transition: "all 0.3s"
                        }}
                    >
                        {getDifficultyLabel(val)}
                    </button>
                ))}
                <button
                    onClick={() => setSelectedDifficulty(null)}
                    style={{
                        backgroundColor: selectedDifficulty === null ? "#1565c0" : "#ccc",
                        color: selectedDifficulty === null ? "#fff" : "#000",
                        padding: "10px 20px",
                        borderRadius: "8px",
                        border: "none",
                        cursor: "pointer",
                        fontWeight: "bold",
                        fontSize: "16px",
                        transition: "all 0.3s"
                    }}
                >
                    Toate
                </button>
            </div>


            <h2 style={{
                fontSize: "24px",
                fontWeight: "bold",
                marginBottom: "20px",
                padding: "10px 20px",
                backgroundColor: "#e3f2fd",
                color: "#0d47a1",
                borderRadius: "8px",
                display: "inline-block",
                boxShadow: "0 2px 6px rgba(0,0,0,0.1)"
            }}>
                Dificultate selectată: {selectedDifficulty === 1
                ? "Ușor"
                : selectedDifficulty === 2
                    ? "Mediu"
                    : selectedDifficulty === 3
                        ? "Greu"
                        : "Toate"}
            </h2>

            {chaptersWithLectures.map(({chapter, lectures}) => {
                const isExpanded = expandedChapters.has(chapter.id);
                const filteredLectures = selectedDifficulty
                    ? lectures.filter(l => l.dificultate === selectedDifficulty)
                    : lectures;

                if (filteredLectures.length === 0) return null;

                return (
                    <div key={chapter.id} style={{marginBottom: "40px"}}>
                        <div
                            onClick={() => toggleChapter(chapter.id)}
                            style={{
                                backgroundColor: "rgba(255, 255, 255, 0.95)",
                                borderRadius: "10px",
                                padding: "15px 20px",
                                cursor: "pointer",
                                fontSize: "20px",
                                fontWeight: "bold",
                                boxShadow: "0 4px 8px rgba(0,0,0,0.1)",
                                marginBottom: "10px",
                                display: "flex",
                                justifyContent: "space-between",
                                alignItems: "center",
                                transition: "all 0.3s ease"
                            }}
                        >
                            <span>{`Chapter ${chapter.id} – ${chapter.name} – ${chapter.grade} – ${chapter.subject}`}</span>
                            <span style={{
                                fontSize: "24px",
                                transform: isExpanded ? "rotate(90deg)" : "rotate(0deg)",
                                transition: "transform 0.3s"
                            }}>
                                ▶
                            </span>
                        </div>

                        <div style={{
                            maxHeight: isExpanded ? "2000px" : "0",
                            overflow: "hidden",
                            transition: "max-height 0.5s ease-in-out"
                        }}>
                            <div style={{
                                display: "grid",
                                gridTemplateColumns: "repeat(auto-fill, minmax(300px, 1fr))",
                                gap: "25px"
                            }}>
                                {filteredLectures.map((lecture) => (
                                    <div key={lecture.lectureId} style={{
                                        background: "rgba(255, 255, 255, 0.85)",
                                        borderRadius: "16px",
                                        padding: "25px",
                                        boxShadow: "0 8px 15px rgba(0,0,0,0.1)",
                                        backdropFilter: "blur(4px)",
                                        transition: "transform 0.2s",
                                        display: "flex",
                                        flexDirection: "column",
                                        justifyContent: "space-between",
                                        minHeight: "320px"
                                    }}
                                         onMouseEnter={(e) => e.currentTarget.style.transform = "scale(1.03)"}
                                         onMouseLeave={(e) => e.currentTarget.style.transform = "scale(1.0)"}
                                    >
                                        <div>
                                            <h2 style={{fontSize: "22px", color: "#222"}}>{lecture.titlu}</h2>
                                            <p><strong>Dificultate:</strong> {getDifficultyLabel(lecture.dificultate)}
                                            </p>
                                            <p style={{marginTop: "10px", color: "#444", flexGrow: 1}}>
                                                {(lecture.rezumat && lecture.rezumat.replace(/<[^>]*>?/gm, '').slice(0, 100) + '...') ||
                                                    "Această lecție te va ajuta să înțelegi mai bine conceptele matematice."}
                                            </p>
                                        </div>

                                        <div style={{marginTop: "auto"}}>
                                            <button
                                                onClick={() => handleStartLecture(lecture.lectureId)}
                                                style={{
                                                    marginTop: "20px",
                                                    backgroundColor: "#1976d2",
                                                    color: "#fff",
                                                    border: "none",
                                                    padding: "10px 25px",
                                                    borderRadius: "8px",
                                                    fontSize: "16px",
                                                    cursor: "pointer",
                                                    transition: "background-color 0.3s"
                                                }}
                                                onMouseOver={(e) => e.target.style.backgroundColor = "#0d47a1"}
                                                onMouseOut={(e) => e.target.style.backgroundColor = "#1976d2"}
                                            >
                                                Începe lecția
                                            </button>
                                        </div>
                                    </div>
                                ))}
                            </div>
                        </div>
                    </div>
                );
            })}
        </div>
    );
};

export default Lectures;
