var currentuser; //userid of logged in user
var currentusername;

var loggedin = false; //true if a user is logged in

var creditcounter = 0; //tracks number of attempted orders

$(document).ready(function(){

    //switchtoMainPage(); 	//show header and main page templates

    switchtoShoppingCart();

    $.ajax({
	url: "/bookstore/rest/product_catalogue/recommended_books/0132350882",
	type: "GET", 
	contentType: "application/json",
	success: function(data){
	    console.log(data, "data and id to find!!!!!");
	    //buildRecommended(data, cartlist[y]);
	},
	error: function(error){
	    console.log("ERRPR", error);
	}
    }); 

    
});



function buildRecommended(data, recsFor, index){
    console.log("building recommended", data, recsFor, index);
    var carouselItem = document.createElement("div");
    
    carouselItem.className = "carousel-item";

    if (index === "0"){
	carouselItem.className += " active";
    }

    var text = document.createElement("p");
    text.innerHTML = "for " + recsFor.title;

    carouselItem.appendChild(text);

    var row = document.createElement("div");
    row.className = "row";

    for (x in data){

	var newid = recsFor.id + "_" + data[x].bookid;

	var topdiv = document.createElement("div");
	topdiv.id = "card_template";

	var card = document.createElement("div");
	card.className = "card";
	card.style = "width:12rem;height:14rem";

	var cardbod = document.createElement("div");
	cardbod.className = "card-body";

	var h5 = document.createElement("h5");
	h5.className = "card-title text-truncate";
	h5.innerHTML = data[x].title;

	var auth = document.createElement("p");
	auth.className = "card-text text-truncate";
	auth.innerHTML = data[x].author;

	var price = document.createElement("p");
	price.className = "card-text mt-3";
	price.innerHTML = "$" + data[x].price;

	var category = document.createElement("p");
	category.className = "card-text";
	category.innerHTML = data[x].category;

	var button = document.createElement("a");
	button.href = "#";
	button.className = "btn btn-secondary";
	button.innerHTML = "Add to cart";
	button.id = "btn_" + data[x].bookid;
	button.onclick = addToCartClick;


	cardbod.appendChild(h5);
	cardbod.appendChild(auth).appendChild(price).appendChild(category);
	cardbod.appendChild(button);
	card.appendChild(cardbod);
	topdiv.appendChild(card);
	

	row.appendChild(topdiv);

	//append all cards to row one by one
	
	carouselItem.appendChild(row);

    }
    
    $("#carousel-append").append(carouselItem);
    
}

function placeOrder(){
    //called in response to "Place order" button in account order

    var creditcard = $("#creditcard").val(); //grabs the value of the input box

    //only allow 5 login attempts
    if (creditcounter < 5){
	if (creditcard === ""){
	    alert("Please enter a valid credit card. " + (4-creditcounter) + " attempts remain");
	    creditcounter++;
	}
	else{
	    //use the process_card api
	    $.post("/bookstore/rest/order_processor/process_card/?credit_card=" + creditcard, function (data){
		if (data.msg === "Credit card transaction successful!"){
		    alert(data.msg); 
		    creditcounter = 0;
		    $.get("/bookstore/rest/order_processor/empty_cart", function (data,error){
		    	console.log(data,error);
		    });
		    
		    $.ajax({
		    	url: "/bookstore/rest/shopping_cart_service/empty_cart",
		    	type: "GET", 
		    	contentType: "application/json",
		    	success: function(data){
		    	    console.log(data.msg);
		    	},
		    	error: function(error){
		    	    console.log("ERRPR", error);
		    	}
		    });
		    
		    switchtoMainPage();
		}
		else{
		    alert(data.msg + " " + (4-creditcounter) + " attempts remain");
		    creditcounter++;
		}
	    });
	}
    }
    if (creditcounter == 5){
	alert("You are only allowed 5 attempts!");
	switchtoMainPage();
    }
}

function getCategories(){
    //fetch the categories from the server and append them to the DOM

    $.get( "/bookstore/rest/product_catalogue/categories" , function ( data ) {
	for (x in data){
	    let li = document.createElement("li");
	    li.className = "nav-item";

	    let a = document.createElement("a");
	    a.id = data[x];
	    a.className = "nav-link pl-0";
	    a.onclick = catClick;
	    a.href = "#";
	    a.innerHTML = data[x];

	    li.appendChild(a);
	    $("#categories-list").append(li);
	}
    });
}

function buildShoppingCart(){
    //get all the dynamic shopping cart content and append it to the shopping cart template

    var cartlist = new Array();
    
    $.get("/bookstore/rest/shopping_cart_service/show_cart", function (data){
	$("#inline").html("");

	for (let x in data.all_books){
	    let currentbook = data.all_books[x];
	    var divGroup = document.createElement("div");
	    divGroup.id = "shopdiv_" + currentbook.id;
	    divGroup.className="form-group w-100 row clearfix ml-4";

	    var button = document.createElement("button");
	    button.className = "btn btn-light";
	    button.id = "remove_" + currentbook.id;
	    button.innerHTML="Remove";
	    button.onclick=removeClick;
	    divGroup.appendChild(button);

	    var text = document.createTextNode(currentbook.title);
	    divGroup.appendChild(text);

	    var input = document.createElement("input");
	    input.id = "input_" + currentbook.id;
	    input.type="number";
	    input.value = currentbook.quantity;
	    input.oninput = updateClick; //when the value changes, update the back end

	    divGroup.appendChild(input);

	    $("#inline").append(divGroup);

	    cartlist.push({id: data.all_books[x].id, title: data.all_books[x].title});

	}

	for (let y in cartlist){
	    $.ajax({
		url: "/bookstore/rest/product_catalogue/recommended_books/" + cartlist[y].id,
		type: "GET", 
		contentType: "application/json",
		success: function(data){
		    console.log("about to build recommended");
		    buildRecommended(data, cartlist[y], y);
		},
		error: function(error){
		    console.log("ERRPR", error);
		}
	    }); 
	}

    });
}

function getAllBooks(){
    //fetch all books and append them to the main book area
    $("#bookitem").html("");

    //get all the books in the DB
    $.get( "/bookstore/rest/product_catalogue/books", function (data){
	populateBooks(data);
    });
}

function populateBooks(data){
    //append fetched books to visible DOM

    for (x in data){

	//Bunch of create html things
	var button = document.createElement("button");

	button.className = "btn btn-light";
	button.innerHTML = "Add to cart";
	button.id = "btn_" + data[x].bookid;
	button.onclick = addToCartClick;

	var div = document.createElement("div");
	div.id=data[x].bookid;
	div.className= "item col-sm-6 col-lg-4";

	var titletext = document.createElement("h4");
	titletext.className = "group inner list-group-item-heading mt-5";

	var a = document.createElement("a");
	a.href = "#";
	a.bookid = data[x].bookid;
	a.innerHTML = data[x].title;
	a.onclick = titleClick;

	titletext.appendChild(a);
	div.appendChild(titletext);


	var extraDiv = document.createElement("div");
	extraDiv.id = "extra_" + data[x].bookid;
	extraDiv.style = "display:none";
	
	var price = document.createElement("p");
	price.innerHTML = data[x].price;

	var author = document.createElement("p");
	author.innerHTML = data[x].author;

	extraDiv.appendChild(author);
	extraDiv.appendChild(price);
	
	div.appendChild(extraDiv);

	div.appendChild(button);

	$("#bookitem").append(div);
    }
}

function buildAccountOrder(){
    //create the dynamic content for the account order page and append it to the append order template

    var totalprice = 0;

    $.get("/bookstore/rest/shopping_cart_service/show_cart", function (data){
	$("#order-info").html("");

	for (x in data.all_books){
	    let currentbook = data.all_books[x];
	    var divGroup = document.createElement("div");
	    divGroup.id = "orderdiv_" + currentbook.id;
	    divGroup.className="w-100 row clearfix ml-4 mt-2";

	    var text = document.createTextNode(currentbook.title);
	    divGroup.appendChild(text);

	    var divGroup2 = document.createElement("div");
	    divGroup2.className="w-100 row clearfix ml-5";

	    var quant = document.createTextNode(currentbook.quantity + " x $" + currentbook.price);
	    quant.innerHTML = currentbook.quantity;
	    
	    divGroup2.appendChild(quant);

	    $("#order-info").append(divGroup);
	    $("#order-info").append(divGroup2);

	    totalprice += currentbook.price * currentbook.quantity;
	}

	var pricediv = document.createElement("div");
	pricediv.className = "ml-3 mt-3";
	pricediv.innerHTML = "<b> Total price: </b> $" + totalprice;

	$("#order-info").append(pricediv);
	
    });

    $.ajax({
	url: "/bookstore/rest/order_processor/user_detail/" + currentuser,
	type: "POST",
	contentType: "application/json",
	success: function(data){
	    fillUserInfoFields(data);
	},
	error: function (error){
	    console.log(error);
	},
    });

    
}

function fillUserInfoFields(data){
    //take fetched user data and show it on the screen
    let user = data[0];

    $("#order_user").html(user.userName);
    $("#order_first").html(user.firstName);
    $("#order_last").html(user.lastName);

    $("#order_ship_street").html(user.address[0].street);
    $("#order_ship_prov").html(user.address[0].province);
    $("#order_ship_country").html(user.address[0].country);;
    $("#order_ship_zip").html(user.address[0].zip);
    $("#order_ship_phone").html(user.address[0].phone);

    $("#order_bill_street").html(user.address[1].street); 
    $("#order_bill_prov").html(user.address[1].province);
    $("#order_bill_country").html(user.address[1].country);
    $("#order_bill_zip").html(user.address[1].zip);
    $("#order_bill_phone").html(user.address[1].phone);
}



function createAccount(){
    //called when the user clicks "Create account" button on account creation page
    var jsobj = new Object();
    var doajax = true; 
    
    //build javascipt object from input fields, later to be sent to server
    jsobj.userName = $("#create_user").val();
    jsobj.password = $("#create_pass").val();
    jsobj.passconfirm = $("#create_pass_confirm").val();
    jsobj.firstname = $("#create_first").val();
    jsobj.lastname = $("#create_last").val();

    jsobj.shipstreet = $("#ship_street").val();
    jsobj.shipprov = $("#ship_prov").val();
    jsobj.shipcountry = $("#ship_country").val();
    jsobj.shipzip = $("#ship_zip").val();
    jsobj.shipphone = $("#ship_phone").val();

    jsobj.billstreet = $("#bill_street").val();
    jsobj.billprov = $("#bill_prov").val();
    jsobj.billcountry = $("#bill_country").val();
    jsobj.billzip = $("#bill_zip").val();
    jsobj.billphone = $("#bill_phone").val();

    for (let x in jsobj){
	//make sure no fields are left blank
	if (jsobj[x] === ""){
	    alert ("Please ensure that no fields are left empty");
	    doajax = false;
	    break;
	}
    }

    // Do a bunch of input validation. if any of the tests fail, do not proceed
    if (jsobj.password !== jsobj.passconfirm){
	doajax = false;
	alert("You have entered two different passwords");
    }

    if (jsobj.shipzip.length < 6 || jsobj.billzip.length < 6){
	alert("Zip must be at least 6 characters in length");
	doajax = false;
	
    }
    if (jsobj.shipphone.length < 10 || jsobj.billphone.length < 10){
	alert ("Please enter a 10 digit telephone number");
	doajax = false;
    }

    if (jsobj.userName.length < 4){
	alert ("Username must be at least 4 characters long");
	doajax = false;
    }

    if (jsobj.password.length < 4){
	alert ("Password must be at least 4 characters long");
	doajax = false;
    }

    if (jsobj.shipstreet.length < 2 || jsobj.billstreet.length < 2){
	alert ("Street must be at least two characters long");
	doajax = false;	
    }

    if (jsobj.shipcountry.length < 2 || jsobj.billcountry.length < 2){
	alert ("Country must be at least two characters long");
	doajax = false;	
    }

    if (jsobj.shipprov.length < 2 || jsobj.billprov.length < 2){
	alert ("Province must be at least two characters long");
	doajax = false;	
    }
    
    if (doajax) {
	var userjson = {
	    "userName" : jsobj.userName,
	    "password" : jsobj.password,
	    "firstName" : jsobj.firstname,
	    "lastName" : jsobj.lastname,
	    "address" : [
		{
		    "street" : jsobj.shipstreet,
		    "province" : jsobj.shipprov,
		    "phone" : jsobj.shipphone,
		    "country" : jsobj.shipcountry,
		    "zip" : jsobj.shipzip,
		    "type" : "shipping"
		},
		{
		    "street" : jsobj.billstreet,
		    "province" : jsobj.billprov,
		    "phone" : jsobj.billphone,
		    "country" : jsobj.billcountry,
		    "zip" : jsobj.billzip,
		    "type" : "billing"
		}
	    ]
	};

	
	var stringify = JSON.stringify(userjson); 

	$.ajax({
	    url: "/bookstore/rest/order_processor/create_account",
	    type: "POST",
	    contentType: "application/json",
	    data: stringify,
	    success: function(data){
		if (data.msg === "username has already been taken"){
		    alert ("The username you have chosen already exits. Please choose a new one.");
		}
		else {
		    alert ("Account creation successful! Signing you in.");
		    signin(userjson);
		}
	    },
	    error: function (error){
		console.log(error);
	    },
	});
    }
}

function signin(userjson){
    //sign in a user
    var newuserjson = JSON.stringify({userName: userjson.userName, password: userjson.password});
    $.ajax({
	url:"/bookstore/rest/order_processor/signin",
	type:"POST",
	data: newuserjson,
	contentType: "application/json",
	success: function (data){
	    alert(data.msg);

	    if (data.msg !== "Sign in unsuccessful"){
		loggedin=true; //there is now someone logged in
		currentuser=data.user_id; //set current user
		currentusername = userjson.userName;
		console.log(userjson.userName);
		$("#login-window").modal('hide'); //get rid of modal pop-up
		switchtoAccountOrder();
	    }

	},
	error: function(error){
	    alert(error.msg);
	}
    });
}

function signout(){
    //log the user out if there is someone logged in
    if (currentuser){
	$.ajax({
	    url: "/bookstore/rest/order_processor/signout",
	    data: currentuser,
	    success : function (data){
		alert(data.msg);
		currentuser = "";
		currentusername = "";
		$("#logged-in-user").html("");
	    },
	    error: function (data){
		alert(data.msg);
	    }
	});
    }
    else {
	alert("nobody is logged in");
    }
}

function updateClick(){
    //called in response to value of quantity box in shopping cart changing
    var bookid = this.id.split("_")[1];
    var value = this.value;
    
    $.post("/bookstore/rest/shopping_cart_service/update_cart/" + bookid + "/" + value, function (data){
    });
}

function removeClick(){
    //called in response to "remove" button on shopping cart page
    var bookid = this.id.split("_")[1];

    if (window.confirm("Are you sure you want to remove this item?")) {
    	$.post("/bookstore/rest/shopping_cart_service/remove_from_cart/" + bookid, function (data){
	    $("#shopdiv_" + bookid).remove();
	});
    };
}

function titleClick(){
    //called in response to clicking book title. shows extra info
    $("#extra_" + this.bookid).toggle();
}

function addToCartClick(){
    //called in response to clicking "add to cart" button on main page
    let bookid = this.id.split("_")[1];

    $.post("/bookstore/rest/shopping_cart_service/add_2_cart/" + bookid, function (data){
	//$("#carousel-append").html("");
	switchtoShoppingCart();
    });

}

function catClick(catname){
    //called in response to clicking a category element on the main page
    $("#bookitem").html(""); //clear main book area before repopulating
    
    $.get("/bookstore/rest/product_catalogue/books/" + this.id, function(data){
	populateBooks(data);	
    });
}

function loginClick(){
    //handles the submit button on the modal login window

    //signin function likes uname and password formatted this way
    var userjson = {
	userName : $("#username").val(),
	password : $("#password").val()
    };

    //check if uname or pword is blank
    if (userjson.userName != "" && userjson.password != ""){
	
	signin(userjson);	
	
    }
    else{
	alert("Username or password left blank");
    }
}

function createClick(){
    switchtoCreateAccount();
    $("#login-window").modal('hide'); //get rid 
}

function checkoutClick(){
    //called in response to shopping cart page checkout button click 

    //if there is someone logged in, go to checkout page
    if (loggedin){
	switchtoAccountOrder();
    }
    else{
	//otherwise, prompt them to log in
	$("#login-window").modal('show');
    } 
}

function switchtoRecommendations(){
    wipePage();
    showHeader();
    showRecommendations();
}

function switchtoShoppingCart(){
    //load the html for the shopping cart page
    wipePage();
    showHeader();
    buildShoppingCart();
    //buildRecommended();
    showShoppingCart();
}

function switchtoMainPage(){
    //load the html for the main page
    wipePage();
    showHeader();
    getCategories();
    showBooks();
    getAllBooks();
}

function switchtoCreateAccount(){
    //load the html for the account creation page
    wipePage();
    showHeader();
    showAccountCreate();
}

function switchtoAccountOrder(){
    //load the html for the account order page
    wipePage();
    showHeader();
    buildAccountOrder();
    showAccountOrder();
}

function showShoppingCart(){
    //append the shopping cart template to the main page area
    $("#screen").append($("#shoppingcart_template")[0].innerHTML);
    $("#screen").append($("#template_recommendations")[0].innerHTML);
    console.log($("#template_recommendations")[0].innerHTML, "template recs");
   
}

function showAccountOrder(){
    //append account order template to the visible dom
    $("#screen").append($("#accountorder_template")[0].innerHTML);
}


function showBooks(){
    //copy main page template and append to visible page
    $("#screen").append($("#mainpage_template")[0].innerHTML);
}

function showHeader(){
    //copy header template and append to visible page
    $("#screen").append($("#header_template")[0].innerHTML);

    if (loggedin){
	$("#logged-in-user").html(currentusername); //append logged in user name to div
	//$("#recommendations").show();
    }
}

function showRecommendations(){
    $("#screen").append($("#recommendations_template")[0].innerHTML);
}

function showAccountCreate(){
    //copy account order template and append to visible div
    $("#screen").append($("#createaccount_template")[0].innerHTML);
}

function showCredit(){
    //toggle hidden div featuring credit card input field
    $("#creditdiv").show();
}

function wipePage(){
    //wipe visible page
    $("#screen")[0].innerHTML="";
}
