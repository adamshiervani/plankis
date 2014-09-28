var request = require('request');
var config 	= require('../configuration.js');

module.exports = function (latitude, longitude, callback) {
	// Convert distance from kilometer to miles with doing km times 0.62137
		var nearbyStationPath = 'http://127.0.0.1:8081/api/StopsNearby/'+ latitude+'/'+ longitude +'/' + config.confirm.map.boostDistance * 0.62137;

		request(nearbyStationPath, function (error, response, body) {
			if (error){
				callback(error, null);
				return;
			}

			callback(null, JSON.parse(body));
		});

};
