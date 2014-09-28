var findArea        = require('./../location/findarea');
var     async       = require('async');
var TicketPatterns  = require('./../schemas/ticket').ticketsPattern;

exports.getConfiguration = function (req, res) {


    if (!req.param('city') || !req.param('uuid')) {
        res.send(400, {error: 'Missing parameter!'});
        return;
    }
    //
    // report = new TicketPatterns();
    // //Set all the Report variables
    // report.setCity(req.param('city'));
    // report.setTicketPattern('pattern');
    // report.setTicketFrom('1234');
    //
    // report.save(function (err, save) {
    //     console.log(save);
    // });
    // var uuid = req.param('uuid');
    //
    // res.send(200, {city: req.param('city'), ticketfrom:"asdasd", ticketpattern: "patter"})

    TicketPatterns.findOne({city: req.param('city')}, {city: 1, ticketfrom: 1, ticketpattern: 1}, function (err, ticketPattern) {
        if (err ) {
            res.send(401, {error: err});
            return;
        }

        if (ticketPattern === {} || ticketPattern === null) {
          res.send('200', {error: err});
        }

        res.send(200, ticketPattern);
    });


};
