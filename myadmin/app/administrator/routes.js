const express = require('express');

const router = express.Router();

const { list, create, store, edit, update, destroy } = require('./controller');

router.get('/', list);

router.route('/create')
  .get(create)
  .post(store);

router.route('/update/:id')
  .get(edit)
  .post(update);

router.route('/delete/:id')
  .get(destroy);

module.exports = router;
