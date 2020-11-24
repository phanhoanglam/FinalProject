const bcrypt = require('bcryptjs');

const BaseRepository = require('../core/base_repository');

const repository = new BaseRepository('administrators');

module.exports = {
  showLoginForm: (req, res) => {
    if (req.session.user) {
      return res.redirect('/');
    }
    res.render('app/auth/login', { layout: 'base' });
  },

  login: async (req, res) => {
    const { email, password } = req.body;
    const item = await repository.getByCondition({ email });

    if (!item || !bcrypt.compareSync(password, item.password)) {
      req.flash('oldValue', { email: req.body.email });
      req.flash('errors', {
        password: { msg: 'Email or password is not correct' },
      });

      return res.redirectBack();
    }
    req.session.user = {
      id: item.id,
      name: item.name,
      avatar: item.avatar,
    };

    return res.redirect('/');
  },

  list: async (req, res) => {
    const list = await repository.listBy();

    res.render('app/administrator/index', {
      list,
      query: req.query,
    });
  },

  create: async (req, res, next) => {
    res.render('app/administrator/create');
  },

  store: async (req, res, next) => {

  },

  edit: async (req, res) => {
    res.render('app/administrator/create');
  },

  update: async (req, res, next) => {},

  destroy: async (req, res, next) => {},
};
