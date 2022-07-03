package com.udd.lucene.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ResultDataApplication {

    private String firstName;
    private String lastName;
    private String education;
    private String filename;
    private String city;
    //private String location;
    private String highlight;
}
