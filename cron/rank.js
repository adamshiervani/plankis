var Report = require('../schemas/report.js'),
		config = require('../configuration.js');


module.exports = function () {
	var minutes = 1, interval = minutes * 60 * 1000;

	setInterval(function () {
		//Find all relevant reports, ie. those NOT 0 because otherwise we'll get a negative number.
		//Decrease all the relevant reports
		Report.update({rank: { $gt: config.confirm.spans.min}}, {$inc: { 'rank': config.confirm.spans.dec }}, function (err, report) {
			if (err) {
				res.json(204, []);
				return;
			}
		});

	}, interval);
};
