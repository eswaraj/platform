$(document).ready(function(){
fetch_locationTypeId();
$.ajax({
  type: "GET",
  url:"http://dev.admin.eswaraj.com/eswaraj-web/ajax/pbtype/get",
  contentType: "application/json; charset=utf-8",
  dataType: "JSON",
  success: function(data){
  if(data.length ==0){
  $('#admin_type_data').html("<p>No Admin Type Data Found</p>");}
    var admin_type_data = "";
	for(var i=0;i<data.length;i++){
	admin_type_data += "<tr ><td>"+(i+1)+"</td><td id='admin_type_name"+data[i].id+"'>"+data[i].name+"</td><td id='admin_type_short_name"+data[i].id+"'>"+data[i].shortName+"</td><td id='admin_type_description"+data[i].id+"'>"+data[i].description+"</td><td id='admin_type_locationTypeId"+data[i].id+"'>"+data[i].locationTypeId+"</td><td><a class='btn blue' id='"+data[i].id+"' value='"+data[i].id+"' description='"+data[i].description+"' short_name='"+data[i].shortName+"' full_name='"+data[i].name+"' location_type_id ='"+data[i].locationTypeId+"' href='#add-admin-type' onclick='return runMyFunction(event);' data-toggle='modal'><i class='fa fa-pencil'></i></a></td></tr> ";
	}
	var prev_admin_type_data = 	$('#admin_type_data').html();					
	$('#admin_type_data').html(prev_admin_type_data+admin_type_data);							
  }
});
});

function add_update_admin_type(){

var post_data = {
  "id":$("#admin_type_id").val(),
   "shortName":$("#short_name").val(),
   "name":$("#full_name").val(),
   "description":$("#admin_description").val(),
   "locationTypeId":$("#location_type_id").val()
   
  };
 // alert(JSON.stringify(post_data));
$.ajax({
  type: "POST",
  url:"http://dev.admin.eswaraj.com/eswaraj-web/ajax/pbtype/save",
  data: JSON.stringify(post_data),
  contentType: "application/json; charset=utf-8",
  dataType: "JSON",
  success: function(data){
  var count = document.getElementById('admin_type_data').rows.length;
  //alert(count);
  if(post_data.id){
   
  $("#admin_type_name"+post_data.id).html($('#full_name').val());
  $("#admin_type_short_name"+post_data.id).html($('#short_name').val());
  $("#admin_type_description"+post_data.id).html($('#admin_description').val());
  $("#admin_type_locationTypeId"+post_data.id).html($('#location_type_id').val());
  } else {
  if(count ==0){ count =1;}
  var admin_type_data ="<tr><td>"+(count)+"</td><td id='admin_type_name"+data.id+"'>"+data.name+"</td><td id='admin_type_short_name"+data.id+"'>"+data.shortName+"</td><td id='admin_type_description"+data.id+"'>"+data.description+"</td><td id='admin_type_locationTypeId"+data.id+"'>"+data.locationTypeId+"</td><td><a id='"+data.id+"' value='"+data.id+"' description='"+data.description+"' short_name='"+data.shortName+"' location_type_id ='"+data.locationTypeId+"' full_name='"+data.name+"' class='btn blue' onclick='return runMyFunction(event);' href='#add-admin-type' data-toggle='modal'><i class='fa fa-pencil'></i></a></td></tr> ";
  var prev_admin_type_data = $('#admin_type_data').html();					
  $('#admin_type_data').html(prev_admin_type_data+admin_type_data);	}
  
  }
});
	
}

function fetch_locationTypeId(){
$.ajax({
  type: "GET",
  url:"http://dev.admin.eswaraj.com/eswaraj-web/ajax/locationtype/get",
  dataType: "JSON",
  success: function(data){
  var location_dd =$("#location_type_id").html();
  location_dd += "<option value='"+data.id+"'>"+data.name+"</option>"
  if(data.children.length>0){
    
	for(var i=0;i< data.children.length;i++){
	 location_dd += "<option value='"+data.children[i].id+"'>"+data.children[i].name+"</option>" ;
	}
	$("#location_type_id").html(location_dd);
  }
   
  }});
  
}
function runMyFunction(event){

	var target = event.target || event.srcElement;	
	$('#admin_type_id').val($('#'+target.id).attr('value'));
	$('#short_name').val($('#'+target.id).attr('short_name'));
	$('#full_name').val($('#'+target.id).attr('full_name'));	
	$('#admin_description').val($('#'+target.id).attr('description'));
	$("#ocation_type_id option[value='"+$('#'+target.id).attr('location_type_id')+"']").attr('selected', 'selected');
	//$("#location_type_id").val($('#'+target.id).attr('location_type_id'));
	return true;
	
}
