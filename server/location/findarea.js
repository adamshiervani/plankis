var config = require('../configuration.js'),
	yelp = require('../api/yelpinterface.js').createClient(config.yelp.oauth2),
	request = require('request');


module.exports = function (latitude, longitude, err, success) {
	/*
		This is for using YELP API V2, V2 version of the API does not have a dedicated neighborhood api. 
		I will keep this code snippets for future refernece, in case of the removal of the old API. It's already depreceated
	*/

	/*yelp.search({ll:latitude + "," + longitude}, function (err, data) {
		success(data);
	});*/


	var url = 'http://api.yelp.com/neighborhood_search?lat=' + latitude + '&long=' + longitude + '&ywsid=' + config.yelp.oauth1.ywsid;
	
	request(url , function (error, response, body) {
		if (!error && response.statusCode === 200 && JSON.parse(body).neighborhoods.length > 0) {
			success(JSON.parse(body));
		}else{
			err(error);
		}
	});
	
};