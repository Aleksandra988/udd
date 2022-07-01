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

});

function uploadData() {

    // Get form
    var form = $('#fileUploadForm')[0];

    var data = new FormData(form);

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
    var operation = $('select[name=operation]').val();
    var firstnameIsPhrase = $("#checkboxfirstname").is(':checked');
    var lastnameIsPhrase = $("#checkboxlastname").is(':checked');
    var educationIsPhrase = $("#checkboxeducation").is(':checked');
    var contentIsPhrase = $("#checkboxcontent").is(':checked')

    var data = JSON.stringify({"firstnameField":firstnameField, "firstnameValue":firstnameValue, "lastnameField":lastnameField, "lastnameValue":lastnameValue,
    "educationField":educationField, "educationValue":educationValue, "contentField":contentField, "contentValue":contentValue, "operation": operation,
    "firstnameIsPhrase":firstnameIsPhrase, "lastnameIsPhrase": lastnameIsPhrase, "educationIsPhrase":educationIsPhrase, "contentIsPhrase":contentIsPhrase});
   
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
            console.log("SUCCESS : ", data);
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