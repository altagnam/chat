var stompClient = null;


function getAllUser (){
	var xmlHttp = new XMLHttpRequest();
	xmlHttp.onreadystatechange = function() { 
	if (xmlHttp.readyState == 4 && xmlHttp.status == 200)
		var json = JSON.parse(xmlHttp.responseText);		
		for(var k in json) {
		   var user = json[k];
		   $("#table_users").append('<tr style=\'cursor: pointer;\' onclick=\'startTalk(this)\'><td id=' + user.login + '>' + user.name + '<span style=\'float: right;\'></span><span style=\'float: right;  width: 20px; text-align: center;\'></span></td></tr>');
		}
	}
	xmlHttp.open("GET", 'http://localhost:8080/user/all', true); // true for asynchronous 
	xmlHttp.send(null);
}


function getInfoUserSelf(){
	var xmlHttp = new XMLHttpRequest();
	xmlHttp.onreadystatechange = function() { 
	if (xmlHttp.readyState == 4 && xmlHttp.status == 200){
		var json = JSON.parse(xmlHttp.responseText);		
		$("#my_user_talk_name").text(json.name);
		$("#my_user_talk_login").val(json.login);
		}
	}
	xmlHttp.open("GET", 'http://localhost:8080/user/self', true); //
	xmlHttp.send(null);
} 



function startTalk (row){
	var table = row.parentElement;
	for (var i = 0; i < table.childElementCount; i++){
		table.children[i].className = '';
	}
	
	var td = row.children[0];
	var name = td.innerText;
	var login = td.id;
	document.getElementById('user_talk_login').value = login;
	document.getElementById('user_talk_name').value = name;
	row.className = 'table-active';
	 $("#send").prop("disabled", false);
}


function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) {
        $("#conversation").show();
    }
    else {
        $("#conversation").hide();
    }
    $("#greetings").html("");
}

function connect() {
    var socket = new SockJS('/gs-guide-websocket');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
    	setConnected(true);
        stompClient.subscribe('/user/queue/greetings', function (greeting) {
            showGreeting(JSON.parse(greeting.body));
        });
        
        stompClient.subscribe('/topic/greetings', function (greeting) {
        	updateStatusUsers(JSON.parse(greeting.body));
        });
    });
}


function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}


function sendMessage() {
    stompClient.send("/app/message", {}, JSON.stringify({
    		'to':   document.getElementById("user_talk_login").value,
    		'toName':   document.getElementById("user_talk_name").value, 
    		'text': document.getElementById("message").value
    	}));
    
    document.getElementById("message").value = '';
}

function isLessTen (number){
	return number < 10 ? true : false;
}


function formatterDate (date){
	 var datePattern = isLessTen(date.getDate()) ? '0' + date.getDate() : date.getDate();
	 datePattern += '/';
	 datePattern += isLessTen(date.getMonth() + 1) ? '0' + (date.getMonth() + 1) : (date.getMonth() + 1);
	 datePattern += '/' + date.getFullYear();
	 datePattern += ' ';
	 datePattern +=  isLessTen(date.getHours()) ? '0' + date.getHours() : date.getHours();   
	 datePattern += ':'; 
	 datePattern += isLessTen(date.getMinutes()) ? '0' + date.getMinutes() : date.getMinutes();
	 return datePattern;
}

function showGreeting(message) {
	var table = document.getElementById('greetings');
	var tr = document.createElement("TR");
	var td = document.createElement("TD");
	
	var p =  document.createElement("P");
	var span =  document.createElement("SPAN");
	
	p.className  = 'font-weight-bold';
	p.style.fontSize = 'small';
	
	var date = new Date(message.date);
	p.innerText = formatterDate(date);
	
	if (message.from != document.getElementById('my_user_talk_login').value){
		p.innerText +=  ' ' + message.fromName + ' diz:';		
	}else{
		p.innerText += ' - Mensagem para ' + message.toName;	
	}//
	
	span.innerText =  message.text;
	if (message.from != document.getElementById('my_user_talk_login').value){
		span.style.float = 'right';
	}
	td.appendChild(p);
	td.appendChild(span);
	
	tr.appendChild(td);
	table.appendChild(tr);
	table.scrollIntoView(false);
	table.scrollIntoView({block: "end"});
	table.scrollIntoView({behavior: "smooth", block: "end", inline: "nearest"});
}


function updateStatusUsers(message){
	var table = document.getElementById('greetings');
	var tr = document.createElement("TR");
	var td = document.createElement("TD");
	
	var p =  document.createElement("P");
	var span =  document.createElement("SPAN");
	
	p.classList.add('font-weight-bold');
	if (message.status == 0){
		p.classList.add('text-success');
	}else{
		p.classList.add('text-danger');
	}
	
	p.style.fontSize = 'small';
	p.style.textAlign = 'center';	
	p.innerText = message.text;	
	
	td.appendChild(p);
	td.appendChild(span);
	
	tr.appendChild(td);
	table.appendChild(tr);
	table.scrollIntoView(false);
	table.scrollIntoView({block: "end"});
	table.scrollIntoView({behavior: "smooth", block: "end", inline: "nearest"});
}


function showMessagesNotRead(message){
	$("#greetings").append("<tr><td>" + message.text + "<span></span></td></tr>");
}

$(function () {
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    $( "#connect" ).click(function() { connect(); });
    $( "#disconnect" ).click(function() { disconnect(); });
    $( "#send" ).click(function() { sendMessage(); });
});

getInfoUserSelf();
getAllUser();
connect();