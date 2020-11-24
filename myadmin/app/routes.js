const express = require('express');
const router = express.Router();

const authRoutes = require('./auth/routes');
const pageRoutes = require('./page/routes');
const jobTypeRoutes = require('./job_type/routes');
const jobCategoryRoutes = require('./job_category/routes');
const employerRoutes = require('./employer/routes');

router.use('/auth', authRoutes);

router.use((req, res, next) => {
  if (!req.session.user) {
    return res.redirect('/auth/login');
  }
  next();
});

router.use('/', pageRoutes);
router.use('/job-types', jobTypeRoutes);
router.use('/job-categories', jobCategoryRoutes);
router.use('/employers', employerRoutes);

module.exports = router;
