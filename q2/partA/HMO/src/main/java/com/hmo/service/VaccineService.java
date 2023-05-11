package com.hmo.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.hmo.model.Vaccine;
import com.hmo.repository.VaccineRepository;

//defining the business logic
@Service
public class VaccineService {
    @Autowired
    VaccineRepository vaccineRepository;

    public List<Vaccine> getAllMVaccine() {
        List<Vaccine> vaccines = new ArrayList<>();
        vaccineRepository.findAll().forEach(vaccine -> vaccines.add(vaccine));
        return vaccines;
    }

    public Vaccine getMemberById(String id) {
        return vaccineRepository.findById(id).get();
    }

    public void saveOrUpdate(Vaccine vaccine) {
        vaccineRepository.save(vaccine);
    }
}