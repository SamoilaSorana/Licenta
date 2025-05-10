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
                if (!res.data.permisiuni.includes("TAKE_TEST")) navigate("/not-allowed");
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
                .then(res => setQuestions(res.data))
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
                        alert(`❌ You failed the test with a grade of ${grade.toFixed(2)}. Please try again.`);
                    }

                    window.location.href = "/lectures"; // redirecționează după închidere alert
                })
                .catch(() => {
                    alert("Error submitting test.");
                });
        }
    };

    if (loading) return <h2>Checking permissions...</h2>;

    const currentQuestion = questions[currentQuestionIndex];

    return (
        <div style={{ display: "flex", padding: 20 }}>
            {/* Sidebar container */}
            <div style={{
                width: "150px",
                marginRight: "30px",
                border: "2px solid #ccc",
                borderRadius: "8px",
                padding: "15px",
                display: "flex",
                flexDirection: "column",
                alignItems: "center",
                gap: "10px"
            }}>
                {questions.map((q, index) => (
                    <button
                        key={index}
                        style={{
                            padding: "10px",
                            backgroundColor: submittedQuestions.has(q.id)
                                ? "#4caf50"
                                : currentQuestionIndex === index ? "#1976d2" : "#eeeeee",
                            color: submittedQuestions.has(q.id) || currentQuestionIndex === index ? "#fff" : "#333",
                            border: "2px solid #999",
                            borderRadius: "6px",
                            cursor: "pointer",
                            width: "40px"
                        }}
                        onClick={() => setCurrentQuestionIndex(index)}
                    >
                        {index + 1}
                    </button>
                ))}
            </div>

            {/* Main question area */}
            <div style={{ flex: 1 }}>
                {currentQuestion && (
                    <div key={currentQuestion.id} style={{ marginBottom: 30 }}>
                        <h3>{`Question ${currentQuestionIndex + 1}: ${currentQuestion.text}`}</h3>
                        {currentQuestion.answers.map((entry, i) => (
                            <div key={entry.answer.answerId || i}>
                                <label>
                                    <input
                                        type="checkbox"
                                        value={entry.answer.answerId}
                                        checked={selectedAnswers[currentQuestion.id]?.includes(entry.answer.answerId) || false}
                                        onChange={(e) => handleChange(currentQuestion.id, entry.answer.answerId, e.target.checked)}
                                        disabled={submittedQuestions.has(currentQuestion.id)}
                                    /> {entry.answer.text}
                                </label>
                            </div>
                        ))}
                    </div>
                )}

                <button
                    onClick={handleSubmit}
                    disabled={submittedQuestions.has(currentQuestion?.id)}
                    style={{
                        backgroundColor: submittedQuestions.has(currentQuestion?.id) ? "#aaa" : "#1976d2",
                        color: "#fff",
                        border: "none",
                        padding: "10px 25px",
                        borderRadius: "8px",
                        fontSize: "16px",
                        cursor: submittedQuestions.has(currentQuestion?.id) ? "not-allowed" : "pointer"
                    }}
                >
                    Submit answer
                </button>
            </div>

            {/* Finalize button outside question box */}
            <div style={{ position: "fixed", bottom: 30, left: 30 }}>
                <button
                    onClick={finalizeTest}
                    style={{
                        backgroundColor: "#d32f2f",
                        color: "#fff",
                        border: "none",
                        padding: "12px 24px",
                        borderRadius: "8px",
                        fontSize: "16px",
                        cursor: "pointer"
                    }}
                >
                    Finalizează testul
                </button>
            </div>
        </div>
    );
};

export default TestPage;
