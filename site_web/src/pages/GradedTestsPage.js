import React, { useEffect, useState } from "react";
import axios from "axios";
import Cookies from "js-cookie";

const GradedTestsPage = () => {
    const [attempts, setAttempts] = useState([]);
    const [expanded, setExpanded] = useState(null);
    const [lectureTitles, setLectureTitles] = useState({});

    useEffect(() => {
        const token = Cookies.get("token");
        if (!token) return;

        axios.get("http://localhost:4000/logic/attempts/all", {
            headers: {
                Authorization: `Bearer ${token}`
            }
        })
            .then(res => setAttempts(res.data))
            .catch(err => console.error("Error loading attempts:", err));

        axios.get("http://localhost:4000/logic/lecture/all_unfiltered", {
            headers: {
                Authorization: `Bearer ${token}`
            }
        })
            .then(res => {
                const titles = {};
                res.data.forEach(group => {
                    group.lectures.forEach(lecture => {
                        titles[lecture.lectureId] = lecture.titlu;
                    });
                });
                setLectureTitles(titles);
            })
            .catch(err => console.error("Error loading lecture titles:", err));
    }, []);

    const getQuestionColor = (answers) => {
        const correct = answers.filter(a => a.correct);
        const chosenCorrect = correct.filter(a => a.chosenByUser);
        const chosenIncorrect = answers.filter(a => a.chosenByUser && !a.correct);

        if (chosenCorrect.length === correct.length && chosenIncorrect.length === 0) return "#c8e6c9"; // green
        if (chosenCorrect.length > 0 && (chosenCorrect.length < correct.length || chosenIncorrect.length > 0)) return "#ffe0b2"; // orange
        return "#ffcdd2"; // red
    };

    const getAnswerColor = (answer) => {
        if (answer.correct && answer.chosenByUser) return "#a5d6a7"; // green
        if (answer.correct && !answer.chosenByUser) return "#ffe082"; // orange
        if (!answer.correct && answer.chosenByUser) return "#ef9a9a"; // red
        return "#f5f5f5"; // default
    };

    // organize attempts by lectureId
    const groupedAttempts = attempts.reduce((acc, attempt) => {
        const key = attempt.lectureId;
        if (!acc[key]) acc[key] = [];
        acc[key].push(attempt);
        return acc;
    }, {});

    return (
        <div style={{ padding: 30 }}>
            <h1>Graded Attempts</h1>
            {Object.entries(groupedAttempts).map(([lectureId, lectureAttempts]) => (
                <div key={lectureId} style={{ marginBottom: 24 }}>
                    <h2>{lectureTitles[lectureId]} (Lecture ID: {lectureId})</h2>
                    {lectureAttempts.map((attempt, idx) => (
                        <div key={attempt.attemptId} style={{
                            border: "1px solid #ccc",
                            borderRadius: 8,
                            padding: 16,
                            marginBottom: 12,
                            backgroundColor: "#f9f9f9"
                        }}>
                            <div
                                style={{ cursor: "pointer", display: "flex", justifyContent: "space-between" }}
                                onClick={() => setExpanded(expanded === attempt.attemptId ? null : attempt.attemptId)}
                            >
                                <span><strong>{idx + 1}. Attempt ID:</strong> {attempt.attemptId}</span>
                                <span><strong>Grade:</strong> {attempt.grade}</span>
                            </div>

                            {expanded === attempt.attemptId && (
                                <div style={{ marginTop: 16 }}>
                                    {attempt.questions.map((question) => (
                                        <div key={question.questionId} style={{
                                            backgroundColor: getQuestionColor(question.answers),
                                            borderRadius: 6,
                                            padding: 12,
                                            marginBottom: 10
                                        }}>
                                            <p><strong>{question.text}</strong></p>
                                            <ul style={{ listStyle: "none", padding: 0 }}>
                                                {question.answers.map((a, i) => (
                                                    <li key={i} style={{
                                                        backgroundColor: getAnswerColor(a),
                                                        margin: "4px 0",
                                                        padding: "6px 10px",
                                                        borderRadius: 4
                                                    }}>
                                                        {a.text}
                                                    </li>
                                                ))}
                                            </ul>
                                        </div>
                                    ))}
                                </div>
                            )}
                        </div>
                    ))}
                </div>
            ))}
        </div>
    );
};

export default GradedTestsPage;
