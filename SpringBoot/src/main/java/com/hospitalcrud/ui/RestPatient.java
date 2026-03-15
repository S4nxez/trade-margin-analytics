package com.hospitalcrud.ui;

import com.hospitalcrud.domain.model.PatientUI;
import com.hospitalcrud.domain.services.PatientService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class RestPatient {
    private final PatientService patientService;

    public RestPatient(PatientService patientService){
        this.patientService=patientService;
    }

    @CrossOrigin(origins = "http://127.0.0.1:5500")
    @GetMapping("/patients")
    public List<PatientUI> get(){
        return patientService.get();
    }

    @CrossOrigin(origins = "http://127.0.0.1:5500")
    @PostMapping("/patients")
    public int add(@RequestBody  PatientUI patientUI){
        return patientService.add(patientUI);
    }

    @CrossOrigin(origins = "http://127.0.0.1:5500/")
    @DeleteMapping("/patients/{idDelete}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int idDelete, @RequestParam(required=false) boolean confirm){
        patientService.delete(idDelete, true);
    }

    @CrossOrigin(origins = "http://127.0.0.1:5500")
   @PutMapping("/patients")
   @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@RequestBody  PatientUI patientUI){
        patientService.update(patientUI);
    }
}
