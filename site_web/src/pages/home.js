import React, { useEffect, useState } from "react";
import Cookies from "js-cookie";
import axios from "axios";
import despreImg from "../images/Despre.jpg";
import "./style.css";

const Home = () => {
    const [permisiuni, setPermisiuni] = useState([]);

    useEffect(() => {
        const token = Cookies.get("token");
        if (token) {
            axios
                .get("http://localhost:4000/idm/api/auth/check-token", {
                    headers: {
                        Authorization: `Bearer ${token}`,
                    },
                })
                .then((response) => {
                    setPermisiuni(response.data.permisiuni);
                })
                .catch((error) => {
                    Cookies.remove("token");
                    setPermisiuni([]);
                });
        } else {
            setPermisiuni([]);
        }
    }, []);

    return (
        <div className="home-container">
            {permisiuni.length > 0 ? (
                <>
                    {/* Secțiune hero cu fundal imagine */}
                    <section className="hero-section-with-bg">
                        <div className="overlay">
                            <h1 className="hero-title">Misiunea <span>MathClub</span></h1>
                            <p className="hero-text">
                                <strong>MathClub</strong> își propune să facă învățarea matematicii
                                <br />
                                <em>clară, interesantă și eficientă</em> pentru toți elevii.
                                Prin explicații accesibile, exemple reale și teste inteligente,
                                <br />
                                îi ajutăm pe elevi să înțeleagă mai rapid, să-și dezvolte abilitățile și să capete încredere.
                            </p>
                        </div>
                    </section>

                    {/* Secțiune vizionară */}
                    <section className="vision-section">
                        <h2>Tu ești eroul, MathClub doar te ajută!</h2>
                        <p>
                            Lecțiile explicate scurt și clar, testele interactive și explicațiile logice transformă matematica într-un proces
                            simplu și eficient. Copiii învață repede, rețin esențialul și prind încredere în forțele proprii.
                        </p>
                    </section>

                    {/* Secțiune audiență */}
                    <section className="audience-section">
                        <div>
                            <h3>🎓 MathClub pentru elevi</h3>
                            <p>Învăță ușor și rapid. Economisești timp și înveți cu plăcere.</p>
                        </div>
                        <div>
                            <h3>👨‍👩‍👧 MathClub pentru părinți</h3>
                            <p>Oferiți copiilor educație de calitate, fără stres și fără meditații costisitoare.</p>
                        </div>
                        <div>
                            <h3>👩‍🏫 MathClub pentru profesori</h3>
                            <p>Folosiți platforma ca suport educațional. Lecții clare, gata de folosit în clasă sau online.</p>
                        </div>
                    </section>

                    {/* Secțiune activități */}
                    <section className="features-section">
                        <h2>Activități pregătite pentru tine</h2>
                        <div className="feature-grid">
                            <div>
                                <h4>📘 Lecții explicate</h4>
                                <p>Conținut bazat pe programă, cu explicații logice, pas cu pas.</p>
                            </div>
                            <div>
                                <h4>📝 Teste interactive</h4>
                                <p>Imediat după lecție poți verifica ce ai înțeles.</p>
                            </div>
                            <div>
                                <h4>💬 Întrebări și răspunsuri</h4>
                                <p>Nu ai înțeles ceva? Întreabă și primește explicații clare.</p>
                            </div>
                            <div>
                                <h4>🎯 Motivație</h4>
                                <p>Înveți prin jocuri, logică și provocări captivante. Educație cu zâmbet!</p>
                            </div>
                        </div>
                    </section>
                </>
            ) : (
                <h2 style={{ marginTop: "50px", textAlign: "center" }}>
                    Welcome! Please login to access the platform.
                </h2>
            )}
        </div>
    );
};

export default Home;
