var request = require('request');
var config 	= require('../configuration.js');

module.exports = function (latitude, longitude, callback) {
		var nearbyStationPath = 'http://localhost:8081/api/StopsNearby/'+ latitude+'/'+ longitude +'/0.25';

		request(nearbyStationPath, function (error, response, body) {
			if (error){
				callback(error, {});
				return;
			}

			callback(null, JSON.parse(body));
		});

};
