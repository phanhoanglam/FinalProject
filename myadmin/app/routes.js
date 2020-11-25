const express = require('express');
const router = express.Router();

const authRoutes = require('./administrator/auth_routes');
const pageRoutes = require('./page/routes');
const jobTypeRoutes = require('./job_type/routes');
const jobCategoryRoutes = require('./job_category/routes');
const employerRoutes = require('./employer/routes');
const jobRoutes = require('./job/routes');
const administratorRoutes = require('./administrator/routes');

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
router.use('/jobs', jobRoutes);
router.use('/administrators', administratorRoutes);

module.exports = router;
