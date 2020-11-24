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

  store: async (req, res) => {
    const { body: data } = req;
    data.password = bcrypt.hashSync(data.password, bcrypt.genSaltSync(10));

    await repository.create(data);

    return res.redirect('/administrators');
  },

  destroy: async (req, res) => {
    const { id } = req.params;
    await repository.delete({ id });

    return res.redirect('/administrators');
  },
};
