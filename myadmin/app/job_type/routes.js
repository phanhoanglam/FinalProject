const express = require('express');
const router = express.Router();

const { list } = require('./controller');

router.get('/', list);

module.exports = router;
