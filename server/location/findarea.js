var config = require('../configuration.js'),
	yelp = require('../api/yelpinterface.js').createClient(config.yelp.oauth2),
	request = require('request');


exports.area = function (latitude, longitude, callback) {
	/*
		This is for using YELP API V2, V2 version of the API does not have a dedicated neighborhood api.
		I will keep this code snippets for future refernece, in case of the removal of the old API. It's already depreceated
	*/

	/*yelp.search({ll:latitude + "," + longitude}, function (err, data) {
		callback(data);
	});*/


	var url = 'http://api.yelp.com/neighborhood_search?lat=' + latitude + '&long=' + longitude + '&ywsid=' + config.yelp.oauth1.ywsid;

	request(url , function (error, response, body) {
		console.log(body);
		if (!error && response.statusCode === 200 && JSON.parse(body).neighborhoods.length > 0) {
			console.log(body);
			callback(error, JSON.parse(body));
		}else{
			callback(error);
		}
	});

};


exports.city = function (latitude, longitude, callback) {
	console.log('CITY');
	var yelp = require("../api/yelpinterface").createClient({
		consumer_key: "aBvlOelO4pMRt3ujfH5tXw",
		consumer_secret: "ZdnufhFVyu6iibxmfmw1kX8kw60",
		token: "XEq1cOVR8poToi8wQRu5zYXr7usyTfM6",
		token_secret: "3d7qf5Cr23TcRS3C3kQFvj6Iabc"
	});

	yelp.search({ll: '52.52,13.405'}, callback);
}
