'user strict';
module.exports = {

  findarea: {
    radius: 1000,
    types: 'subway_station,bus_station,train_station'
  },
  yelp: {
    oauth2: {
      consumer_key: "aBvlOelO4pMRt3ujfH5tXw",
      consumer_secret: "ZdnufhFVyu6iibxmfmw1kX8kw60",
      token: "XEq1cOVR8poToi8wQRu5zYXr7usyTfM6",
      token_secret: "3d7qf5Cr23TcRS3C3kQFvj6Iabc"
    },
    oauth1: {
      ywsid: '-TiLIqt5lksEhGxrPcITqQ'
    }
  },
  tickets: {
    expires: 5400000 - 300000,
    //5 400 000 ms = 1.5 Hours
    //300000 ms = 5 min
    //This is to have some margin between getting the ticket and actually using it.
  },
  confirm: {
    map: {
      boostDistance: 1,
      unit: 'K'
    },
    time: {
      timediff: 1800000
      // 1800000 ms = 30min
    },
    spans: {
      max: 30,
      min: 0,
      inc: 3,
      dec: -1
    }
  }
};
