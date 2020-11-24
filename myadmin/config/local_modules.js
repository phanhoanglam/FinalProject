const moment = require('moment');

module.exports = (res) => {
  res.locals.moment = moment;

  res.locals.getPageIndex = (page, perPage = 10) => {
    page = parseInt(page, 10);
    return ((!page || page <= 1) ? 0 : (page - 1) * perPage) + 1;
  };
};
