const knex = require('../../config/database');
const BaseRepository = require('../core/base_repository');

const repository = new BaseRepository('employers');
const repositoryJobs = new BaseRepository('jobs');

module.exports = {
  list: async (req, res) => {
    const list = await repository.listBy({}, [
      '*',
      knex('jobs').where('employer_id', knex.ref('employers.id')).count().as('jobs_count'),
    ]);

    res.render('app/employer/index', {
      list,
      query: req.query,
    });
  },

  update: async (req, res) => {
    const { id } = req.params;
    const { body: data } = req;
    const attributes = {
      is_blocked: false,
      updated_at: Date
    };
    attributes.is_blocked = !data.is_blocked;
    delete data.id;
    const result = await repository.update({ id }, attributes , ['*']);

    if(result.is_blocked == true){
      const attributeJobs = {
        status: 3,
        updated_at: Date
      }
      const employer_id = id;
      await repositoryJobs.update({employer_id}, attributeJobs);
    }

    res.json({
      success: true,
      data: result,
      message: 'Ok',
    });
  },
};