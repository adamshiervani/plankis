var mongoose = require('mongoose'),
	Schema = mongoose.Schema;

var ticketSchema = new Schema({

	content: {
		type: String
	},
	from: {
		type: String
	},
	uuid: {
		type: String
	},
	// TODO: Add CITY
	timestamp: {
		type: Number
	},
	// TODO: Add CITY
	expires: {
		type: Number
	}
});
ticketSchema.methods.setContent = function(content) {
	//TODO: Add validation??
	this.content = content;
};

ticketSchema.methods.setFrom = function(from) {
	//TODO: Add validation??
	this.from = from;
};

ticketSchema.methods.setUuid = function(uuid) {
	this.uuid = uuid;
};

ticketSchema.methods.setTimeStamp = function(timestamp) {
	this.textcontent = Date.now();
};

ticketSchema.methods.setExpires = function(expires) {
	this.expires = +expires;
};

mongoose.model('tickets', ticketSchema);
exports.tickets = mongoose.model('tickets');


var usedTicketsSchema = new Schema({

	ticketid: {
		type: String
	},
	from: {
		type: String
	},
	hostuuid: {
		type: String
	},
	clientuuid: {
		type: String
	},

	clientid: {
		type: String
	},
	timestamp: {
		type: Number
	},

	expires : {
		type: Number
	}

});
usedTicketsSchema.methods.setTicketid = function(content) {
	this.ticketid = content;
};

usedTicketsSchema.methods.setHostUuid = function(uuid) {
	this.hostuuid = uuid;
};

usedTicketsSchema.methods.setClientUuid = function(uuid) {
	this.clientuuid = uuid;
};

usedTicketsSchema.methods.setClientId = function(id) {
	this.clientid = id;
};

usedTicketsSchema.methods.setTimeStamp = function(timestamp) {
	this.textcontent = Date.now();
};

usedTicketsSchema.methods.setExpires = function(expires) {
	this.expires = +expires;
};

usedTicketsSchema.methods.setFrom = function(from) {
	this.from = +from;
};

mongoose.model('usedTickets', usedTicketsSchema);
exports.usedTickets = mongoose.model('usedTickets');

var ticketsPatternSchema = new Schema({

	city: {
		type: String
	},
	ticketpattern: {
		type: String
	},
	ticketfrom: {
		type: String
	}

});
ticketsPatternSchema.methods.setCity = function(city) {
	this.city = city;
};

ticketsPatternSchema.methods.setTicketPattern = function(pattern) {
	this.ticketpattern = pattern;
};
ticketsPatternSchema.methods.setTicketFrom = function(from) {
	this.ticketfrom = from;
};

mongoose.model('ticketsPattern', ticketsPatternSchema);
exports.ticketsPattern = mongoose.model('ticketsPattern');
