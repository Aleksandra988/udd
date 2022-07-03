package com.udd.service;

import com.udd.lucene.model.EducationLevel;
import com.udd.repository.EducationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EducationService {

    @Autowired
    private EducationRepository educationRepository;

    public List<EducationLevel> getAll(){
        return educationRepository.findAll();
    }
}
