import React, { useEffect, useState } from "react";
import axios from "axios";
import Cookies from "js-cookie";
import { useParams, useNavigate } from "react-router-dom";

const GradedTestsPage = () => {
    const [attempts, setAttempts] = useState([]);
    const [expanded, setExpanded] = useState(null);
    const [lectureTitles, setLectureTitles] = useState({});
    const [permissions, setPermissions] = useState([]);
    const navigate = useNavigate();
    const { userId } = useParams();

    useEffect(() => {
        const token = Cookies.get("token");
        if (!token) return;

        axios.get("http://localhost:4000/idm/api/auth/check-token", {
            headers: { Authorization: `Bearer ${token}` },
        })
            .then((res) => {
                setPermissions(res.data.permisiuni);

                if (userId && !res.data.permisiuni.includes("SEE_OTHER_TESTS")) {
                    navigate("/not-allowed");
                    return;
                }

                const url = userId
                    ? `http://localhost:4000/logic/attempts/all/${userId}`
                    : `http://localhost:4000/logic/attempts/all`;

                axios.get(url, {
                    headers: { Authorization: `Bearer ${token}` },
                })
                    .then(res => setAttempts(res.data))
                    .catch(err => console.error("Error loading attempts:", err));

                axios.get("http://localhost:4000/logic/lecture/all_unfiltered", {
                    headers: { Authorization: `Bearer ${token}` },
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
            })
            .catch(() => {
                Cookies.remove("token");
                navigate("/login");
            });
    }, [userId, navigate]);

    const getQuestionColor = (answers) => {
        const correct = answers.filter(a => a.correct);
        const chosenCorrect = correct.filter(a => a.chosenByUser);
        const chosenIncorrect = answers.filter(a => a.chosenByUser && !a.correct);

        if (chosenCorrect.length === correct.length && chosenIncorrect.length === 0) return "#c8e6c9";
        if (chosenCorrect.length > 0 && (chosenCorrect.length < correct.length || chosenIncorrect.length > 0)) return "#ffe0b2";
        return "#ffcdd2"; // red
    };

    const getAnswerColor = (answer) => {
        if (answer.correct && answer.chosenByUser) return "#a5d6a7";
        if (answer.correct && !answer.chosenByUser) return "#ffe082";
        if (!answer.correct && answer.chosenByUser) return "#ef9a9a";
        return "#f5f5f5";
    };

    const groupedAttempts = attempts.reduce((acc, attempt) => {
        const key = attempt.lectureId;
        if (!acc[key]) acc[key] = [];
        acc[key].push(attempt);
        return acc;
    }, {});

    const getGradeClass = (grade) => {
        if (grade >= 9) return "grade-excellent";
        if (grade >= 7) return "grade-good";
        if (grade > 0) return "grade-medium";
        return "grade-bad";
    };

    return (
        <div className="graded-wrapper blue-background">
            <h1 className="graded-title">📚 Graded Attempts</h1>
            {Object.entries(groupedAttempts).map(([lectureId, lectureAttempts]) => (
                <div key={lectureId} className="graded-lecture-box">
                    <h2 className="graded-lecture-title">
                        Lecția {lectureId} – {lectureTitles[lectureId] || "Lectură necunoscută"}
                    </h2>
                    {lectureAttempts.map((attempt, idx) => (
                        <div
                            key={attempt.attemptId}
                            className={`graded-attempt-card`}
                        >
                            <div
                                className="graded-attempt-header"
                                onClick={() =>
                                    setExpanded(expanded === attempt.attemptId ? null : attempt.attemptId)
                                }
                            >
                                <span><strong>{idx + 1}. Attempt ID:</strong> {attempt.attemptId}</span>
                                <span className={`grade-tag ${getGradeClass(attempt.grade)}`}>
                                    Grade: {attempt.grade}
                                </span>
                            </div>

                            {expanded === attempt.attemptId && (
                                <div className="graded-questions-box">
                                    {attempt.questions.map((question) => (
                                        <div
                                            key={question.questionId}
                                            className="graded-question"
                                            style={{ backgroundColor: getQuestionColor(question.answers) }}
                                        >
                                            <p><strong>{question.text}</strong></p>
                                            <ul className="graded-answer-list">
                                                {question.answers.map((a, i) => (
                                                    <li
                                                        key={i}
                                                        style={{ backgroundColor: getAnswerColor(a) }}
                                                        className="graded-answer"
                                                    >
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
