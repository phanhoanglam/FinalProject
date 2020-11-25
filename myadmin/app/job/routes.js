const express = require('express');

const router = express.Router();

const { list, remove } = require('./controller');

router.get('/', list);

router.post('/delete/:id', remove);

module.exports = router;
