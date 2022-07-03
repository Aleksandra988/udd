package com.udd.controller;

import com.udd.lucene.model.EducationLevel;
import com.udd.service.EducationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class EducationController {

    @Autowired
    private EducationService educationService;

    @GetMapping(value="/getAllEducation"/*, consumes="application/json"*/)
    public List<EducationLevel> getAll(){
        return educationService.getAll();
    }
}
