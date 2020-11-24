const express = require('express');
const router = express.Router();

const { renderDashboard } = require('./controller');

router.get('/', renderDashboard);

module.exports = router;
