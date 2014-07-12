var Report = require('../schemas/report.js'),
	config = require('../configuration.js'),
	ObjectId = require('mongoose').Types.ObjectId; 

module.exports = function () {
	var minutes = 1, interval = minutes * 60 * 1000;
	
	setInterval(function () {
		console.log('----------');	
		console.log('----------');	
		console.log('THE RANK IS UPDATING!!');	
		console.log('----------');	
		console.log('----------');	

		//Find all relevant reports, ie. those NOT 0 because otherwise we'll get a negative number. 
		Report.find({ rank: { $gt: config.confirm.spans.min} }).sort({timestamp: -1}).exec(function(err, report) {
			if (err) {
				res.json(204, []);
				return;
			}
			for (var i = 0; i < report.length; i++) {

				//Decrease all the relevant reports
				Report.update({_id : new ObjectId(report[i]._id)}, {$inc: { 'rank': config.confirm.spans.dec }}, function (err, report) {
					if (err) {
						res.json(204, []);
						return;
					}
					console.log('------------');
					console.log('THE UPDATE WAS SUCCESSFULL!');
					console.log('------------');
				});
			}

		});

	}, interval);
};