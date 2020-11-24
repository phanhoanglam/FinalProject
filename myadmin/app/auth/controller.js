const bcrypt = require('bcryptjs');

const BaseRepository = require('../core/base_repository');

const repository = new BaseRepository('administrators');

module.exports = {
  login: async (req, res) => {
    const { email, password } = req.body;
    const admin = await repository.getByCondition({ email });

    if (!admin) {
      return res.redirectBack();
    }

  },

  register: async (req, res) => {

  },
};
