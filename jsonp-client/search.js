jQuery.fn.liveUpdate = function(table) {
  // Variation of http://ejohn.org/blog/jquery-livesearch/
  table = jQuery(table);

  if ( table.length ) {
    var rows = table.children().children('tr'),
    cache = rows.map(function(){
                       var string = "";
                       var tds = jQuery(this).children();
                       tds.each(function(i, td) {
                                  string += td.innerHTML.toLowerCase() + " ";
                                });
                       return string;
                     });

    this.keyup(filter).keyup();
    // .parents('form').submit(function(){ return false; });
  }

  return this;

  function filter() {
    var term = jQuery.trim( jQuery(this).val().toLowerCase() ), scores = [];

    if ( !term ) {
      rows.show();
    } else {
      rows.hide();

      cache.each(function(i){
                   var score = this.score(term);
                   if (score > 0) { scores.push([score, i]); }
                 });

      jQuery.each(scores.sort(function(a, b){return b[0] - a[0];}), function() {
                    jQuery(rows[ this[1] ]).show();
                  });
    }
  }
};

var aj = {
  ajaxSearch : function() {
    var searchVal = aj.searchInput.val();
    if (aj.searchVal == aj.searchInput.val()) return false;
    aj.searchVal = searchVal;
    if (searchVal.length == 0) {
      aj.clear();
    }
    if (searchVal.length < 3) return false;
    aj.searchStatus.empty();
    $("#search-field").css("background",
                           "url('images/loading.gif') no-repeat right");
    aj.searchStatus.fadeIn(500);
    $.ajax({
             //url: "http://mega.cs.umu.se:8080/umume-rest/search/" + searchVal,
             url: "http://localhost:9998/search/" + searchVal,
             type: "GET",
             dataType: "jsonp",
             cache: true,
             success: aj.dataLoaded,
             error: function() {
               alert("error");
             },
             complete: function() {
             }
           });
    return false;
  },

  failure : function () {
    alert("failure function()");
  },

  clear : function() {
    aj.searchOutput.fadeOut(1000);
    aj.refineLabel.fadeOut(1000);
  },

  clickPerson : function() {
    var uid = $(this).children('.uid').text();
    alert(uid);
  },

  dataLoaded : function(data) {
    aj.searchInput.css("background", "");
    if (data == null) {
      aj.searchStatus.empty();
      aj.searchStatus.append(document.createTextNode("Found nothing"));
      aj.clear();
      return false;
    }
    aj.searchStatus.fadeOut(1000);
    var persons = data;
//     if (!persons.length) {
//       persons = data.people;
//     }

    var output = aj.searchOutput;
    output.empty();
    output.show();
    var table = $("<table>");
    table.attr("id", "search-output-list");
    output.append(table);
    $.each(persons, function(i, person) {
             var tr = $("<tr>");
             tr.css("cursor", "pointer");
             tr.click(aj.clickPerson);
             var tdName = $("<td>");
             tdName.append(document.createTextNode(person.givenName + " " + person.familyName));
             tr.append(tdName);

             var tdUid = $("<td>");
             tdUid.attr("class", "uid");
             tdUid.append(document.createTextNode(person.uid));
             tr.append(tdUid);

             var tdType = $("<td>");
             var type = "student";
             if (person.employeeType) {
               type = person.employeeType;
             }
             tdType.append(document.createTextNode(type));
             tr.append(tdType);

             var tdOu = $("<td>");
             var ou = "";
             if (person.organizationalUnit) {
               ou = person.organizationalUnit;
             }
             tdOu.append(document.createTextNode(ou));
             tr.append(tdOu);

             table.append(tr);
           });

    // Add refine funcitonality
    aj.refine.attr("value", "");
    aj.refineLabel.show();
    aj.refine.liveUpdate(table);
    return false;
  },

  init: function() {
    aj.searchInput = $("#search-field");
    aj.searchStatus = $("<span>");
    aj.searchOutput = $("<div>");
    aj.searchOutput.attr("id", "searchOutput");
    aj.searchInput.parent().after(aj.searchStatus);
    aj.searchInput.parent().after(aj.searchOutput);
    aj.refineLabel = $('<label>');
    aj.refineLabel.append(document.createTextNode("Refine: "));
    aj.refineLabel.hide();
    aj.refine = $('<input>');
    aj.refineLabel.append(aj.refine);
    aj.searchInput.parent().after(aj.refineLabel);
    aj.searchInput.typeWatch({highlight:true, wait:400, callback: aj.ajaxSearch, captureLength: -1});
  }
};

$(document).ready(function() {
                    aj.init();
                    $('#search-field').focus();
                  });
