const express = require('express');
const router = express.Router();

const pageRoutes = require('./page/routes');
const jobTypeRoutes = require('./job_type/routes');

router.use('/', pageRoutes);
router.use('/job-types', jobTypeRoutes);

module.exports = router;
