var	protocol = window.location.protocol;
var	path = window.location.host;
var	url = protocol + '//' + path;

function createUSer() {
	var xhttp = new XMLHttpRequest();
	xhttp.onreadystatechange = function() {
		if (this.readyState == 4 && this.status == 200) {
			var json = JSON.parse(this.responseText);
			if (json.id){
				var div = document.getElementById('msg');
				div.innerText = 'Usuário criado com sucesso!';
				div.className = 'alert alert-success';
			}
		}else if (this.readyState == 4 && this.status == 500){
			var json = JSON.parse(this.responseText);
			var div = document.getElementById('msg');
			div.innerText = json.message;
			div.className = 'alert alert-danger';
			
		}
	};
	
	xhttp.open("POST", url + "/user", true);
	xhttp.setRequestHeader("Content-Type", "application/json");
	xhttp.send(JSON.stringify({
	    'login': document.getElementById("login").value, 
	    'name': document.getElementById("name").value, 
	    'password': document.getElementById("password").value, 
	}));

	return false;	
}


function init(){	
	if (window.location.href.includes('erro')){
		var div = document.getElementById('msg-login');
		div.innerText = 'Usuário ou senha inválidos.';
		div.className = 'alert alert-danger';
	}
}