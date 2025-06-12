import React, { useState } from "react";
import "../pages/style.css";

const Contact = () => {
    const [expandedIndex, setExpandedIndex] = useState(null);

    const toggleAccordion = (index) => {
        setExpandedIndex(expandedIndex === index ? null : index);
    };

    const questions = [
        {
            title: "Cum pot contacta echipa MathClub?",
            content: (
                <>
                    Ne poți contacta prin email la <a href="mailto:help@mathclub.ro">help@mathclub.ro</a>, pe <a href="https://facebook.com">Facebook</a>
                    și prin intermediul secțiunilor de comentarii din aplicație. Răspundem activ în toate aceste locuri.
                </>
            )
        },
        {
            title: "Care este programul de răspuns la mesaje?",
            content: (
                <>Răspundem la emailuri de luni până vineri, între orele <strong>09:00 - 17:00</strong>. În weekend, echipa noastră este disponibilă doar pentru urgențe.</>
            )
        },
        {
            title: "Cum pot oferi feedback despre platformă?",
            content: (
                <>Poți trimite sugestii, idei sau reclamații prin formularul de contact sau direct pe email. Ne bucurăm de fiecare propunere care ne ajută să devenim mai buni!</>
            )
        }
    ];

    return (
        <div className="faq-container">
            <h1 className="faq-title">Contact și suport</h1>
            {questions.map((item, index) => (
                <div key={index} className="faq-item">
                    <button
                        className={`faq-question ${expandedIndex === index ? 'open' : ''}`}
                        onClick={() => toggleAccordion(index)}
                    >
                        {item.title}
                        <span className="arrow">{expandedIndex === index ? '▲' : '▼'}</span>
                    </button>
                    {expandedIndex === index && (
                        <div className="faq-answer">
                            {item.content}
                        </div>
                    )}
                </div>
            ))}
        </div>
    );
};

export default Contact;