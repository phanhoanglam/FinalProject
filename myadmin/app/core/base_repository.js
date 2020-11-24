const knex = require('../../config/database');

class BaseRepository {
  constructor(tableName) {
    if (!tableName) {
      throw new Error('You have to specify this, buddy!');
    }
    this.tableName = tableName;
  }

  cloneQuery() {
    return knex(this.tableName).clone();
  }

  async listBy(clauses = {}, columns = ['*'], options = {}) {
    const { page = '1', perPage = 10 } = options;
    let query = this.cloneQuery().select(columns).orderBy('created_at', 'desc');
    query = this.handleConditions(query, clauses);

    return query.paginate(page, perPage);
  }

  getByCondition(clauses, columns = ['*']) {
    let query = this.cloneQuery().select(columns);
    query = this.handleConditions(query, clauses);

    return query.first();
  }

  getById(id, columns = ['*']) {
    return this.getByCondition({ id }, columns);
  }

  handleConditions(query, clauses) {
    if (typeof clauses === 'function') {
      query = clauses(query);
    } else {
      query = query.where(clauses);
    }

    return query;
  }

  async create(attributes, returningColumn = 'id') {
    attributes.created_at = new Date();
    attributes.updated_at = new Date();

    return (await this.cloneQuery().table(this.tableName).insert(attributes).returning(returningColumn))[0];
  }

  async update(clauses, attributes, returning = ['id']) {
    const query = this.handleConditions(this.cloneQuery(), clauses);
    attributes.updated_at = new Date();

    return (await query.update(attributes, returning))[0];
  }

  delete(clauses) {
    const query = this.handleConditions(this.cloneQuery(), clauses);

    return query.delete();
  }
}

module.exports = BaseRepository;
