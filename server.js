var express = require('express')
  , partials = require('express-partials')
  , passport = require('passport')
  , LocalStrategy = require('passport-local').Strategy
  , mongoose = require('mongoose')
  , secrets = require('./secrets.json')
  , twilio  = require('twilio')
  , request = require('request');

mongoose.connect(secrets.mongoURL);

var Image = mongoose.model('Image', { 
  base64: String,
  person: String
});

var Patient = mongoose.model('Patient', { 
  name: String,
  age: String,
  lat: String,
  lon: String,
  gender: String,
  baby_birth: String,
  my_birth: String,
  fromNumber: String
});

/*Image.remove({},function(){});
Patient.remove({},function(){});*/

var db = mongoose.connection;
db.on('error', console.error.bind(console, 'connection error:'));
db.once('open', function callback () {
  console.log('It\'s Open!');
});

/*var accountSid = secrets.TWILIO_ACCOUNT_SID;
var authToken = secrets.TWILIO_AUTH_TOKEN;
var client = require('twilio')(accountSid, authToken);
*/

var config = {
    authToken: secrets.TWILIO_AUTH_TOKEN
};

var app = express();

var doctorCredentials = {
	username: 'onco@gram.com',
	password: 'password'
}

app.set('views', __dirname + '/views');
app.set('view engine', 'ejs');
app.use(partials());
app.use(express.logger());
app.use(express.cookieParser());
app.use(express.bodyParser());
app.use(express.session({ secret: 'hack kitty was here' }));
app.use(passport.initialize());
app.use(passport.session());
app.use(app.router);
app.use(express.errorHandler({ dumpExceptions: true, showStack: true }));

passport.use(new LocalStrategy(
  function(username, password, done) {
  	if (username == doctorCredentials.username &&
  		password == doctorCredentials.password) {
  		done(null, {
			username: username
		});
  	} else {
	  	done(null, false);
  	}
	/*Patient.findOne({ username: username }, function (err, user) {
      if (err) { return done(err); }
      if (!user) { return done(null, false); }
      if (!user.verifyPassword(password)) { return done(null, false); }
      return done(null, user);
    });*/
  }
));

passport.serializeUser(function(user, done) {
  done(null, user.username);
});

passport.deserializeUser(function(id, done) {
  done(null, {username: id});

/*  findById(id, function (err, user) {
    
  });*/
});

app.use('/js', express.static(__dirname + '/js'));
app.use('/css', express.static(__dirname + '/css'));
app.use('/public', express.static(__dirname + '/public'));

app.get('/', 
	function(req, res) {
	res.render('home', {
		page: req.url,
		user: req.user
	});
});

app.get('/login', 
	function(req, res){
	res.render('login', {
		page: req.url,
		user: req.user
	});
});

app.get('/profile', ensureAuthenticated, 
  function(req, res){
    if (req.query.id) {
      Patient.findOne({'_id': req.query.id}, function(err, person) {
        if (err) {console.log(err);}
        console.log(person);
        if (person) {
          Image.find({'person': req.query.id}, function(err, images) {
            if (err) {console.log(err);}
            console.log('Length:');
            console.log(images.length);
            res.render('profile', {
              page: req.url,
              user: req.user,
              person: person,
              images: images
            });
          });
        } else {
          res.redirect('/dashboard'); 
        }
      });

/*      res.render('profile', {
        page: req.url,
        user: req.user,
        person: {
          name: 'Person 1',
          location: {
            lat: 10,
            lon: 10
          },
          age: 45
        },
        images: [secrets.sampleImage]
      });*/
/*      User.findOne({'_id': req.query.id}, function(err, user) {
              

      });*/
    } else {
      res.redirect('/dashboard');
    }
});

app.get('/search', ensureAuthenticated,
  function(req, res) {
    var q = req.query.q || '';
    //name: '.*' + q + '.*'
    Patient.find({name: { $regex: '.*' + q + '.*', $options: 'i' }}, function(err, people) {
      console.log(people);
      for (i in people) {
        console.log(i)
        var p = people[i];
        console.log(people[i]);
      }
      res.json(people);
    });
/*    res.json([{
      id: 0,
      name: 'Person 1',
      location: {
        lat: 10,
        lon: 10
      },
      age: 45
    },{
      id: 1,
      name: 'Person 2',
      location: {
        lat: 20,
        lon: 20
      },
      age: 55
    }]);*/
});

app.get('/dashboard', ensureAuthenticated,
	function(req,res){
	res.render('dashboard', {
		page: req.url,
		user: req.user
	});
});

app.get('/logout', function(req, res){
  req.logOut();
  res.redirect("/");
});

app.post('/login', 
  passport.authenticate('local', { failureRedirect: '/login' }),
  function(req, res) {
  	console.log(req.user);
    res.redirect('/dashboard');
});

var loadBase64Image = function (url, callback) {
    // Make request to our image url
    var options = {uri: url, encoding: null};
    request(options, function (err, res, body) {
        if (!err && res.statusCode == 200) {
            // So as encoding set to null then request body became Buffer object
            var base64prefix = 'data:' + res.headers['content-type'] + ';base64,'
                , image = body.toString('base64');
            if (typeof callback == 'function') {
                callback(image, base64prefix);
            }
        } else {
            throw new Error('Can not download image');
        }
    });
};

app.post('/incoming', function(req, res) {
    //console.log(req);
    if (true || twilio.validateExpressRequest(req, config.authToken)) {
        var resp = new twilio.TwimlResponse();
        console.log(req.body);
        console.log(req.body.Body);
        var message = JSON.parse(req.body.Body);
        var regex = /\{(.*?)\}/;
        var personMessage = JSON.parse(message.info.match(regex)[0]);
        var fromNumber = req.body.From;

        var imageUrl = message.url;
        loadBase64Image(imageUrl, function(image, prefix) {
          console.log(image);
          if (!!image) {
            Patient.find({fromNumber: fromNumber, name: personMessage.name}, function(err, people) {
              if (err) { console.log(err); }
              var person = people[0];
              console.log('Person: ');
              console.log(person);
              console.log(!person);
              if (!person) {
                var person = new Patient(
                {
                  name: personMessage.name,
                  age: personMessage.age,
                  lat: personMessage.lat || 0,
                  lon: personMessage.lon || 0,
                  gender: personMessage.gender,
                  baby_birth: personMessage.baby_birth,
                  my_birth: personMessage.my_birth,
                  fromNumber: fromNumber
                });
                person.save(function (err, user) {
                  if (err) { console.log(err); }
                  console.log('Successful Patient Save ' + user);
                });
                console.log(person);
              }
              var newImage = new Image({
                base64: image,
                person: person._id
              });
              console.log(newImage);
              //Add image
              newImage.save(function (err, user) {
                if (err) { console.log(err); }
                console.log('Successful Image Save');
              });
            });
          }
        });

        resp.message('Thanks for the image! URL: ' + imageUrl);

        res.type('text/xml');
        return res.send(resp.toString()); 
    } else {
        return res.send('Nice try, imposter.');
    }
});

var server = app.listen(8111, function() {
    console.log('Listening on port %d', server.address().port);
});

function ensureAuthenticated(req, res, next) {
  if (req.isAuthenticated()) { return next(); }
  res.redirect('/login')
}

/*
var http = require('http');
http.createServer(function (req, res) {
    res.writeHead(200, {'Content-Type': 'text/plain'});
    res.end('hello, i know nodejitsu\n');
}).listen(8111);
console.log('Server running at http://localhost:8111');*/