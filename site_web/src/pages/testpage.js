import React, { useEffect, useState } from "react";
import Cookies from "js-cookie";
import axios from "axios";
import { useNavigate, useParams } from "react-router-dom";

const TestPage = () => {
    const [permissions, setPermissions] = useState([]);
    const [questions, setQuestions] = useState([]);
    const [selectedAnswers, setSelectedAnswers] = useState({});
    const [submittedQuestions, setSubmittedQuestions] = useState(new Set());
    const [currentQuestionIndex, setCurrentQuestionIndex] = useState(0);
    const [loading, setLoading] = useState(true);
    const navigate = useNavigate();
    const { id } = useParams();

    const hasPermission = (permission) => permissions.includes(permission);

    useEffect(() => {
        const token = Cookies.get("token");
        if (!token) return navigate("/login");

        axios.get("http://localhost:4000/idm/api/auth/check-token", {
            headers: { Authorization: `Bearer ${token}` },
        })
            .then(res => {
                setPermissions(res.data.permisiuni);
                if (!res.data.permisiuni.includes("TAKE_TEST")) {
                    navigate("/not-authorized");
                    return;
                }
            })
            .catch(() => {
                Cookies.remove("token");
                navigate("/login");
            })
            .finally(() => setLoading(false));
    }, [navigate]);

    useEffect(() => {
        if (hasPermission("TAKE_TEST")) {
            axios.get(`http://localhost:4000/logic/lecture/${id}/test`, {
                headers: { Authorization: `Bearer ${Cookies.get("token")}` },
            })
                .then(res => {
                    const data = res.data;
                    const shuffled = [...data];
                    for (let i = shuffled.length - 1; i > 0; i--) {
                        const j = Math.floor(Math.random() * (i + 1));
                        [shuffled[i], shuffled[j]] = [shuffled[j], shuffled[i]];
                    }
                    shuffled.forEach(q => {
                        for (let i = q.answers.length - 1; i > 0; i--) {
                            const j = Math.floor(Math.random() * (i + 1));
                            [q.answers[i], q.answers[j]] = [q.answers[j], q.answers[i]];
                        }
                    });
                    setQuestions(shuffled);
                })
                .catch(err => console.error("Error fetching questions:", err));
        }
    }, [permissions, id]);

    const handleChange = (questionId, answerId, isChecked) => {
        if (submittedQuestions.has(questionId)) return;
        setSelectedAnswers(prev => {
            const updated = { ...prev };
            if (!updated[questionId]) updated[questionId] = [];
            if (isChecked) {
                if (!updated[questionId].includes(answerId)) {
                    updated[questionId].push(answerId);
                }
            } else {
                updated[questionId] = updated[questionId].filter(id => id !== answerId);
            }
            return updated;
        });
    };

    const handleSubmit = () => {
        const currentQuestion = questions[currentQuestionIndex];
        setSubmittedQuestions(prev => new Set(prev).add(currentQuestion.id));
    };

    const finalizeTest = () => {
        const unanswered = questions.filter(q => !submittedQuestions.has(q.id));
        if (unanswered.length > 0) {
            alert("You have unanswered questions. Please submit all answers before finalizing.");
        } else {
            const payload = Object.entries(selectedAnswers).map(([questionId, answerIds]) => ({
                questionId: parseInt(questionId),
                answerIds
            }));

            axios.post(`http://localhost:4000/logic/lecture/${id}/test/submit`, payload, {
                headers: { Authorization: `Bearer ${Cookies.get("token")}` },
            })
                .then((res) => {
                    const grade = res.data;
                    if (grade >= 6) {
                        alert(`🎉 Congratulations! You passed with a grade of ${grade.toFixed(2)}.`);
                    } else {
                        alert(`❌ You failed with a grade of ${grade.toFixed(2)}. Please try again.`);
                    }
                    navigate("/lectures");
                })
                .catch(() => alert("Error submitting test."));
        }
    };

    if (loading) return <h2 style={{ padding: "20px" }}>Checking permissions...</h2>;

    const currentQuestion = questions[currentQuestionIndex];

    return (
        <div style={{
            display: "flex",
            padding: "30px",
            fontFamily: "'Segoe UI', sans-serif",
            backgroundColor: "#f8fafc",
            minHeight: "100vh"
        }}>
            {/* Sidebar */}
            <div style={{
                width: "160px",
                marginRight: "40px",
                backgroundColor: "#ffffff",
                border: "1px solid #d1d5db",
                borderRadius: "12px",
                padding: "20px",
                boxShadow: "0 4px 12px rgba(0,0,0,0.05)"
            }}>
                <h4 style={{ marginBottom: "10px", color: "#0f172a" }}>Questions</h4>
                <div style={{ display: "flex", flexWrap: "wrap", gap: "10px" }}>
                    {questions.map((q, index) => (
                        <button
                            key={index}
                            onClick={() => setCurrentQuestionIndex(index)}
                            style={{
                                width: "40px",
                                height: "40px",
                                backgroundColor: submittedQuestions.has(q.id)
                                    ? "#16a34a"
                                    : currentQuestionIndex === index ? "#2563eb" : "#e5e7eb",
                                color: "#fff",
                                fontWeight: "bold",
                                border: "none",
                                borderRadius: "8px",
                                cursor: "pointer",
                                transition: "background-color 0.3s"
                            }}
                        >
                            {index + 1}
                        </button>
                    ))}
                </div>
            </div>

            {/* Main content */}
            <div style={{ flex: 1 }}>
                {currentQuestion && (
                    <div style={{
                        backgroundColor: "#ffffff",
                        padding: "30px",
                        borderRadius: "16px",
                        boxShadow: "0 4px 12px rgba(0,0,0,0.05)",
                        marginBottom: "30px"
                    }}>
                        <h2 style={{ marginBottom: "20px", color: "#1e293b" }}>
                            Question {currentQuestionIndex + 1}
                        </h2>
                        <p style={{ fontSize: "18px", marginBottom: "20px" }}>{currentQuestion.text}</p>
                        {currentQuestion.answers.map((entry, i) => (
                            <div key={entry.answer.answerId || i} style={{ marginBottom: "12px" }}>
                                <label style={{ fontSize: "16px" }}>
                                    <input
                                        type="checkbox"
                                        value={entry.answer.answerId}
                                        checked={selectedAnswers[currentQuestion.id]?.includes(entry.answer.answerId) || false}
                                        onChange={(e) => handleChange(currentQuestion.id, entry.answer.answerId, e.target.checked)}
                                        disabled={submittedQuestions.has(currentQuestion.id)}
                                        style={{ marginRight: "10px" }}
                                    />
                                    {entry.answer.text}
                                </label>
                            </div>
                        ))}

                        <button
                            onClick={handleSubmit}
                            disabled={submittedQuestions.has(currentQuestion.id)}
                            style={{
                                marginTop: "20px",
                                backgroundColor: submittedQuestions.has(currentQuestion.id) ? "#9ca3af" : "#3b82f6",
                                color: "#fff",
                                padding: "10px 24px",
                                border: "none",
                                borderRadius: "8px",
                                cursor: submittedQuestions.has(currentQuestion.id) ? "not-allowed" : "pointer",
                                fontSize: "16px",
                                transition: "background-color 0.3s"
                            }}
                        >
                            Submit Answer
                        </button>
                    </div>
                )}
            </div>

            {/* Finalize Button */}
            <div style={{
                position: "fixed",
                bottom: "30px",
                left: "30px",
            }}>
                <button
                    onClick={finalizeTest}
                    style={{
                        backgroundColor: "#dc2626",
                        color: "#fff",
                        border: "none",
                        padding: "12px 28px",
                        borderRadius: "10px",
                        fontSize: "16px",
                        cursor: "pointer",
                        boxShadow: "0 4px 10px rgba(0,0,0,0.1)",
                        transition: "background-color 0.3s"
                    }}
                >
                    Finalize Test
                </button>
            </div>
        </div>
    );
};

export default TestPage;
