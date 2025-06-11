const express = require('express');
const cors = require('cors');
const { createProxyMiddleware } = require('http-proxy-middleware');

const app = express();

// Activează CORS pentru toate rutele
app.use(cors());

// Proxy pentru /test -> http://localhost:5000
app.use('/test', createProxyMiddleware({
    target: 'http://localhost:10000',
    changeOrigin: true,
    pathRewrite: {
        '^/test': '', // elimină /test din URL
    },
}));

// Proxy pentru /idm -> http://localhost:6000
app.use('/idm', createProxyMiddleware({
    target: 'http://localhost:9000',
    changeOrigin: true,
    pathRewrite: {
        '^/idm': '', // elimină /idm din URL
    },
}));
// Proxy pentru /idm -> http://localhost:6000
app.use('/logic', createProxyMiddleware({
    target: 'http://localhost:11000',
    changeOrigin: true,
    pathRewrite: {
        '^/logic': '', // elimină /idm din URL
    },
}));

// Pornire server pe portul 4000
app.listen(4000, () => {
    console.log('Gateway rulează pe http://localhost:4000');
});



