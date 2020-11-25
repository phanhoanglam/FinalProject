const express = require('express');

const router = express.Router();

const { showLoginForm, login, logout } = require('./controller');

router.route('/login')
    .get(showLoginForm)
    .post(login);

router.get('/logout', logout);

module.exports = router;
