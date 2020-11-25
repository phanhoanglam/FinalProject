const knex = require('../../config/database');
const BaseRepository = require('../core/base_repository');

const repository = new BaseRepository('employees');
const repositoryJobs = new BaseRepository('jobs');

module.exports = {
  list: async (req, res) => {
    const list = await repository.listBy({}, [
      '*',
    ]);

    const page = req.query.page ? req.query.page : 1;

    res.render('app/employee/index', {
      list,
      page,
      query: req.query
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

    if(result.is_blocked === true){
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
