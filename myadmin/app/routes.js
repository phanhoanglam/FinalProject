const express = require('express');
const router = express.Router();

const pageRoutes = require('./page/routes');
const jobTypeRoutes = require('./job_type/routes');
const jobCategoryRoutes = require('./job_category/routes');

router.use('/', pageRoutes);
router.use('/job-types', jobTypeRoutes);
router.use('/job-categories', jobCategoryRoutes);

module.exports = router;
