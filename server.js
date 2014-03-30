var express = require('express')
  , partials = require('express-partials')
  , passport = require('passport')
  , LocalStrategy = require('passport-local').Strategy
  , mongoose = require('mongoose')
  , secrets = require('./secrets.json')
  , twilio  = require('twilio');

/*mongoose.connect(secrets.mongoURL);

var db = mongoose.connection;
db.on('error', console.error.bind(console, 'connection error:'));
db.once('open', function callback () {
});

var Image = mongoose.model('Image', { 
});

var User = mongoose.model('User', { 
  id: String,
  name: String,
  email: String,
  recent: Array
});
*/

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

/* Goals

get twilio working
get authentication working
get mongo working

*/

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
	/*User.findOne({ username: username }, function (err, user) {
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
	done(null, {
		username: id
	});	
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

app.post('/incoming', function(req, res) {
 
    // Validate Twilio request
    if (twilio.validateExpressRequest(req, config.authToken)) {
        var resp = new twilio.TwimlResponse();
        var imageUrl;
 
 		console.log(req.body);
 		console.log(req.body.NumMedia);
        // Retrieve url from request 
        if (config.supportsMMS && req.body.NumMedia && req.body.NumMedia > 0) {
            imageUrl = req.body["MediaUrl" + 1];
        } else {
            var urls = /(http|https):\/\/(\w+:{0,1}\w*@)?(\S+)(:[0-9]+)?(\/|\/([\w#!:.?+=&%@!\-\/]))?/.exec(req.body.Body);
            imageUrl = (urls && urls.length) ? urls[0] : null;
        }
 
        if (imageUrl) {
            resp.message('Thanks for the image! URL: ' + imageUrl);
 
            res.type('text/xml');
            return res.send(resp.toString()); 
        } else {
            resp.message('Oops! Try sending an image.');
 
            res.type('text/xml');
            return res.send(resp.toString());
        }
    } else {
        return res.send('Nice try imposter.');
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