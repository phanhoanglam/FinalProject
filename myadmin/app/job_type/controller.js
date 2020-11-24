const BaseRepository = require('../core/base_repository');

const repository = new BaseRepository('job_types');

module.exports = {
  list: async (req, res) => {
    const list = await repository.listBy();

    res.render('app/job_type/index', {
      list,
      query: req.query,
    });
  }
};
