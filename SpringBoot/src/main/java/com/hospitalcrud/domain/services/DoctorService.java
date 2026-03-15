package com.hospitalcrud.domain.services;

import com.hospitalcrud.dao.model.Doctor;
import com.hospitalcrud.dao.repositories.DoctorRepository;
import com.hospitalcrud.domain.model.DoctorUI;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DoctorService {
    private DoctorRepository doctorRepository;

    public DoctorService(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
    }

    public List<DoctorUI> getAll() {

        List<Doctor> doctors= doctorRepository.getAll();
        List<DoctorUI> doctorUIs = doctors.stream()
                .map(doctor -> new DoctorUI(doctor.getDoctor_id(), doctor.getName()))
                .toList();
        return doctorUIs;
    }
}
