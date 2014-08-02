var	Report = require('../schemas/report.js'),
	findstation = require('../location/findstation.js'),
	utils = require('../utils/utils.js'),
	findArea = require('../location/findarea.js'),
	config = require('../configuration.js');


exports.getReport = function(req, res){
	if (!req.param('city')) {
		res.json(400, {error: 'Missing parameter!'});
		return;
	}
	// findArea.area(req.param('latitude'), req.param('longitude'), function (err, area) {
		//TODO: Only send relevant data, not only station, area, timestamp
		Report.find({ timestamp: { $gt: +(Date.now() - config.confirm.time.timediff)}, city: req.param('city')}, {area: 1, timestamp: 1, station: 1, rank: 1, '_id': 1}).sort({timestamp: -1}).exec(function(err, report) {
			if (err) {
				res.json(204, err);
				return;
			}
			res.json(200, report);
		});
	// })
};


exports.addReport = function (req, res) {
	/*
		Generate click matrix between all the different reports.
		 If a report is less than x Meters away from the new report, and withing X minutes, then add importance to the old report.
	*/
	console.log(req.body);
	if (!req.param('longitude') || !req.param('latitude') || !req.param('uuid')) {
		res.json(400, {error: 'Missing parameter!'});
		return;
	}

	//Set long, lati, and variables for future use
	var lat_origin = req.param('latitude');
	var long_origin = req.param('longitude');

	//TODO : Need a more optimized way of doing this. There is probably some MAX function in MongoDb
	Report.find({ timestamp: { $gt: +(Date.now() - config.confirm.time.timediff) } }).sort({timestamp: -1}).exec(function(err, report) {
		if (err) {
			res.json(204, []);
			return;
		}

		//Loop through all the reports within the time frame, and find the shortest distance
		var shortest, shortest_report_id;
		for (var i = 0; i < report.length; i++) {

			//Find the distance between the newly report and the previous ones(within the timframe)
			var distance = utils.distance(lat_origin , long_origin, report[i].latitude, report[i].longitude, config.confirm.unit);

			console.log('Distance: ' +  distance + ' KM');

			//If distance is shorter than previous and shortest is not undefined
			if (distance < shortest || shortest === undefined) {
				shortest  = distance;
				shortest_report_id = report[i]._id;
				console.log('Current Shortest: ' + shortest);
			}
		}
		callbackDbFind(shortest, shortest_report_id);
	});

	function callbackDbFind (shortest, shortest_report_id) {
		if (+shortest <= +config.confirm.map.boostDistance && shortest_report_id) {

			//Whohoo! There is already a report withing the distance and timeframe, thus, we increase the importance/rank on the report
			console.log('Updating existing report');

			//Get the ObjectID Type
			var ObjectId = require('mongoose').Types.ObjectId;

			// //Update the nearest report AND if the rank is not more than 30 , because that is the maximum!!
			Report.update(
				{
					_id : new ObjectId(shortest_report_id),
					rank: {
						$lt : config.confirm.spans.max - config.confirm.spans.inc
					}
				},  {$inc: {
						'rank':config.confirm.spans.inc
					}}
				,{upsert:true,safe:true}, function (err, report) {
						if (err) {
							res.json(204, []);
							return;
						}
						if (report === 1) {
							console.log('UPDATED');
							res.json(200, 'OK');
						}else{
							console.log('NOT NOT NOT UPDATED');
							res.json(200, 'OK');
						}
						//Success
				});

					console.log("Shortest distance: " + shortest);
		}else{


			// No previous entry within the distance and timeframe, thus, we need to create a new one.
			console.log('Not short distance and time. Cretes a new entry.');

			// //Create new entry because there isnt any similiar report made.
			findArea.area(req.param('latitude'), req.param('longitude'), function (err, area) {
				console.log(err);
				console.log(area);
				if (err || typeof(area) === "undefined") {
					console.log('Area error');
					res.json(401, 'Area Error');
					return;
				}

				//Create a new Report
				var report = new Report();
				console.log(JSON.stringify(area));
				//Set all the Report variables
				report.setLongitude(req.param('longitude'));
				report.setLatitude(req.param('latitude'));
				report.setArea(area.neighborhoods[0].name);
				report.setStation('nearStation');
				report.setCity(area.neighborhoods[0].city);
				// report.setStation(nearStation);
				report.setUuid(req.param('uuid'));
				report.setTimeStamp(+Date.now());


				//Save to database
				report.save(function (err, report) {
					if (err) {
						console.log('Save Error');
						res.json(400, report);
					}
					//Success
					res.json(200, report);
				});

			});


		}
	}
};
