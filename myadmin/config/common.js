module.exports = function (req, res) {
  res.locals.flash = req.session.flash;
  res.locals.currentUser = req.session.user;

  res.redirectBack = () => {
    const backURL = req.header('Referer') || '/';
    return res.redirect(backURL);
  };

  res.locals.old = (key, value = '') => {
    if (typeof res.locals.flash !== 'undefined' && res.locals.flash.oldValue) {
      const name = key.split('.');

      if (name.length > 1) {
        return res.locals.flash.oldValue[0][name[0]][name[1]] || (value || '');
      }

      return res.locals.flash.oldValue[0][key] || (value || '');
    }

    return value || '';
  };

  res.locals.errors = (key) => {
    if (typeof res.locals.flash !== 'undefined' && res.locals.flash.errors) {
      const { errors } = res.locals.flash;

      if (errors[0] && errors[0][key]) {
        const name = key.split('.');

        if (name.length > 1) {
          return errors[0][name[0]][name[1]].msg;
        }

        return errors[0][key].msg;
      }
    }

    return '';
  };
};
