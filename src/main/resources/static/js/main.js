$(document).ready(function () {

    $("#btnSubmit").click(function (event) {

        //stop submit the form, we will post it manually.
        event.preventDefault();

        uploadData();

    });
    
    $("#btnSubmitLuceneTermQuery").click(function (event) {

        //stop submit the form, we will post it manually.
        event.preventDefault();

        searchLuceneTermQuery();

    });

    $("#btnSubmitGeo").click(function (event) {

            //stop submit the form, we will post it manually.
            event.preventDefault();

            luceneGeoSearch();

        });

//        $("#btnSubmitStatistic").click(function (event) {
//
//                    //stop submit the form, we will post it manually.
//                    event.preventDefault();
//
//                    getStatistic();
//
//                });
                data1 = {
                                 "aggs":{
                                   "citymax":{
                                         "terms":{
                                             "field":"city"
                                         }
                                    }
                                 }
                                }

                 $.ajax({
                                type: "POST",
                                url: "http://localhost:9200/index_application_second/_search",
                                headers:{
                                   'Content-Type': 'application/json'},
                                data: JSON.stringify(data1),
                                success: function (dataMaxCity) {
                                      var maxCity = dataMaxCity.aggregations.citymax.buckets[0].key
                                    $('#result3').empty();
                                    for(index = 0; index < dataMaxCity.hits.hits.length; index++){
                                            var result = dataMaxCity.hits.hits[index]
                                            $('#result3').append('<li>' + "Applicant " + index+1 + '</li>');
                                            $('#result3').append('<li>' + "firstname" + ': ' + result._source.firstname + '</li>');
                                            $('#result3').append('<li>' + "lastname" + ': ' + result._source.lastname + '</li>');
                                            $('#result3').append('<li>' + "education" + ': ' + result._source.education + '</li>');
                                            $('#result3').append('<li>' + "city" + ': ' + result._source.city + '</li>');
                                            $('#result3').append('<li>' + "time" + ': ' + result._source.timestamp  + 'h' + '</li>');
                                             $('#result3').append('<li></li>');

                                    }

                    //                $("#btnSubmitGeo").prop("disabled", false);

                                },
                                error: function (e) {
//                                	$('#result2').empty();
                                    $("#result3").text(e.responseText);
                                    console.log("ERROR : ", e);
//                                    $("#btnSubmitStatistic").prop("disabled", false);

                                }
                            });


                            $.ajax({
                                                            type: "GET",
                                                            url: "http://localhost:8080/getAllEducation",
                                                            success: function (allEducation) {
                                                            console.log(allEducation)

                                                            this_select_content = '';
                                                            for(var i=0; i < allEducation.length; i++){
                                                                var this_select_content = this_select_content + '<option value="' + allEducation[i].name + '">' + allEducation[i].name + '</option>';
                                                            }
                                                            $("#education").empty().append(this_select_content);
//                                                            var myOptions = { val1 : allEducation[0].name,val2 : allEducation[1].name,
//                                                                val3 : allEducation[2].name, val4 : allEducation[3].name};
//                                                                $.each(myOptions, function(val, text) {
//                                                                   $('#education').append(new Option(text, val));
//                                                                });


                                                            },
                                                            error: function (e) {
                                                                console.log("ERROR : ", e);

                                                            }
                                                        });

//                                                        data1 = {
//                                                                         "aggs":{
//                                                                           "citymax":{
//                                                                                 "terms":{
//                                                                                     "field":"city"
//                                                                                 }
//                                                                            }
//                                                                         }
//                                                                        }
                                                               data2={
                                                                       "query":{
                                                                            "exists":{
                                                                                 "field":"city"
                                                                            }
                                                                       },
                                                                      "aggs":{
                                                                        "timemax":{
                                                                            "terms":{
                                                                                "field":"timestamp",
                                                                                "size":1
                                                                            }
                                                                        }
                                                                     }
                                                               }
//                                                                $('#result2').empty();
                                                                $.ajax({
                                                                                            type: "POST",
                                                                                            url: "http://localhost:9200/index_application_second/_search",
                                                                                            headers:{
                                                                                               'Content-Type': 'application/json'},
                                                                                            data: JSON.stringify(data2),
                                                                                            success: function (dataMaxDate) {
                                                                                            console.log(dataMaxDate.aggregations.timemax.buckets[0].key)
                                                        //                                        var parts = dataMaxDate.aggregations.timemax.buckets[0].key_as_string.split("T")
                                                        //
                                                        //                                        var time = parts[1].split(".")
                                                        //
                                                        //                                        finalMaxDateTime = parts[0] + " " + time[0]
                                                                                                    finalMaxDateTime = dataMaxDate.aggregations.timemax.buckets[0].key + "h"
                                                                                                $('#result2').append('<li>' + "Time of day with the most applicants: " + ': ' + finalMaxDateTime + '</li>');
                                                                                            },
                                                                                            error: function (e) {
                                                                                            	$('#result2').empty();
                                                                                                $("#result2").text(e.responseText);
                                                                                                console.log("ERROR : ", e);
                                                                                                $("#btnSubmitStatistic").prop("disabled", false);

                                                                                            }
                                                                                        });

                                                                $.ajax({
                                                                        type: "POST",
                                                                        url: "http://localhost:9200/index_application_second/_search",
                                                                        headers:{
                                                                           'Content-Type': 'application/json'},
                                                                        data: JSON.stringify(data1),
                                                                        success: function (dataMaxCity) {
                                                                              var maxCity = dataMaxCity.aggregations.citymax.buckets[0].key
                                                        //                      console.log(dataMaxCity)
                                                        //                    console.log(maxCity)
                                                                            console.log(dataMaxCity.aggregations.citymax.buckets[0])

                                                                            $('#result2').append('<li>' + "City with the most applicants: " + ': ' + maxCity + '</li>');

                                                            //                $("#btnSubmitGeo").prop("disabled", false);

                                                                        },
                                                                        error: function (e) {
                                                                        	$('#result2').empty();
                                                                            $("#result2").text(e.responseText);
                                                                            console.log("ERROR : ", e);
                                                                            $("#btnSubmitStatistic").prop("disabled", false);

                                                                        }
                                                                    });

});

function uploadData() {

    // Get form
    var form = $('#fileUploadForm')[0];

    var data = new FormData(form);

    console.log($('select[name=education]').val())


    $("#btnSubmit").prop("disabled", true);

    $.ajax({
        type: "POST",
        enctype: 'multipart/form-data',
        url: "/index/add",
        data: data,
        processData: false, //prevent jQuery from automatically transforming the data into a query string
        contentType: false,
        cache: false,
        timeout: 600000,
        success: function (data) {
        	$('#result').empty();
            $("#result").text(data);
            console.log("SUCCESS : ", data);
            $("#btnSubmit").prop("disabled", false);

        },
        error: function (e) {
        	$('#result').empty();
            $("#result").text(e.responseText);
            console.log("ERROR : ", e);
            $("#btnSubmit").prop("disabled", false);

        }
    });

}

function searchLuceneTermQuery() {

    var firstnameField = $('#luceneTermQuery input[name=firstnameField]').val();
    var firstnameValue = $('#luceneTermQuery input[name=firstnameValue]').val();
    var lastnameField = $('#luceneTermQuery input[name=lastnameField]').val();
    var lastnameValue = $('#luceneTermQuery input[name=lastnameValue]').val();
    var educationField = $('#luceneTermQuery input[name=educationField]').val();
    var educationValue = $('#luceneTermQuery input[name=educationValue]').val();
    var contentField = $('#luceneTermQuery input[name=contentField]').val();
    var contentValue = $('#luceneTermQuery input[name=contentValue]').val();
//    var operation = $('select[name=operation]').val();
    var firstnameIsPhrase = $("#checkboxfirstname").is(':checked');
    var lastnameIsPhrase = $("#checkboxlastname").is(':checked');
    var educationIsPhrase = $("#checkboxeducation").is(':checked');
    var contentIsPhrase = $("#checkboxcontent").is(':checked')
    var firstnameOperation = $('select[name=firstnameOperation]').val();
    var lastnameOperation = $('select[name=lastnameOperation]').val();
    var educationOperation = $('select[name=educationOperation]').val();
    var contentOperation = $('select[name=contentOperation]').val();

    console.log(educationValue)

    var data = JSON.stringify({"firstnameField":firstnameField, "firstnameValue":firstnameValue, "lastnameField":lastnameField, "lastnameValue":lastnameValue,
    "educationField":educationField, "educationValue":educationValue, "contentField":contentField, "contentValue":contentValue, /*"operation": operation,*/
    "firstnameIsPhrase":firstnameIsPhrase, "lastnameIsPhrase": lastnameIsPhrase, "educationIsPhrase":educationIsPhrase, "contentIsPhrase":contentIsPhrase,
    "firstnameOperation":firstnameOperation, "lastnameOperation":lastnameOperation, "educationOperation":educationOperation, "contentOperation":contentOperation});
   
    $("#btnSubmitLuceneTermQuery").prop("disabled", true);

    $.ajax({
        type: "POST",
        url: "/search/boolean",
        data: data,
        contentType: 'application/json',
        success: function (data) {

        	$('#result').empty();
            for(index = 0; index < data.length; index++){
                var result = data[index]
                $.each(result, function(key, value) {
                  $('#result').append('<li>' + key + ': ' + value + '</li>');
                });
            }
            console.log("SUCCESS : ", data);
            $("#btnSubmitLuceneTermQuery").prop("disabled", false);

        },
        error: function (e) {
        	$('#result').empty();
            $("#result").text(e.responseText);
            console.log("ERROR : ", e);
            $("#btnSubmitLuceneTermQuery").prop("disabled", false);

        }
    });

}

function luceneGeoSearch() {

    var city = $('#luceneGeoSearch input[name=city]').val();
    var country = $('#luceneGeoSearch input[name=country]').val();
    var distance = $('#luceneGeoSearch input[name=distance]').val();

    var data = JSON.stringify({"city":city, "country":country, "distance":distance});

    $("#btnSubmitGeo").prop("disabled", true);

    $.ajax({
        type: "POST",
        url: "/geoSearch",
        data: data,
        contentType: 'application/json',
        success: function (data) {

        	$('#result').empty();
            for(index = 0; index < data.length; index++){
                var result = data[index]
                $.each(result, function(key, value) {
                  $('#result').append('<li>' + key + ': ' + value + '</li>');
                });
            }
//            console.log("SUCCESS : ", data);
            $("#btnSubmitGeo").prop("disabled", false);

        },
        error: function (e) {
        	$('#result').empty();
            $("#result").text(e.responseText);
            console.log("ERROR : ", e);
            $("#btnSubmitGeo").prop("disabled", false);

        }
    });

}

//function getStatistic(){
//    data1 = {
//                 "aggs":{
//                   "citymax":{
//                         "terms":{
//                             "field":"city"
//                         }
//                    }
//                 }
//                }
//       data2={
//               "query":{
//                    "exists":{
//                         "field":"city"
//                    }
//               },
//              "aggs":{
//                "timemax":{
//                    "terms":{
//                        "field":"timestamp",
//                        "size":1
//                    }
//                }
//             }
//       }
//        $('#result2').empty();
//        $.ajax({
//                                    type: "POST",
//                                    url: "http://localhost:9200/index_application_second/_search",
//                                    headers:{
//                                       'Content-Type': 'application/json'},
//                                    data: JSON.stringify(data2),
//                                    success: function (dataMaxDate) {
//                                    console.log(dataMaxDate.aggregations.timemax.buckets[0].key)
////                                        var parts = dataMaxDate.aggregations.timemax.buckets[0].key_as_string.split("T")
////
////                                        var time = parts[1].split(".")
////
////                                        finalMaxDateTime = parts[0] + " " + time[0]
//                                            finalMaxDateTime = dataMaxDate.aggregations.timemax.buckets[0].key + "h"
//                                        $('#result2').append('<li>' + "Time of day with the most applicants: " + ': ' + finalMaxDateTime + '</li>');
//                                    },
//                                    error: function (e) {
//                                    	$('#result2').empty();
//                                        $("#result2").text(e.responseText);
//                                        console.log("ERROR : ", e);
//                                        $("#btnSubmitStatistic").prop("disabled", false);
//
//                                    }
//                                });
//
//        $.ajax({
//                type: "POST",
//                url: "http://localhost:9200/index_application_second/_search",
//                headers:{
//                   'Content-Type': 'application/json'},
//                data: JSON.stringify(data1),
//                success: function (dataMaxCity) {
//                      var maxCity = dataMaxCity.aggregations.citymax.buckets[0].key
////                      console.log(dataMaxCity)
////                    console.log(maxCity)
//                    console.log(dataMaxCity.aggregations.citymax.buckets[0])
//
//                    $('#result2').append('<li>' + "City with the most applicants: " + ': ' + maxCity + '</li>');
//
//    //                $("#btnSubmitGeo").prop("disabled", false);
//
//                },
//                error: function (e) {
//                	$('#result2').empty();
//                    $("#result2").text(e.responseText);
//                    console.log("ERROR : ", e);
//                    $("#btnSubmitStatistic").prop("disabled", false);
//
//                }
//            });
//
//}