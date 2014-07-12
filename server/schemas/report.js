var mongoose = require('mongoose'),
	Schema = mongoose.Schema,
	config = require('../configuration.js');


var schema = new Schema({

	longitude: {
		type: String
	},

	latitude: {
		type: String
	},
	
	area: {
		type: String
	},

	station: {
		type: String
	},
	// TODO: Add CITY
	rank: {
		type: Number,
		default: config.confirm.spans.max,
		max : config.confirm.spans.max
	},

	uuid: {
		type: String
	},

	timestamp: {
		type: Number
	}
});
schema.methods.setLongitude = function(longitude) {
	//TODO: Add validation??
	this.longitude = longitude;
};

schema.methods.setLatitude = function(latitude) {
	this.latitude = latitude;
};

schema.methods.setArea = function(area) {
	this.area = area;
};

schema.methods.setStation = function(station) {
	this.station = station;
};

schema.methods.setRank = function(rank) {
	if (rank === 1) {
		this.rank += 1;
	}else if(rank === 0){
		this.rank -= 1;
	}
};

schema.methods.setUuid = function(uuid) {
	this.uuid = uuid;
};

schema.methods.setTimeStamp = function(timestamp) {
	this.timestamp = +timestamp;
};

mongoose.model('Report', schema);
module.exports = mongoose.model('Report');