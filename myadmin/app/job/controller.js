const knex = require('../../config/database');
const BaseRepository = require('../core/base_repository');

const repository = new BaseRepository('jobs');
const repositoryProposals = new BaseRepository('job_proposals');
const repositoryReview = new BaseRepository('reviews');
const repositoryJobSkill = new BaseRepository('job_skill');

module.exports = {
  list: async (req, res) => {
    const list = await repository.listBy({}, [
      '*',
      knex('employers').select(['name']).where('id', knex.ref('jobs.employer_id')).as('employer'),
      knex('job_proposals').where('job_id', knex.ref('jobs.id')).count().as('apply_count'),
    ]);

    res.render('app/job/index', {
      list,
      query: req.query,
    });
  },

  remove: async (req, res) => {
    const { id } = req.params;
    const list = await repositoryProposals.listBy({job_id: id},['*']);
    for (const element of list.results) {
      await repositoryReview.delete({job_proposal_id: element.id});
    }
    await repositoryJobSkill.delete({job_id: id});
    await repositoryProposals.delete({job_id: id});
    await repository.delete({id});

    res.json({
      success: true,
      message: 'Ok'
    });
  }
};
