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
    private boolean firstnameIsPhrase;
    private String lastnameField;
    private String lastnameValue;
    private boolean lastnameIsPhrase;
    private String educationField;
    private String educationValue;
    private boolean educationIsPhrase;
    private String contentField;
    private String contentValue;
    private boolean contentIsPhrase;
    private String operation;

    public boolean getFirstnameIsPhrase() {
        return firstnameIsPhrase;
    }
    public boolean getLastnameIsPhrase() {
        return lastnameIsPhrase;
    }
    public boolean getEducationIsPhrase() {
        return educationIsPhrase;
    }
    public boolean getContentIsPhrase() {
        return contentIsPhrase;
    }
}
