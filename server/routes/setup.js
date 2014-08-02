var findArea = require('./../location/findarea');
var async = require('async');
var TicketPatterns = require('./../schemas/ticket').ticketsPattern;

exports.getConfiguration = function (req, res) {


    if (!req.param('longitude') || !req.param('latitude') || !req.param('uuid')) {
        res.send(400, {error: 'Missing parameter!'});
        return;
    }

    //Todo: remove this
    var report = new TicketPatterns();
    //Set all the Report variables
    report.setCity('Berlin');
    report.setTicketPattern('pattern');
    report.setTicketFrom('1234');

    report.save(function (err, save) {
        console.log(save);
    });


    report = new TicketPatterns();
    //Set all the Report variables
    report.setCity('Paris');
    report.setTicketPattern('pattern');
    report.setTicketFrom('1234');

    report.save(function (err, save) {
        // console.log(save);
    });
    var lat_origin = req.param('latitude'),
    long_origin = req.param('longitude'),
    uuid = req.param('uuid');

    findArea.area(lat_origin, long_origin, function (err, area) {
        console.log(area);
        if (err || typeof(area) === "undefined" || typeof(area.neighborhoods) === "undefined" || area.neighborhoods.length === 0) {
            res.send(401, null);
            return;
        }

        TicketPatterns.findOne({city: area.neighborhoods[0].city}, {city: 1, ticketfrom: 1, ticketpattern: 1}, function (err, ticketPattern) {
            console.log(ticketPattern);
            if (err || ticketPattern === null || ticketPattern === {}) {
                console.log(err);
                res.send(401, err);
                return;
            }

            res.send(200, ticketPattern);
        });
    });


};
