package com.udd.lucene.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AdvancedQueryApplication {

    private String firstnameField;
    private String firstnameValue;
    private String lastnameField;
    private String lastnameValue;
    private String educationField;
    private String educationValue;
    private String contentField;
    private String contentValue;
    private String operation;
}
