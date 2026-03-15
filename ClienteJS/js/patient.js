
//GET ALL
function getAllpatients() {
    fetch("http://localhost:8080/patients")
    .then(response => {
        if (!response.ok) {
            throw new Error('Network response was not ok');
        }
        return response.json();
    })
        .then(data => {
            console.log(data)
            var tableBody = document.querySelector("#patientTable tbody");

            tableBody.innerHTML = "";

            data.forEach(patient => {
                let row = createPatientRow_LongVersion(patient);
                tableBody.appendChild(row);
            });
        })
        .catch(error => {
            console.error('Error fetching data:', error);
        });
}


// DELETE PATIENT
function deletePatient(button) {
    var row = button.parentNode.parentNode;
    var patientId = row.querySelector('td:first-child').innerText.trim();
    sendDeleteRequest(false);

    // Función para enviar la solicitud DELETE al servidor
    function sendDeleteRequest(confirmation) {
        // Delete the patient from the server
        fetch(`http://localhost:8080/patients/${patientId}?confirm=${confirmation}`, {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json'
            }
        })
            .then(response => {
                if (response.status === 204) {
                    // No content, successful deletion
                    console.log('Patient deleted successfully');
                    row.parentNode.removeChild(row);
                    document.getElementById("infoContainer").innerHTML = "";
                    return;
                }

                if (!response.ok) {
                    // Si la respuesta no es ok, manejar el error
                    if (response.status === 409) {
                        // Conflicto: el paciente tiene registros médicos
                        return response.text().then(eMessage => {
                            // Mostrar el mensaje de error al usuario y confirmar
                            let resp = confirm(eMessage);
                            if (resp) {
                                // Volvemos a llamar al delete pero con la confirmación
                                sendDeleteRequest(true);
                                throw new Error('Patient has medical records');
                            } else {
                                // El usuario canceló, lanzar un error
                                throw new Error('User cancelled operation');
                            }
                        });
                    } else {
                        // Otro tipo de error
                        throw new Error('Network response was not ok');
                    }
                }
                // Si la respuesta es ok, devolver el cuerpo de la respuesta como JSON
                return response.json();
            })
            .catch(error => {
                console.error('Error deleting patient:', error);
            });
    }
}



// ADD PATIENT
function addPatient(event) {
    event.preventDefault();

    // Get form values
    let patientName = document.getElementById("addpatientName").value;
    let patientDOB = document.getElementById("addpatientDOB").value;
    let patientPhone = document.getElementById("addpatientPhone").value;
    let patientUname = document.getElementById("addpatientUserName").value;
    let patientPwd = document.getElementById("addpatientPassword").value;


    // Prepare data for sending
    let patient = {
        id: 0,
        name: patientName,
        birthDate: patientDOB,
        phone: patientPhone,
        paid:0,
        userName: patientUname,
        password: patientPwd
    };

    // Send a fetch request to add the patient to the server
	fetch("http://localhost:8080/patients", {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(patient)
    })

    .then(response => {
        if (!response.ok) {
            // Si la respuesta no es ok, manejar el error
            if (response.status === 409) {
                // Conflicto: Username duplicated
                return response.text().then(eMessage => {
                    // Mostrar el mensaje de error al usuario
                    let resp = alert(eMessage);
                    throw new Error('Username duplicated');

                    })}
                    else {
                        // El usuario canceló, lanzar un error
                        throw new Error('User cancelled operation');
                    }
            }
        return response.json();
    })
        .then(data => {
            console.log('patient added successfully,index:', data);
            // Reset and close the modal after handling the form submission
            document.getElementById("addpatientForm").reset();
            $('#addpatientModal').modal('hide');
            //Add patient to HTML table
            // Get the table body
            var tableBody = document.querySelector("#patientTable tbody");
            patient.id=data;
            let row = createPatientRow_LongVersion(patient);
            // Append the row to the table body
            tableBody.appendChild(row);

        })
        .catch(error => {
            console.error('Error adding patient:', error);
        });

}

// UPDATE PATIENT
function updatePatient(event) {
    event.preventDefault();

    // Get form values
    let patientId = document.getElementById("updatepatientId").value;
    let patientName = document.getElementById("updatepatientName").value;
    let patientDOB = document.getElementById("updatepatientDOB").value;
    let patientPhone = document.getElementById("updatepatientPhone").value;

    // Prepare data for updating
    var patient = {
        id: patientId,
        name: patientName,
        birthDate:patientDOB,
        phone: patientPhone
    };

    // Send a fetch request to update the patient on the server
	fetch("http://localhost:8080/patients", {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(patient)
    })
    .then(response => {
        if (response.ok) {  //successful
            $('#updatepatientModal').modal('hide');
            //Modify patient in HTML table
            modifyPatientHTMLtable(patient);
        }else throw new Error('Network response was not ok');
    })
        .catch(error => {
            console.error('Error updating patient:', error);
        });

}

// Function to modify HTML patient table
function modifyPatientHTMLtable(data) {
    // Recorremos todas las filas de la tabla
    var rows = document.querySelectorAll("#patientTable tbody tr");
    for (var i = 0; i < rows.length; i++) {
        var row = rows[i];
        // Verificamos si el ID de la fila coincide con el ID proporcionado en data
        if (row.cells[0].textContent == data.id) {
            // Actualizamos los datos de la fila
            row.cells[1].textContent = data.name;
            row.cells[2].textContent = data.birthDate;
            row.cells[3].textContent = data.phone;
            break;
        }
    }
}

//Function to show the modal window for updating
function showUpdatePatientModalW(button) {
    let row = button.parentNode.parentNode;
    let cells = row.getElementsByTagName("td");

    // Obtener los datos de la fila seleccionada
    let patientId = cells[0].innerText;
    let patientName = cells[1].innerText;
    let patientDOB = cells[2].innerText;
    let patientPhone = cells[3].innerText;

    // Poblar el formulario de actualización con los datos de la fila seleccionada
    document.getElementById("updatepatientId").value = patientId;
    document.getElementById("updatepatientName").value = patientName;
    document.getElementById("updatepatientDOB").value = patientDOB;
    document.getElementById("updatepatientPhone").value = patientPhone;

    // Add event listener for update patient form submission
    var updatepatientForm = document.getElementById("updatepatientForm");
    updatepatientForm.addEventListener("submit", updatePatient);

    // Mostrar la ventana modal de actualización
    var updatepatientModal = new bootstrap.Modal(document.getElementById('updatepatientModal'));
    updatepatientModal.show();
}

//Function to add row to the table
function createPatientRow(patient) {
    var row = document.createElement("tr");
    row.innerHTML = "<td>" + patient.id + "</td>" +
        "<td>" + patient.name + "</td>" +
        "<td>" + patient.phone + "</td>" +
        "<td><button class='btn btn-info btn-sm'>Get Info</button></td>" +
        "<td><button class='btn btn-danger btn-sm'>Delete</button></td>" +
        "<td><button class='btn btn-primary btn-sm ms-2'>Update</button></td>";

    // Agregar event listeners a los botones
    row.querySelector(".btn-info").addEventListener("click", getInfo);
    row.querySelector(".btn-danger").addEventListener("click", deletePatient);
    row.querySelector(".btn-primary").addEventListener("click", showUpdatePatientModalW);

    return row;
}

function createPatientRow_LongVersion(patient) {
    // Crear elementos de fila y celdas
    let row = document.createElement("tr");
    let idCell = document.createElement("td");
    let nameCell = document.createElement("td");
    let dobCell = document.createElement("td");
    let phoneCell = document.createElement("td");
    let paidCell = document.createElement("td");
    let getInfoButtonCell = document.createElement("td");
    let deleteButtonCell = document.createElement("td");
    let updateButtonCell = document.createElement("td");

    // Crear textos para las celdas
    let idText = document.createTextNode(patient.id);
    let nameText = document.createTextNode(patient.name);
    let dobText = document.createTextNode(patient.birthDate);
    let phoneText = document.createTextNode(patient.phone);
    let paidText = document.createTextNode(patient.paid);

    // Añadir textos a las celdas correspondientes
    idCell.appendChild(idText);
    nameCell.appendChild(nameText);
    dobCell.appendChild(dobText);
    phoneCell.appendChild(phoneText);
    paidCell.appendChild(paidText);

    // Crear botones
    var getInfoButton = document.createElement("button");
    getInfoButton.className = "btn btn-info btn-sm";
    getInfoButton.textContent = "Get Info";

    var deleteButton = document.createElement("button");
    deleteButton.className = "btn btn-danger btn-sm";
    deleteButton.textContent = "Delete";

    var updateButton = document.createElement("button");
    updateButton.className = "btn btn-primary btn-sm ms-2";
    updateButton.textContent = "Update";

    // Agregar eventos a los botones con addEventListener
    getInfoButton.addEventListener("click", function () {
        getInfo(this);
    });

    deleteButton.addEventListener("click", function () {
        deletePatient(this);
    });

    updateButton.addEventListener("click", function () {
        showUpdatePatientModalW(this);
    });

    // Añadir botones a las celdas correspondientes
    getInfoButtonCell.appendChild(getInfoButton);
    deleteButtonCell.appendChild(deleteButton);
    updateButtonCell.appendChild(updateButton);

    // Añadir celdas a la fila
    row.appendChild(idCell);
    row.appendChild(nameCell);
    row.appendChild(dobCell);
    row.appendChild(phoneCell);
    row.appendChild(paidCell);
    row.appendChild(getInfoButtonCell);
    row.appendChild(deleteButtonCell);
    row.appendChild(updateButtonCell);

    return row;
}




// Call the function when the DOM is ready
document.addEventListener("DOMContentLoaded", function () {

    getAllpatients();
    var addButton = document.getElementById("addButton");
    addButton.addEventListener("click", function () {
        // Add event listener for add patient form submission
        var addpatientForm = document.getElementById("addpatientForm");
        addpatientForm.addEventListener("submit", addPatient);
        // Show the add patient modal
        var addpatientModal = new bootstrap.Modal(document.getElementById('addpatientModal'));
        addpatientModal.show();
    });

});