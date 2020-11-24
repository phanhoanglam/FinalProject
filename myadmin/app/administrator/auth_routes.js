const express = require('express');

const router = express.Router();

const { showLoginForm, login } = require('./controller');

router.route('/login')
    .get(showLoginForm)
    .post(login);

module.exports = router;
