$(function(){
      $("#menu").load("../admin/sidebar_menu.html"); 
});

$(document).ready(function(){

$.ajax({
  type: "GET",
  url:"/ajax/party/getall",
  contentType: "application/json; charset=utf-8",
  dataType: "JSON",
  success: function(data){
 
    var party_data = "";
	for(var i=0;i<data.length;i++){
	party_data += "<tr ><td>"+(i+1)+"</td><td id='party_name"+data[i].id+"'>"+data[i].name+"</td><td id='party_short_name"+data[i].id+"'>"+data[i].shortName+"<td><a class='btn pink' id='"+data[i].id+"' value='"+data[i].id+"' short_name='"+data[i].shortName+"' full_name='"+data[i].name+"' href='#add-party' onclick='return runMyFunction(event);' data-toggle='modal'></a></td></tr> ";
	}
	var prev_party_data = 	$('#party_data').html();					
	$('#party_data').html(prev_party_data+party_data);							
  }
});
});

function add_update_party(){

var post_data = {
   "id":$("#party_id").val(),
   "shortName":$("#short_name").val(),
   "name":$("#full_name").val()
  };
$.ajax({
  type: "POST",
  url:"/ajax/party/save",
  data: JSON.stringify(post_data),
  contentType: "application/json; charset=utf-8",
  dataType: "JSON",
  success: function(data){
  var count = document.getElementById('party_data').rows.length;
  //alert(count);
  if(post_data.id){
   
  $("#party_name"+post_data.id).html($('#full_name').val());
  $("#party_short_name"+post_data.id).html($('#short_name').val());
  
  } else {
  var party_data ="<tr><td>"+(count)+"</td><td id='party_name"+data.id+"'>"+data.name+"</td><td id='party_short_name"+data.id+"'>"+data.shortName+"<td><a id='"+data.id+"' value='"+data.id+"' short_name='"+data.shortName+"' full_name='"+data.name+"' class='btn pink' onclick='return runMyFunction(event);' href='#add-party' data-toggle='modal'></a></td></tr> ";
  var prev_party_data = $('#party_data').html();					
  $('#party_data').html(prev_party_data+party_data);	}
  
  }
});
	
}

function runMyFunction(event){
   
	var target = event.target || event.srcElement;	
	$('#party_id').val($('#'+target.id).attr('value'));
	$('#short_name').val($('#'+target.id).attr('short_name'));
	$('#full_name').val($('#'+target.id).attr('full_name'));	
	return true;
	
}
