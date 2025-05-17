import React, { useEffect, useState } from "react";
import Cookies from "js-cookie";
import axios from "axios";
import { useNavigate } from "react-router-dom";
import background from "../images/imagess.png";

const Lectures = () => {
    const [permissions, setPermissions] = useState([]);
    const [chaptersWithLectures, setChaptersWithLectures] = useState([]);
    const [loading, setLoading] = useState(true);
    const [expandedChapters, setExpandedChapters] = useState(new Set());
    const [progress, setProgress] = useState({ completed: 0, total: 0 });
    const [animatedPercent, setAnimatedPercent] = useState(0);
    const navigate = useNavigate();

    const hasPermission = (perm) => permissions.includes(perm);

    useEffect(() => {
        const token = Cookies.get("token");
        if (token) {
            axios.get("http://localhost:4000/idm/api/auth/check-token", {
                headers: { Authorization: `Bearer ${token}` },
            })
                .then((res) => setPermissions(res.data.permisiuni))
                .catch(() => {
                    Cookies.remove("token");
                    setPermissions([]);
                })
                .finally(() => setLoading(false));
        } else {
            setPermissions([]);
            setLoading(false);
        }
    }, []);

    useEffect(() => {
        if (hasPermission("VIEW_LECTURES")) {
            axios.get("http://localhost:4000/logic/lecture/all", {
                headers: { Authorization: `Bearer ${Cookies.get("token")}` },
            })
                .then((res) => {
                    setChaptersWithLectures(res.data);
                    const initialExpanded = new Set(res.data.map(item => item.chapter.id));
                    setExpandedChapters(initialExpanded);
                })
                .catch((err) => console.log("Eroare:", err));

            axios.get("http://localhost:4000/logic/lecture/count", {
                headers: { Authorization: `Bearer ${Cookies.get("token")}` },
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

    if (loading) return <h2>Se încarcă...</h2>;
    if (!hasPermission("VIEW_LECTURES")) return <h2>Nu ai permisiunea să vezi lecțiile.</h2>;

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

            {chaptersWithLectures.map(({ chapter, lectures }) => {
                const isExpanded = expandedChapters.has(chapter.id);
                return (
                    <div key={chapter.id} style={{ marginBottom: "40px" }}>
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
                            <span style={{ fontSize: "24px", transform: isExpanded ? "rotate(90deg)" : "rotate(0deg)", transition: "transform 0.3s" }}>
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
                                {lectures.map((lecture) => (
                                    <div key={lecture.lectureId} style={{
                                        background: "rgba(255, 255, 255, 0.85)",
                                        borderRadius: "16px",
                                        padding: "25px",
                                        boxShadow: "0 8px 15px rgba(0,0,0,0.1)",
                                        backdropFilter: "blur(4px)",
                                        transition: "transform 0.2s",
                                    }}
                                         onMouseEnter={(e) => e.currentTarget.style.transform = "scale(1.03)"}
                                         onMouseLeave={(e) => e.currentTarget.style.transform = "scale(1.0)"}
                                    >
                                        <h2 style={{ fontSize: "22px", color: "#222" }}>{lecture.titlu}</h2>
                                        <p><strong>Dificultate:</strong> {lecture.dificultate}</p>
                                        <p style={{ marginTop: "10px", color: "#444" }}>
                                            {lecture.rezumat || "Această lecție te va ajuta să înțelegi mai bine conceptele matematice."}
                                        </p>
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