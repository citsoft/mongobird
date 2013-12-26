$(document).ready(function () {
	getBannerMsg();
});
var request;
var display;
var arrMsg = [];

function getBannerMsg(){
	$.ajax({
		url : "http://distr.citsoft.net/bannerMsg.do",
		dataType : "jsonp",
		jsonp: false,
		jsonpCallback: "callback",
		success:function(jsonData,textStatus){
			try{
				successGetMsg(jsonData);
			}catch(err){
				successGetMsg(defaultJson());
			}
		},
		error:function(xhr,textStatus, errorThrown){
			successGetMsg(defaultJson());
		}
	});
}

function successGetMsg(jsonData){
	clearBanner();
	var width = jsonData.width;
	var height = jsonData.height;
	var requestInterval = jsonData.requestInterval;
	var displayInterval = jsonData.displayInterval;
	for(var i in jsonData.data){	
		arrMsg.push(jsonData.data[i]);
	}
	setDisplay();
	request = setInterval(function() { getBannerMsg(); }, requestInterval);
	display = setInterval(function() { setDisplay(); }, displayInterval);
	$('#banner').css({width:width, height:height});
}

function setDisplay(){
	var length = arrMsg.length;
	var randomNum = Math.floor(Math.random() * length + 1)- 1;
	var msg = arrMsg[randomNum];
//	var title = msg.title;
	var url = msg.url;
	$('#banner').attr("src", url);
}

function clearBanner(){
	clearInterval(request);
	clearInterval(display);
	arrMsg = [];
}

function defaultJson(){
	var defaultJson = {
		    "id": "mongobird",
		    "width": 400,
		    "height": 40,
		    "requestInterval": 3600000,
		    "displayInterval": 600000,
		    "data": [
		        {
		            "title":"mongoextensions",
		            "url":"./mongobird/mongo-extensions.html" 
		        }
		    ]
		};
	return defaultJson;
}
