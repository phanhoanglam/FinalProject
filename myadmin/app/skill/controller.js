const knex = require('../../config/database');
const BaseRepository = require('../core/base_repository');

const repository = new BaseRepository('skills');
const repositoryCategories = new BaseRepository('job_categories');

module.exports = {
  list: async (req, res) => {
    const list = await repository.listBy({},[
      '*',
      knex('job_categories').select(['name']).where('id', knex.ref('skills.job_category_id')).as('category'),
    ]);

    const categories = await repositoryCategories.listBy();

    res.render('app/skill/index', {
      list,
      categories,
      query: req.query,
    });
  },

  store: async (req, res) => {
    const { body: data } = req;

    const result = await repository.create(data, ['*']);

    res.json({
      success: true,
      data: result,
      message: 'Ok',
    });
  },

  update: async (req, res) => {
    const { id } = req.params;
    const { body: data } = req;
    delete data.id;

    const result = await repository.update({ id }, data, ['*']);

    res.json({
      success: true,
      data: result,
      message: 'Ok',
    });
  },
};
