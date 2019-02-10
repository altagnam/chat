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
		}
	};
	xhttp.open("POST", "http://localhost:8080/user", true);
	xhttp.setRequestHeader("Content-Type", "application/json");
	xhttp.send(JSON.stringify({
	    'login': document.getElementById("login").value, 
	    'name': document.getElementById("name").value, 
	    'password': document.getElementById("password").value, 
	}));

	return false;	
}
