module.exports = function (req, res) {
  res.redirectBack = () => {
    const backURL = req.header('Referer') || '/';
    return res.redirect(backURL);
  };
};
