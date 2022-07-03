package com.udd.lucene.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Document(indexName = "index_application_second")
public class Application {

    @Id
    @Field(type = FieldType.Keyword)
    private String id;

    @Field(type = FieldType.Text, fielddata = true, store = true, analyzer = "serbian", searchAnalyzer = "serbian")
    private String firstname;

    @Field(type = FieldType.Text, fielddata = true, store = true, analyzer = "serbian", searchAnalyzer = "serbian")
    private String lastname;

    @Field(type = FieldType.Text, fielddata = true, store = true, analyzer = "serbian", searchAnalyzer = "serbian")
    private String education;

    @Field(type = FieldType.Text, fielddata = true, store = true, analyzer = "serbian", searchAnalyzer = "serbian")
    private String content;

    @Field(type = FieldType.Text, fielddata = true, store = true, analyzer = "serbian", searchAnalyzer = "serbian")
    private String city;

    @Field(type = FieldType.Integer, store = true)
    private int timestamp;

    @Field(type = FieldType.Text, fielddata = true, store = true, analyzer = "serbian", searchAnalyzer = "serbian")
    private String filename;

    @GeoPointField
    private GeoPoint location;

}
