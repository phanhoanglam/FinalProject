const express = require('express');

const router = express.Router();

const { list, create, store, destroy } = require('./controller');

router.get('/', list);

router.route('/create')
  .get(create)
  .post(store);

router.route('/delete/:id')
  .get(destroy);

module.exports = router;
