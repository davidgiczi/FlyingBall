/**
 * 
 **/
var min;
var sec;
var secCounter = 0;
var COLS = parseInt(document.getElementById("cols").value);
var run = setInterval(function sending() {sendAjaxRequest("flyBall")}, 500);
var runTime = setInterval(displayTime, 1000);
document.addEventListener("keydown", pressButton);

function pressButton(e) {

    e = e || window.event;

	if (e.keyCode === 38) {
	
       sendAjaxRequest("stepNorth");
	}
	if (e.keyCode === 40) {
	
       sendAjaxRequest("stepSouth");
	}
 else if (e.keyCode === 13) {
	
   		 alert("Pihi!?");
    } 
}



function sendAjaxRequest(req) {

    var xmlHTTP = new XMLHttpRequest();

    xmlHTTP.onreadystatechange = function () {

        if (xmlHTTP.readyState === 4 && xmlHTTP.status === 200) {

            var response = xmlHTTP.responseText;
				
				if(response.startsWith("ball")){
					
					displayBall(response);
					displayHomePlayer(response);
				}
				else if(response.startsWith("player")){
					
					displayPlayer(response);
				}
				else{
				
				removeImage("ball");
				clearInterval(runTime);
				clearInterval(run);		
				
				if(confirm(response + "\n" + getTimeMessage())){
					
				location.reload();
				}
				else{
				
				document.removeEventListener("keydown", pressButton);
				}
			}			

        }

    };

    var url = document.location.protocol + "//" + document.location.host +
            document.location.pathname + "/ajaxRequest?usereq=" + req;


    xmlHTTP.open("GET", url, true);
    xmlHTTP.send();


}

function displayBall(resp){
	
	var ballComponent = resp.split("_")[1];		
	removeImage("ball");
	var image = document.createElement("img");
	image.width = 55;
	image.height = 60;
	image.src ="img/ball.jpg";
	image.id = "ball";
	document.getElementById(ballComponent).appendChild(image);
	
}

function removeImage(name){
	var img = document.getElementById(name);
	img.parentNode.removeChild(img);
}

function displayPlayer(resp){

	var playerComponent = resp.split("_")[1];		
	var player = document.getElementById("player");
	player.parentNode.removeChild(player);
	var image = document.createElement("img");
	image.width = 55;
	image.height = 90;
	image.src ="img/Garfield.jpg";
	image.id = "player";
	document.getElementById(playerComponent).appendChild(image);
}


function displayHomePlayer(resp){
	
	var ballPosition = resp.split("_")[1];
	var x = parseInt(ballPosition) / COLS;
	var y = parseInt(ballPosition) % COLS;
	
	if(y === COLS - 1){
		
	removeImage("home_player");
	var image = document.createElement("img");
	image.width = 80;
	image.height = 90;
	image.src ="img/Odie.jpg";
	image.id = "home_player";
	document.getElementById(ballPosition).appendChild(image);
		
	}

	
}

function displayTime(){
		
		secCounter = secCounter + 1;
	    min = Math.floor(secCounter / 60);
		sec = Math.floor(secCounter % 60);
		
		document.getElementById("time").innerHTML = "<b>" + getTimeMessage() + "</b>";
		setSpeed();
	}
	
function getTimeMessage(){
	return 10 > sec ?  "[" + min + ":0" + sec + "]" : "[" + min + ":" + sec + "]";
}


function setSpeed(){
	
	if(secCounter == 60){
		clearInterval(run);
		run = setInterval(function sending() {sendAjaxRequest("flyBall")}, 400);
	}
	else if(secCounter == 120){
		clearInterval(run);
		run = setInterval(function sending() {sendAjaxRequest("flyBall")}, 300);
	}
	else if(secCounter == 180){
		clearInterval(run);
		run = setInterval(function sending() {sendAjaxRequest("flyBall")}, 200);
	}
	else if(secCounter == 240){
		clearInterval(run);
		run = setInterval(function sending() {sendAjaxRequest("flyBall")}, 100);
	}
	
	
}

