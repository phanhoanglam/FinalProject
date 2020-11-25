const express = require('express');

const router = express.Router();

const { list, store, update } = require('./controller');

router.get('/', list);

router.post('/update/:id', update);

module.exports = router;
