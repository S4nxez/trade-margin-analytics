document.getElementById("bCheck").addEventListener("click", function (event) {

    // Get username and password from the form
    var username = document.getElementById("username").value;
    var password = document.getElementById("password").value;

    getCredentials(username, password)

})

//Function to get the Credentials (with POST)
function getCredentials(u, p) {
// Obtener los datos del formulario o crear un objeto con los datos del estudiante
let cred = {
username: u,
password: p
};

// Realizar la solicitud POST
fetch("http://localhost:8080/login", {
method: 'POST',
headers: {
    'Content-Type': 'application/json'
},
body: JSON.stringify(cred) // Convertir datos a JSON y enviar en el cuerpo de la solicitud
})
.then(response => {
// Manejar la respuesta
if (response.ok) {
    // Si la respuesta HTTP fue exitosa, leemos el cuerpo de la respuesta
    return response.text(); // Leemos el cuerpo de la respuesta
} else {
    // Si la respuesta HTTP no fue exitosa, lanzamos un error
		if (response.status === 409) {
			return response.text().then(eMessage => {
				document.getElementById("WrongCred").innerHTML = eMessage;
				})}
		// Autenticación fallida
		else
    	throw new Error('Network response was not ok.');
}
})
.then(data => {
// Manejar el contenido de la respuesta
if (data == "true") {
    // Autenticación exitosa, abrimos la página principal
    window.location.href = "mainPage.html"; // Redirigir a main.html
    document.getElementById("WrongCred").innerHTML = "";
}
})
.catch(error => {
// Manejar errores de la solicitud

console.error('Error al realizar la solicitud:', error);
});
}


