const server = require('server');
var Sentiment = require('sentiment');
var sentiment = new Sentiment();
var fs = require('fs');
var mysql = require ('mysql');
var prompt = require('prompt');

var keepgoing = true;

prompt.start();


prompt.get(['username', 'password'], function (err, result){
    console.log(result.username, result.password);
    
    var connection = mysql.createConnection({
	user: result.username,
	password : result.password,
	database: 'csi5380'
	
    });

    connection.connect(function(err) {
	if (err) {
	    console.error('error connecting: ' + err.stack);
	    return;
	}


	
	console.log('connected as id ' + connection.threadId);



	prompt.get(['bookid', 'title', 'price', 'author', 'category', 'filename'], function (err, result){
	    var promptresult = result;

	    var sentimentScore;
	    fs.readFile(result.filename, 'utf8', function (err, data){

		var result = sentiment.analyze(data);
		sentimentScore = result.comparative;
		console.log(sentimentScore);

		var sql = "INSERT INTO book (bookid, title, price, author, category, sentiment_score) VALUES ('" +
		    promptresult.bookid + "', '" +
		    promptresult.title + "', '" +
		    promptresult.price + "', '" +
		    promptresult.author + "', '" +
		    promptresult.category + "', '" +
		    sentimentScore + "')";
		console.log(sql);
		
		connection.query(sql, function (err, result) {
		    if (err) throw err;
		    console.log("1 record inserted!");
		});
	    });
	    


	});
    });
});



server (ctx => 'Hello world');










