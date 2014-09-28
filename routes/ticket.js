var	Ticket = require('../schemas/ticket.js').tickets ,
	Used = require('../schemas/ticket.js').usedTickets ,
	User = require('../schemas/user.js'),
	utils = require('../utils/utils.js'),
	gcm = require('../gcm/index.js'),
	config = require('../configuration.js');

exports.getTicket = function(req, res){
	/*
		TODO: Check if UUID exist in DB.
		This should be in the db cause when the application needs to register at start for GCM, push notification
	*/


	/*
		TODO:
		Check if a ticket already is sent. IF so then it should send the same. Do stop spam
	*/

	if (!req.param('uuid')) {
		res.json(400, {error: 'Missing parameters'});
		return;
	}

	var timespan = +(Date.now() - config.tickets.expires);

	// Get the oldest ticket, but it should still be valid
	// Ticket.find( { expires: { $gt: Date.now() } },{} ).sort({timestamp: -1}).exec(function(err, tickets) {
	Ticket.find({expires : {$gt : Date.now()}} ,{} ).sort({timestamp: -1}).exec(function(err, tickets) {
		tickets = tickets;
		if (err) {
			res.json(400, {status: err});
			return;
		}

		//IMPORTANT
		if (tickets.length === 0) {
			console.log('No tickets available');
			res.json(400, {status: 'No tickets available'});
			return;
		}else{
			// Send the oldest ticket!
			var validTicket = tickets[0];

			//Track the usedTicket
			var usedTicket = new Used();

			usedTicket.setTicketid(validTicket._id);
			usedTicket.setHostUuid(validTicket.uuid);
			usedTicket.setClientUuid(req.param('uuid'));

			//Set the clients GCM reg id
			User.findOne({uuid: req.param('uuid')},{id: 1}, function (err, users) {

				usedTicket.setClientId(users.id);
				usedTicket.setTimeStamp(Date.now());
				usedTicket.setExpires(validTicket.expires);
				console.log('Expiration date for the sent ticket is: ' + new Date(validTicket.expires).toString());
				usedTicket.save(function (err, usedTickets) {
					if (err) {
						res.json(400, err);
						return;
					}
					//TODO: Does it really have to be in this callback, Can it be further up. Does the function still run when answering on the request
					res.json(200, {from: validTicket.from, message: validTicket.content});
				});
			});
		}

	});

	// TODO: Track Used tickets.

};


exports.addTicket = function (req, res) {
	console.log(req.body);

	if (!req.param('msgTicket') || !req.param('msgFrom') || !req.param('uuid')) {
		res.json(204, {"status": "Missing parameters."});
		return;
	}

	var ticket = new Ticket();

	ticket.setContent(req.param('msgTicket'));
	ticket.setFrom(req.param('msgFrom'));
	ticket.setUuid(req.param('uuid'));

	//Mock data
	// ticket.setContent('msgTicket');
	// ticket.setFrom('msgFrom');
	// ticket.setUuid('uuid');

	ticket.setTimeStamp(Date.now());
	ticket.setExpires(Date.now() + config.tickets.expires);

	ticket.save(function (err, ticket) {
		if (err) {
			console.log(err);
			res.json(204, err);
			return;
		}

		console.log(ticket);
		res.send(200, ticket);
	});
};

exports.validationTickets = function (req, res) {
	console.log(req.body);
	if (!req.param('uuid') || !req.param('msgvalidation') || !req.param('msgfrom')) {
		res.json(400, {status: "Missing parameters!"});
	}

		console.log("UUID: " +  req.param('uuid'));
		Used.find({hostuuid: req.param('uuid')},{clientid: 1},function (err, used) {
			if (err) {
				res.json(400, {error: err});
			}
			var sendValidationTo = [];
			for (var i = 0; i < used.length; i++) {
				sendValidationTo.push(used[i].clientid);
			}

			gcm.sendMessage(req.param('msgfrom'), req.param('msgvalidation'), sendValidationTo, function (err, result) {
				if (err) {
					res.json(400, {error: err});
					return;
				}
				result.asasd = 'asd';
				res.json(result);
			});
		});
};

exports.countValidTickets = function (req, res) {
	timespan = +(Date.now() - config.tickets.expires);

	Ticket.count({ timestamp: { $gt: timespan }}, function (err, c) {
		if (err) {
			res.json(204, {error: err});
		}
		res.json(200, c);
	});
};
