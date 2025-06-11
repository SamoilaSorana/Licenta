import React, { useEffect, useState } from "react";
import axios from "axios";
import Cookies from "js-cookie";
import { useNavigate } from "react-router-dom";

const NotificationsPage = () => {
    const [notifications, setNotifications] = useState([]);
    const [selectedIds, setSelectedIds] = useState([]);
    const [permissions, setPermissions] = useState([]);
    const [loading, setLoading] = useState(true);
    const navigate = useNavigate();

    useEffect(() => {
        const token = Cookies.get("token");
        if (!token) {
            navigate("/login");
            return;
        }

        axios.get("http://localhost:4000/idm/api/auth/check-token", {
            headers: { Authorization: `Bearer ${token}` },
        })
            .then(res => {
                const perms = res.data.permisiuni || [];
                setPermissions(perms);

                if (!perms.includes("SEE_NOTIFICATIONS")) {
                    navigate("/not-authorized");
                    return;
                }

                axios.get("http://localhost:4000/logic/help", {
                    headers: { Authorization: `Bearer ${token}` },
                })
                    .then(res => setNotifications(res.data))
                    .catch(err => console.error("Failed to load notifications:", err));
            })
            .catch(() => {
                Cookies.remove("token");
                navigate("/login");
            })
            .finally(() => setLoading(false));
    }, [navigate]);

    const toggleSelection = (id) => {
        setSelectedIds(prev =>
            prev.includes(id) ? prev.filter(x => x !== id) : [...prev, id]
        );
    };

    const markAsRead = () => {
        const token = Cookies.get("token");
        if (!token || selectedIds.length === 0) return;

        axios.put("http://localhost:4000/logic/help", selectedIds, {
            headers: { Authorization: `Bearer ${token}` },
        })
            .then(() => {
                setNotifications(prev =>
                    prev.map(n =>
                        selectedIds.includes(n.id) ? { ...n, read: 1 } : n
                    )
                );
                setSelectedIds([]);
            })
            .catch(err => console.error("Failed to mark as read:", err));
    };

    if (loading) return <h2>Loading...</h2>;

    return (
        <div style={{ padding: "30px", maxWidth: "800px", margin: "0 auto" }}>
            <h1 style={{ textAlign: "center", marginBottom: "30px" }}>🔔 Notifications</h1>

            {notifications.map((notif) => {
                const isSelected = selectedIds.includes(notif.id);
                return (
                    <div
                        key={notif.id}
                        onClick={() => toggleSelection(notif.id)}
                        style={{
                            backgroundColor: notif.read
                                ? "#f0f0f0"
                                : isSelected
                                    ? "#d0e8ff"
                                    : "#ffffff",
                            padding: "20px",
                            marginBottom: "16px",
                            borderRadius: "12px",
                            boxShadow: "0 4px 12px rgba(0, 0, 0, 0.08)",
                            borderLeft: `6px solid ${notif.read ? "#bbb" : "#1976d2"}`,
                            cursor: "pointer",
                            transition: "all 0.2s ease-in-out"
                        }}
                    >
                        <div style={{ display: "flex", justifyContent: "space-between", alignItems: "center" }}>
                            <div style={{ flex: 1 }}>
                                <p><strong>ID:</strong> {notif.id}</p>
                                <p><strong>Student:</strong> {notif.studentname}</p>
                                <p><strong>Date:</strong> {new Date(notif.date).toLocaleString()}</p>
                                <p><strong>Status:</strong> {notif.read ? "Read" : "Unread"}</p>
                            </div>
                            {!notif.read && (
                                <input
                                    type="checkbox"
                                    checked={isSelected}
                                    onClick={(e) => {
                                        e.stopPropagation();
                                        toggleSelection(notif.id);
                                    }}
                                    onChange={() => {}}
                                    style={{ marginLeft: "16px", transform: "scale(1.3)" }}
                                />
                            )}
                        </div>

                        <div style={{ textAlign: "right", marginTop: "10px" }}>
                            <button
                                onClick={(e) => {
                                    e.stopPropagation();
                                    navigate(`/graded-tests/${notif.userId}`);
                                }}
                                style={{
                                    backgroundColor: "#1976d2",
                                    color: "#fff",
                                    border: "none",
                                    padding: "8px 16px",
                                    borderRadius: "6px",
                                    fontSize: "14px",
                                    cursor: "pointer",
                                    transition: "background-color 0.3s"
                                }}
                                onMouseOver={(e) => e.target.style.backgroundColor = "#0d47a1"}
                                onMouseOut={(e) => e.target.style.backgroundColor = "#1976d2"}
                            >
                                View graded tests
                            </button>
                        </div>
                    </div>
                );
            })}

            {selectedIds.length > 0 && (
                <div style={{ textAlign: "center", marginTop: "30px" }}>
                    <button
                        onClick={markAsRead}
                        style={{
                            backgroundColor: "#43a047",
                            color: "white",
                            border: "none",
                            padding: "12px 28px",
                            borderRadius: "8px",
                            fontSize: "16px",
                            cursor: "pointer"
                        }}
                    >
                        ✅ Mark selected as read
                    </button>
                </div>
            )}
        </div>
    );
};

export default NotificationsPage;
