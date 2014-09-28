/**
 * Module dependencies.
 */
var express = require('express'),
  report = require('./routes/report'),
  ticket = require('./routes/ticket'),
  http = require('http'),
  mongoose = require('mongoose'),
  yelp = require('./location/findarea.js'),
  gcm = require('./gcm/index.js'),
  setup = require('./routes/setup'),
  rankCron = require('./cron/rank.js');


/**
 *	Mongo Configuration
 */
mongoose.connect('localhost', 'plankdb');

var db = mongoose.connection;
db.on('error', console.error.bind(console, 'Connection error:'));


var app = express();
app.configure(function() {
  app.set('port', process.env.PORT || 3010);
  app.use(express.logger('dev'));
  app.use(express.bodyParser());
});

// Configure development settings
app.configure('development', function() {
  app.use(express.errorHandler());
  app.locals.pretty = true;
});

/**
 *	HTTP-Requests
 */
//Check if the connection is open before setting up
db.once('open', function() {

  // Setup the Cronjob
  rankCron();

  // Profile
  app.post('/setup', setup.getConfiguration);

  //Reports
  app.get('/report', report.getReport);
  app.post('/report', report.addReport);

  //Tickets
  app.get('/ticket/:uuid', ticket.getTicket);
  app.post('/ticket', ticket.addTicket);
  app.get('/ticket/count', ticket.countValidTickets);

  app.post('/ticket/validation', ticket.validationTickets);

  //Google Cloud Messenging
  // app.get('/gcm', gcm.sendMessage);
  app.post('/gcm', gcm.setRegId);

});

/*
	Safe exit
*/
process.on('exit', function(err) {
  if (!err) {
    mongoose.connection.close();
  }
});

/**
 *	Und there shall be light
 */
http.createServer(app).listen(app.get('port'), function() {
  console.log('Express server listening on port ' + app.get('port'));
});
