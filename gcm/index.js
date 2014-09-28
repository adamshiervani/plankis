var config = require('../configuration.js'),
	gcm = require('node-gcm'),
	utils = require('../utils/utils.js'),
	Report = require('../schemas/report.js'),
	User = require('../schemas/user.js');

// exports.sendMessage = function (validationFrom, validationMessage , registrationIds, callback) {
exports.sendMessage = function (validationFrom, validationMessage , lol, callback) {
	// create a message with default values
	var message = new gcm.Message();

	// Reference
	// var message = new gcm.Message({
	//     collapseKey: 'demo',
	//     delayWhileIdle: true,
	//     timeToLive: 3,
	//     data: {
	//         key1: 'message1',
	//         key2: 'message2'
	//     }
	// });

	var sender = new gcm.Sender('AIzaSyB80c4MgZ3skKI9Tup-Q8sg6eQ88203TiQ');
	var registrationIds = [];

	// or add a data object
	message.addDataWithObject({
	    msgFrom: validationFrom,
	    message: validationMessage
	});

	// At least one required
	registrationIds.push('APA91bHbFIrq6L0dLRcjCnPQzoeJSl1srE1nWTAnyOg0dXcWYqVAfhfl8hL4gcSGqPWT684C6LMPkHxXFOq2g5OBFgb2-ppfIrTqhi_Yszh9ppAGQ85-6HTO-H9CJClF231QWjwHfo_75CsgylSelcbzVAUIcFZRDweuMYdTfC6jXc5Un5IGe7s');

	sender.send(message, registrationIds, 4, function (err, result) {
		console.log(result);
	    callback(err, result);
	});

};

exports.setRegId = function (req, res) {

	if (!req.param('id') || !req.param('uuid')) {
		res.json(500, {error: 'Missing parameters'});
		return;
	}

	User.find({uuid: req.param('uuid')}, {}).sort({timestamp: -1}).exec(function(err, report) {
		if (err) {
			res.json(204, {"status": err});
			return;
		}

		// If they don't exist
		if (report.length === 0) {
			var user = new User();

			user.setId(req.param('id'));
			user.setUuid(req.param('uuid'));
			user.setTimestamp(req.param(Date.now()));

			user.save(function (err, user) {
				if (err) {
					res.json(400, err);
				}
				res.json(200, user);
			});

		}

		res.json(200, report);
	});
};
