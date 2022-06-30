package com.udd.lucene.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Document(indexName = "application")
public class Application {

    @Id
    @Field(type = FieldType.Keyword)
    private String id;

    @Field(type = FieldType.Text, store = true, analyzer = "serbian")
    private String firstname;

    @Field(type = FieldType.Text, store = true, analyzer = "serbian")
    private String lastname;

    @Field(type = FieldType.Text, store = true, analyzer = "serbian")
    private String education;

    @Field(type = FieldType.Text, store = true, analyzer = "serbian")
    private String content;

    @Field(type = FieldType.Text, store = true, analyzer = "serbian")
    private String filename;

    @GeoPointField
    private GeoPoint location;

}
