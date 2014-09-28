var mongoose = require('mongoose'),
			Schema = mongoose.Schema,
			config = require('../configuration.js');


var schema = new Schema({
	id: {
		type: String
	},
	uuid: {
		type: String
	},
	timestamp: {
		type: String
	},
});

schema.methods.setId = function(id) {
	this.id = id;
};

schema.methods.setUuid = function(uuid) {
	this.uuid = uuid;
};

schema.methods.setTimestamp = function(timestamp) {
	this.timestamp = timestamp;
};


mongoose.model('User', schema);
module.exports = mongoose.model('User');
