require('dotenv/config');

const createError = require('http-errors');
const express = require('express');
const path = require('path');
const cookieParser = require('cookie-parser');
const logger = require('morgan');
const expressLayouts = require('express-ejs-layouts');
const session = require('express-session');

require('./config/database');
const useLocalModules = require('./config/local_modules');
const useCommonFunctions = require('./config/common');
const globalRouter = require('./app/routes');

const app = express();

// view engine setup
app.set('views', path.join(__dirname, 'views'));
app.set('view engine', 'ejs');

app.use(session({
  secret: 'qwertyuiopasdfghklzxcvbnm1234567890',
  resave: false,
  saveUninitialized: true,
  cookie: { maxAge: 60000 }
}));
app.use(logger('dev'));
app.use(express.json());
app.use(express.urlencoded({ extended: false }));
app.use(cookieParser());
app.use('/public', express.static(path.join(__dirname, 'public')));
app.use(expressLayouts);

app.use((req, res, next) => {
  useLocalModules(res);
  next();
});

app.use((req, res, next) => {
  useCommonFunctions(req, res);
  next();
});

app.use('/', globalRouter);

// catch 404 and forward to error handler
app.use(function(req, res, next) {
  next(createError(404));
});

// error handler
app.use(function(err, req, res, next) {
  // set locals, only providing error in development
  res.locals.message = err.message;
  res.locals.error = req.app.get('env') === 'development' ? err : {};

  // render the error page
  res.status(err.status || 500);
  console.log(err);
  res.send(err);
});

module.exports = app;
