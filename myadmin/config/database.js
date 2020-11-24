const Knex = require('knex');
const KnexQueryBuilder = require('knex/lib/query/builder');

function setupPaginator(knex) {
  KnexQueryBuilder.prototype.paginate = function callback(page = '1', perPage = 10) {
    const offset = (page - 1) * perPage;
    const promises = [
      this.clone()
        .clearSelect()
        .clearOrder()
        .count('* as total')
        .first(),
      this.offset(offset).limit(perPage),
    ];

    return Promise.all(promises).then(([{ total }, results]) => ({
      results,
      totalPage: Math.ceil(total / perPage),
      total: parseInt(total, 10),
      page: parseInt(page, 10),
      perPage: parseInt(perPage, 10),
    }));
  };

  knex.queryBuilder = function queryBuilder() {
    return new KnexQueryBuilder(knex.client);
  };
}

const knex = Knex({
  client: 'mssql',
  connection: {
    server : '127.0.0.1',
    user : process.env.DB_USERNAME,
    password : process.env.DB_PASSWORD,
    database : process.env.DB_NAME,
    options: {
      port: 1433,
    },
  },
  asyncStackTraces: true,
});

setupPaginator(knex);

module.exports = knex;
