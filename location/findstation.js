var request = require('request');
var config = require('../configuration.js');

module.exports = function (latitude, longitude, boost, success) {
	var url = 'https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=' + latitude + ',' + longitude + '&radius=' + config.findarea.radius + '&types=' + config.findarea.types + '&sensor=false&key=AIzaSyDQlznNx7-3MAPuiCgBwq93ng_VBmm1WJg';
		console.log(url);
		
	//http://stackoverflow.com/questions/3089772/find-nearest-transit-station-stopover-bus-train-etc-in-google-maps
	// TODO: LOOP WITH INCREASING RADIUS
		var assert = require('assert');
		var util = require('util');
		var OsmMongo = require('../node_modules/openstreetmapmongo/lib/osm-mongo').OsmMongo;

		settings = {
			MONGO_HOST: 'localhost',
			MONGO_PORT: 27017
		};

		osm = new OsmMongo(settings.MONGO_HOST, settings.MONGO_PORT);

		// find a subway lines near Santiago center
		osm.findOsmNear([ -70.650404, -33.4379537 ], 16, {railway: "subway"}, function(error, subway) {
			var len = 0;
			subway.ways.forEach(function(way) {
				console.log(way);
			});
			success(error, way);
		});
};

