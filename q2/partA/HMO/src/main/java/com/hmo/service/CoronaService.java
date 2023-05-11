package com.hmo.service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.hmo.model.Corona;
import com.hmo.repository.CoronaRepository;

//defining the business logic
@Service
public class CoronaService {
    @Autowired
    CoronaRepository coronaRepository;

    public List<Corona> getAllCorona() {
        List<Corona> coronas = new ArrayList<Corona>();
        coronaRepository.findAll().forEach(corona -> coronas.add(corona));
        return coronas;
    }

    public Corona getCoronaById(String id) {
        return coronaRepository.findById(id).get();
    }

    public void saveOrUpdate(Corona corona) {
        coronaRepository.save(corona);
    }

    public int getActivePatientsCountByDate(Date date) {
        Date startDate;
        Date endDate;
        List<Corona> coronaList = getAllCorona();
        int count = 0;
        for (Corona corona : coronaList) {
            startDate = corona.getSickDate();
            endDate = corona.getRecoveryDate();
            if (!date.before(startDate) && !date.after(endDate)) {
                count++;
            }
        }
        return count;
    }
}