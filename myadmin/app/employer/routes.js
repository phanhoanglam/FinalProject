const express = require('express');

const router = express.Router();

const { list, update } = require('./controller');

router.get('/', list);

router.post('/update/:id', update);

module.exports = router;