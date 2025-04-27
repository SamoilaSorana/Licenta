import React, {useEffect, useState} from "react";
import Cookies from 'js-cookie';
import image from '../images/image.jpg';
import './style.css';
import {useWindowSize} from "./useWindowSize";
import axios from "axios";
// import {useNavigate} from "react-router-dom";

const Home = () => {
    const [permisiuni, setPermisiuni] = useState([]);

    const hasPermisiuni = (permisiune) => {
        return permisiuni.includes(permisiune);
    };
    useEffect(() => {
        const token = Cookies.get('token'); // Assuming token stored in cookie named 'token'
        if (token) {
            axios.get('http://localhost:4000/idm/api/auth/check-token', {
                headers: {
                    Authorization: `Bearer ${token}`
                }
            })
                .then(response => {
                    console.log('✅ Token valid:', response.data);
                    setPermisiuni(response.data.permisiuni);

                })
                .catch(error => {
                    console.log('❌ Token invalid:', error.response.data);
                    Cookies.remove('token'); // Remove token from cookies
                    setPermisiuni([]);
                });
        } else {
            setPermisiuni([]);
        }
    }, []);

    return (
        <div>
            {hasPermisiuni("GET_ACCOUNTS") && <h1>Accounts Page</h1>}
            {hasPermisiuni("accesare_catalog") && <h1>Catalog Page</h1>}
            {!permisiuni.length && <h1>Please login</h1>}
        </div>
    );

}

export default Home;