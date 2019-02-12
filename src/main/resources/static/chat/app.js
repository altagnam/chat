var stompClient = null;
var	protocol = window.location.protocol;
var	path = window.location.host;
var	url = protocol + '//' + path;

/**
 * Recupera todos os usuários cadastrados
 * @returns
 */
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
	xmlHttp.open("GET", url + '/user/all', true); // true for asynchronous 
	xmlHttp.send(null);
}


/**
 * Recupera informações do usuário logado
 * @returns
 */
function getInfoUserSelf(){
	var xmlHttp = new XMLHttpRequest();
	xmlHttp.onreadystatechange = function() { 
	if (xmlHttp.readyState == 4 && xmlHttp.status == 200){
		var json = JSON.parse(xmlHttp.responseText);		
		$("#my_user_talk_name").text(json.name);
		$("#my_user_talk_login").val(json.login);
		}
	}
	xmlHttp.open("GET", url + '/user/self', true); //
	xmlHttp.send(null);
} 


/**
 * Inicia a conversa com um usuário
 * @param row
 * @returns
 */
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
	if (stompClient.connected){
		$("#send").prop("disabled", false);
	}else{
		alert('Você não esta conectado');
	}
}


/**
 *  Indica se o usuário esta conectado ao websocket 
 * @param connected
 * @returns
 */
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


/**
 * Conecta o usuário ao websocket
 * @returns
 */
function connect() {
    var socket = new SockJS('/gs-guide-websocket');
    stompClient = Stomp.over(socket);
    $("#send").prop("disabled", false);
    stompClient.connect({}, function (frame) {
    	setConnected(true);
        stompClient.subscribe('/user/queue/greetings', function (greeting) {
            showMessage(JSON.parse(greeting.body));
        });
        
        stompClient.subscribe('/topic/greetings', function (greeting) {
        	updateStatusUsers(JSON.parse(greeting.body));
        });
    });
}


/**
 * Desconecta o usuário do websocket
 * @returns
 */
function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
        $("#send").prop("disabled", true);
    }
    setConnected(false);
    document.getElementById('send').disabled;
}


/**
 * Envia a mensagem para o destinatário 
 * @returns
 */
function sendMessage() {
    stompClient.send("/app/message", {}, JSON.stringify({
    		'from':   document.getElementById("my_user_talk_login").value,
    		'fromName':   document.getElementById("my_user_talk_name").innerText, 
    		'to':   document.getElementById("user_talk_login").value,
    		'toName':   document.getElementById("user_talk_name").value, 
    		'text': document.getElementById("message").value
    	}));
    
    document.getElementById("message").value = '';
}


/**
 * Verifica se um determinado número é menor do que 10.
 * @param number
 * @returns
 */
function isLessTen (number){
	return number < 10 ? true : false;
}


/**
 * Formata uma data para o padrao dd/mm/yyyy hh:mm
 * @param date
 * @returns
 */
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


/**
 * Adiciona uma nova linha a tabela de mensagem.
 * Pode ser uma texto enviado pelo usuário como também o status
 * @param message
 * @param isStatus
 * @returns
 */
function addRowMessage (message, isStatus){
	var table = document.getElementById('greetings');
	var tr = document.createElement("TR");
	var td = document.createElement("TD");	
	var span =  document.createElement("SPAN");	
	
	if (!isStatus){			
		span.innerText =  message.text;
		if (message.from != document.getElementById('my_user_talk_login').value){
			span.style.float = 'right';
		}		
	}
	
	td.appendChild(crateMessage(message, isStatus));
	if (!isStatus){
		td.appendChild(span);
	}
	tr.appendChild(td);
	table.appendChild(tr);
	table.scrollIntoView(false);
	table.scrollIntoView({block: "end"});
	table.scrollIntoView({behavior: "smooth", block: "end", inline: "nearest"});
	
}


/**
 * Cria a mensagem que será adicionada a tabela
 * @param message
 * @param isStatus
 * @returns
 */
function crateMessage (message, isStatus){
	var p =  document.createElement("P");
	p.classList.add('font-weight-bold');
	p.style.fontSize = 'small';
	if (!isStatus){
		var date = new Date(message.date);
		p.innerText = formatterDate(date);		
		if (message.from != document.getElementById('my_user_talk_login').value){
			p.innerText +=  ' ' + message.fromName + ' diz:';		
		}else{
			p.innerText += ' - Mensagem para ' + message.toName;	
		}
	}else{
		if (message.status == 0){
			p.classList.add('text-success');
		}else{
			p.classList.add('text-danger');
		}
		p.style.textAlign = 'center';	
		p.innerText = message.text;	
	}
	
	return p;
}

/**
 * Exibe as mensagens recebidas para o usuário
 * @param message
 * @returns
 */
function showMessage(message) {
	addRowMessage(message, false);
}

/**
 * Exibe os status dos usuários para o usuário
 * @param message
 * @returns
 */
function updateStatusUsers(message){
	addRowMessage(message, true);
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