//GET INFO (patient medRecords)
function getInfo(button) {
    var row = button.parentNode.parentNode;
    var patientId = row.querySelector('td:first-child').innerText.trim(); // Obtiene el ID del estudiante
    var patientName = row.querySelector('td:nth-child(2)').innerText.trim(); // Obtiene el nombre del estudiante

    // Realiza una solicitud Fetch al servidor para obtener los medRecords del alumno
    fetch(`http://localhost:8080/patients/${patientId}/medRecords`)
    .then(response => {
        if (!response.ok) {
            throw new Error('Network response was not ok');
        }
        return response.json();
    })
        .then(data => {
            // Crea una tabla HTML para mostrar los medRecords
            console.log(data);
            var medRecordsTable = document.createElement("table");
            medRecordsTable.id = "medRecordsTable";
            // Asigna el patientId como un atributo de datos en la tabla
            medRecordsTable.setAttribute("data-patient-id", patientId);
            medRecordsTable.className = "table table-bordered table-condensed table-striped";
            medRecordsTable.innerHTML = `<thead><tr><th  colspan=4>${patientName}'s MedRecords</th><td colspan=2><button class='btn btn-info btn-sm ms-2' onclick='showAddMedRecordModalW(this)'>Add medRecord</button></td></tr>`
            medRecordsTable.innerHTML += "<tr><th>ID</th><th>Diagnosis</th><th>Date</th><th>Doctor</th><th>Medications</th><th>Action</th><th>Action</th></tr></thead><tbody></tbody>";

            // Obtén el cuerpo de la tabla
            var medRecordsTableBody = medRecordsTable.querySelector("tbody");

            // Agrega cada medRecord como una fila a la tabla
            data.forEach(medRecord => {
                let medRecordRow = createMedRecordRow(medRecord);
                // Agrega la fila al cuerpo de la tabla
                medRecordsTableBody.appendChild(medRecordRow);
            });

            // Agrega la tabla de medRecords al documento HTML
            var infoContainer = document.getElementById("infoContainer");
            infoContainer.innerHTML = ""; // Limpiar contenido anterior
            infoContainer.appendChild(medRecordsTable);
        })
        .catch(error => {
            console.error('Error fetching medRecords:', error);
        });
}

// DELETE MEDRECORD
function deleteApp(button) {
    var row = button.parentNode.parentNode;
    var appId = row.querySelector('td:first-child').innerText.trim(); // Obtiene el contenido del primer <td> de la fila

    // Delete the patient from the server
    fetch(`http://localhost:8080/patients/medRecords/${appId}`, {
        method: 'DELETE',
        headers: {
            'Content-Type': 'application/json'
        }
    })
        .then(response => {
            if (response.ok) {
                row.parentNode.removeChild(row);
                console.log('medRecord deleted successfully');
            } else{
                throw new Error('Network response was not ok');
            }
        })
        .catch(error => {
            console.error('Error deleting medRecord:', error);
        });
}

// UPDATE MEDRECORD
function updateMedRecord(event) {
    event.preventDefault();

    // Get form values

    var medRecordId = document.getElementById("updateMedRecordId").value;
    var medRecordDesc = document.getElementById("updateMedRecordDesc").value;
    var medRecordDate = document.getElementById("updateMedRecordDate").value;
    var medRecordIdPatient = document.getElementById("updateMedRecordIdPatient").value;
    var medRecordIdDoctor = document.getElementById("updateMedRecordIdDoctor").value;
    //Get all selected medications
    var selectedMeds = getSelectedMeds("updateMedRecordMeds")


    // Prepare data for updating
    var medRecord = {
        id: medRecordId,
        description: medRecordDesc,
        date: medRecordDate,
        idPatient: medRecordIdPatient,
        idDoctor: medRecordIdDoctor,
        medications: selectedMeds
    };

    // Send a fetch request to update the medRecord on the server
    fetch('http://localhost:8080/patients/medRecords', {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(medRecord)
    })
    .then(response => {
        if (response.ok) {
            console.log('medRecord updated successfully');
            $('#updateMedRecordModal').modal('hide');
            modifyMedRecordHTMLtable(medRecord);
        } else {
            throw new Error('Failed to update medRecord');
        }
    })
    .catch(error => {
        console.error('Error updating medRecord:', error);
    });
}

//Function to get the selected medications from the HTML select control
function getSelectedMeds(medsSelect) {
    medsSelectControl= document.getElementById(medsSelect)
    var selectedOptions = [];
    for (var i = 0; i < medsSelectControl.options.length; i++) {
        var option = medsSelectControl.options[i];
        if (option.selected) {
            selectedOptions.push(option.textContent);
        }
    }
    return selectedOptions;
}


//Function to modify the medRecord in the HTML table
function modifyMedRecordHTMLtable(data) {
    // Recorremos todas las filas de la tabla
    var rows = document.querySelectorAll("#medRecordsTable tbody tr");
    for (var i = 0; i < rows.length; i++) {
        var row = rows[i];
        // Verificamos si el ID de la fila coincide con el ID proporcionado en data
        if (row.cells[0].textContent == data.id) {
            // Actualizamos los datos de la fila
            row.cells[1].textContent = data.description;
            row.cells[2].textContent = data.date;
            row.cells[3].textContent = data.idDoctor;
            row.cells[4].textContent = getMedicationsString(data);
            break;
        }
    }
}
//Function to show the modal window for updating medRecord
function showUpdateMedRecordModalW(button) {
    var row = button.parentNode.parentNode;
    var cells = row.getElementsByTagName("td");

    // Obtener los datos de la fila seleccionada
    var appId = cells[0].innerText;
    var appDesc = cells[1].innerText;
    var appDate = cells[2].innerText;
    var appIdDoctor = cells[3].innerText;
    var table = document.getElementById("medRecordsTable");
    var appIdPatient = table.getAttribute("data-patient-id");


    // Poblar el formulario de actualización con los datos de la fila seleccionada
    document.getElementById("updateMedRecordId").value = appId;
    document.getElementById("updateMedRecordDesc").value = appDesc;
    document.getElementById("updateMedRecordDate").value = appDate;
    document.getElementById("updateMedRecordIdPatient").value = appIdPatient;

    //fillDoctorCombo('updateMedRecordIdDoctor');
    //document.getElementById("updateMedRecordIdDoctor").value = appIdDoctor;
    //Si se ponen las lineas de arriba, no selecciona el profesor adecuado porque fillDoctorCombo es asincrona y no ha
    //terminado de cargar. Hay que hacerlo de la siguiente forma:
    fillDoctorCombo('updateMedRecordIdDoctor', function () {
        // Esta función se ejecutará después de que el combo se haya llenado completamente
        document.getElementById("updateMedRecordIdDoctor").value = appIdDoctor;
    });

    fillMedicationsCombo('updateMedRecordMeds');
    //TODO: Añadir valores por defecto
    //document.getElementById("updateMedRecordIdMeds").value = //Que seleccione todas las que hay

        // Add event listener for update medRecord form submission
        var updateappForm = document.getElementById("updateMedRecordForm");
        updateappForm.addEventListener("submit", updateMedRecord);

    // Mostrar la ventana modal de actualización
    var updateMedRecordModal = new bootstrap.Modal(document.getElementById('updateMedRecordModal'));
    updateMedRecordModal.show();

}

// ADD MEDRECORD
function addMedRecord(event) {
    event.preventDefault();

    // Get form values
    var medRecordDesc = document.getElementById("addMedRecordDesc").value;
    var medRecordDate = document.getElementById("addMedRecordDate").value;
    // TODO añadir combo con profesores
    // Parsear la fecha en JavaScript
    var parsedDate = new Date(medRecordDate);
    // Formatear la fecha en el formato yyyy-MM-dd
    var formattedDate = parsedDate.toISOString().split('T')[0];
    var iddoctor = document.getElementById('addMedRecordDoctor').value;
    //Get all selected medications
    var selectedMeds = getSelectedMeds("addMedRecordMeds")

    //Get patient ID
    var table = document.getElementById("medRecordsTable");
    var patientId = table.getAttribute("data-patient-id");
    // Prepare data for sending
    var medRecord = {
        id: 0,
        description: medRecordDesc,
        date: formattedDate,
        idPatient: patientId,
        idDoctor: iddoctor,
        medications: selectedMeds
    };

    // Send a fetch request to add the patient to the server
    fetch("http://localhost:8080/patients/medRecords", {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(medRecord)
    })

    .then(response => {
        if (!response.ok) {
            throw new Error('Network response was not ok');
        }
        return response.json();
    })
        .then(data => {
            console.log('medRecord added successfully, index:', data);
            // Reset and close the modal after handling the form submission
            document.getElementById("addMedRecordForm").reset();
            $('#addMedRecordModal').modal('hide');
            //Add medRecord to HTML table
            // Get the table body
            var tableBody = document.querySelector("#medRecordsTable tbody");
            medRecord.id=data;
            let row = createMedRecordRow(medRecord);
            console.log("Valor de row:", row);
            // Append the row to the table body
            tableBody.appendChild(row);

        })
        .catch(error => {
            console.error('Error adding medRecord:', error);
        });

}

function showAddMedRecordModalW(event) {
    var addMedRecordModal = new bootstrap.Modal(document.getElementById('addMedRecordModal'));
    fillDoctorCombo('addMedRecordDoctor', function () { });
    fillMedicationsCombo('addMedRecordMeds');
        // Add event listener for add medRecord form submission
        var addappForm = document.getElementById("addMedRecordForm");
        addappForm.addEventListener("submit", addMedRecord);
    addMedRecordModal.show();
}

//fillDoctorCombo: callback es una función que se llama desde esta funcion, para asegurarnos que ha cargado el combo antes
function fillDoctorCombo(combo, callback) {

    fetch('http://localhost:8080/doctors')
    .then(response => {
        if (!response.ok) {
            throw new Error('Network response was not ok');
        }
        return response.json();
    })
        .then(data => {
            // Obtener la combobox de profesores
            let doctorSelect = document.getElementById(combo);

            // Limpiar cualquier opción previa
            doctorSelect.innerHTML = '';

            // Agregar una opción por cada profesor obtenido
            data.forEach(doctor => {
                let option = document.createElement('option');
                option.value = doctor.id;
                option.textContent = doctor.name;
                doctorSelect.appendChild(option);
            });
            callback();
        })
        .catch(error => console.error('Error when fetching doctors:', error));

}

function fillMedicationsCombo(combo) {

    let medsSelect = document.getElementById(combo);
    medsSelect.innerHTML = '';

    let option = document.createElement('option');
    option.textContent = "Ibuprofen";
    medsSelect.appendChild(option);
    option = document.createElement('option');
    option.textContent = "Tylenol";
    medsSelect.appendChild(option);
    option = document.createElement('option');
    option.textContent = "Penicilina";
    medsSelect.appendChild(option);
    option = document.createElement('option');
    option.textContent = "Insulin";
    medsSelect.appendChild(option);
    option = document.createElement('option');
    option.textContent = "Folic acid";
    medsSelect.appendChild(option);
}

//Function to add row to the medRecord table
function createMedRecordRow(medRecord) {
    var medRecordRow = document.createElement("tr");
    medRecordRow.innerHTML = "<td>" + medRecord.id + "</td>" +
        "<td>" + medRecord.description + "</td>" +
        "<td>" + medRecord.date + "</td>" +
        "<td>" + medRecord.idDoctor + "</td>" +
        "<td>" + getMedicationsString(medRecord) + "</td>" +
        "<td><button class='btn btn-danger btn-sm' onclick='deleteApp(this)'>Delete</button></td>" +
        "<td><button class='btn btn-primary btn-sm ms-2' onclick='showUpdateMedRecordModalW(this)'>Update</button></td>";
    return medRecordRow;
}

function getMedicationsString(medRecord) {
    var medicationsString = "";
    if (medRecord.medications !== null)
        for (var i = 0; i < medRecord.medications.length; i++) {
            medicationsString += medRecord.medications[i];
            if (i < medRecord.medications.length - 1) {
                medicationsString += ", ";
            }
        }
    return medicationsString;
}
