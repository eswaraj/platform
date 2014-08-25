/*******************Load Root Node ***********************************/
$(function(){
      $("#menu").load("../admin/sidebar_menu.html"); 
});

function fetch_pbtype(){
  var result;
  $.when($.ajax({
  type: "GET",
  url:"/ajax/pbtype/get",
  dataType: "JSON",
  success: function(data){
  //alert(JSON.stringify(data));
   result = data;
  }
  })).done(function( x ) {
console.log(result);
});
  
}

function fetch_ltype(){
  $.ajax({
  type: "GET",
  url:"/ajax/locationtype/get",
  dataType: "JSON",
  success: function(data){
  //console.log(JSON.stringify(data));
  return data;
  }
  });

}

function get_pbtypeForLocationType(pbtypes,selected_loc_typeid){
console.log(selected_loc_typeid+"line 33");
var result = new Array();
	for (var i=0; i < pbtypes.length; i++){
	 if(pbtypes[i].locationTypeId == selected_loc_typeid){
		var node = {'id':pbtypes[i].id,'shortName':pbtypes[i].shortName };
		result.push(node);
	 }
	} 
	return result;
}

$(document).ready(function(){

var root_node,new_node,sel;
var all_ltype = fetch_ltype();
//console.log(all_pbtype+"All pbtype ");

$("#search_person").autocomplete({
	source: "/ajax/person/search/name/",
	minLength: 3,
	response: function( event, ui ) {
	console.log(JSON.stringify(ui));
	var search_result_content = ""; 
	for(var i=0; i<ui.content.length;i++){
	 if(!ui.content[i].profilePhoto){
	  ui.content[i].profilePhoto = "../images/NoImg.gif" ;
	 }
	 search_result_content += "<tr><td><a id='person"+i+"' onclick='return runMyFunction(event);' email='"+ui.content[i].email+"' landlineNumber1='"+ui.content[i].landlineNumber1+"' landlineNumber2='"+ui.content[i].landlineNumber2+"' line1='"+ui.content[i].line1+"' line2='"+ui.content[i].line2+"' line3='"+ui.content[i].line3+"' value='"+ui.content[i].id+"' line2='"+ui.content[i].line2+"' postalCode='"+ui.content[i].postalCode+"' villageId='"+ui.content[i].villageId+"' wardId='"+ui.content[i].wardId+"' cityId='"+ui.content[i].cityId+"' districtId='"+ui.content[i].districtId+"' stateId='"+ui.content[i].stateId+"' countryId='"+ui.content[i].countryId+"'	 class='btn pink person' href='#assign_pbadmin' data-toggle='modal'>"+(i+1)+"- Name "+ui.content[i].name+"</a></td><td><img width='50px;' src='"+ui.content[i].profilePhoto+"'/></td></tr>";
	}
	$("#search_result").html(search_result_content);
	}
})
 ;


//get Root Node-India

$.ajax({
  type: "GET",
  url:"/ajax/location/getroot",
  dataType: "JSON",
  success: function(data){
  //console.log(JSON.stringify(data));
  var root_node = 
       {		'text'    	:data.name+" Type: "+data.locationTypeId,
				'id'  	 	:data.id,								   
				'li_attr':{'title':data.name,'loc_typeid':data.locationTypeId,'p_id':data.parentLocationId,'center_lat':data.latitude,'center_long':data.longitude,'boundaryFile':data.boundaryFile}  
	   };

	$('#js_tree').jstree({ 'core' : {
		"check_callback" : true,
		'data' :root_node ,
		"plugins" : [ "types","contextmenu"]}}).bind("select_node.jstree", function (e, data) {  
		
		var parent = $('#js_tree').jstree('get_selected');
			
		$('#node_title').val($('#'+parent).attr('title'));
							
		var selected_loc_typeid =  $('#'+parent).attr('loc_typeid');
		var result,all_pbtype;
		//console.log(selected_loc_typeid);
		$.when($.ajax({
		  type: "GET",
		  url:"/ajax/pbtype/get",
		  dataType: "JSON",
		  success: function(data){
		     result = data;
		  }
		  })).done(function( x ) {
		all_pbtype =  result;
		//console.log("from line 86"+all_pbtype);
		var pbtype_list = get_pbtypeForLocationType(all_pbtype,selected_loc_typeid);
		console.log(pbtype_list);
		 var btn_html = "";
		 var pbtype_list_content = "";
				   
		for(var i=0;i < pbtype_list.length; i++){
				   
		pbtype_list_content += "<option id='"+pbtype_list[i].id+"'>"+pbtype_list[i].shortName+"</option>";

		btn_html += "<tr><td><a id='current_pbadmin_btn"+i+"' onclick='return runMyFunction(event);' value='"+pbtype_list[i].id+"' class='btn pink current' href='#current_pbadmin' data-toggle='modal'>Current "+pbtype_list[i].shortName+"</a></td><td><a id='node_add_btn"+i+"' onclick='return runMyFunction(event);' value='"+pbtype_list[i].id+"' class='btn green allpbadmin' href='#pb-list' data-toggle='modal'>All "+pbtype_list[i].shortName+"</a></td></tr>";
					
		}
			   
		$('#pbtype_list').html(pbtype_list_content);
        $('#add_child_btn').html(btn_html);		
		
				
		});
		
				
		if(!$('#'+parent).hasClass('jstree-open') && $('#'+parent).closest("li").children("ul").length ==0){
		//console.log("Dummy Node created");	
		new_node = {'text':'fake','id':'fake_node'};
		$('#js_tree').jstree(true).create_node(parent, new_node);
			
}
}).bind("open_node.jstree",function(e,data){
   
    $("#js_tree").jstree("delete_node", $('#fake_node'));
	if($('#'+selected_node).closest("li").children("ul").length ==0){
		
	var selected_node = data.node.id;
	var new_node;
	var sel;
	$.ajax({
		  type: "GET",
		  url:"/ajax/location/getchild/"+selected_node,
		  contentType: "application/json; charset=utf-8",
		  dataType: "JSON",
		  success: function(data){
		       if(data.length ==0){alert("No Children found");}		   			   
			   for(var i=0; i< data.length;i++){
			   	  new_node = {'text':data[i].name+" Type :"+data[i].locationTypeId,'id':data[i].id,'li_attr':{'title':data[i].name,'loc_typeid':data[i].locationTypeId,'p_id':data[i].parentLocationId,'center_lat':data[i].latitude,'center_long':data[i].longitude,'boundaryFile':data[i].boundaryFile}};
                  sel = $('#js_tree').jstree(true).create_node(selected_node, new_node);
				  
				}
			  ;
			
		  }
		});

	
	$("#js_tree").jstree("open_node", selected_node);
	}

});    
	    
	
   $('#js_tree').jstree("select_node",root_node.id);	
	
    }
});

});



/*******************Add a new Person***********************************/


function new_person(){

var post_data = {
  
   "name":$("#new_person_name").val(),
   "biodata":$("#new_person_biodata").val(), 
   "dob":$("#new_person_dob").val(),
   "gender":$("#new_person_gender").val(),
   "profilePhoto":$("#new_person_photo").val(),
   "email":$("#new_person_email").val(),
    "landlineNumber1":$("#new_person_ll1").val(),
    "landlineNumber2":$("#new_person_ll2").val(),
    "mobileNumber1":$("#new_person_mobile1").val(),
    "mobileNumber2":$("#new_person_mobile2").val(),
    "personAddress": {
           "id":'',     
           "line1":$("#new_person_lin1").val(),
           "line2":$("#new_person_line2").val(),
           "line3":$("#new_person_line3").val(),
           "postalCode":$("#new_person_postal").val(),
           "villageId":$("#village-list").val(),
           "wardId":$("#ward-list").val(),
           "cityId":$("#city-list").val(),
           "districtId":$("#district-list").val(),
           "stateId":$("#state-list").val(),
           "countryId":$("#country-list").val()
          }
  };
$.ajax({
  type: "POST",
  url:"/ajax/person/save",
  data: JSON.stringify(post_data),
  contentType: "application/json; charset=utf-8",
  dataType: "JSON",
  success: function(data){
  alert("Person Added"+data.name);
  $(".close").trigger("click");
	console.log(JSON.stringify(data, null, 4));

  }
});

}


/*******************Add a new Child Node***********************************/


function add_pbadmin(){

var selected_node =  $('#js_tree').jstree('get_selected');
var post_data = {
    "politicalBodyTypeId":$("#pbtype_list").children(":selected").attr("id"),
    "locationId":selected_node[0],
    "personId": $("#pbadmin_personId").val(),
    "partyId": $("#party_list").children(":selected").attr("id"),
    "email": $("#pbadmin_email").val(),
    "landLine1": $("#pbadmin_llandline1").val(),
    "landLine2": $("#pbadmin_llandline2").val(),
    "mobile1": $("#pbadmin_mobile1").val(),
    "mobile2": $("#pbadmin_mobile2").val(),
    "startDate": $("#pbadmin_startdate").val(), //date as milliseonds
    "endDate": $("#pbadmin_enddate").val(), //date as milliseonds
    "officeAddressDto": {
           
           "line1":$("#pbadmin_oaLine1").val(),
           "line2":$("#pbadmin_oaLine2").val(),
           "line3":$("#pbadmin_oaLine3").val(),
           "postalCode":$("#pbadmin_oapostal").val(),
           "villageId":$("#pbadmin_ovillage-list").val(),
           "wardId":$("#pbadmin_oward-list").val(),
           "cityId":$("#pbadmin_ocity-list").val(),
           "districtId":$("#pbadmin_odistrict-list").val(),
           "stateId":$("#pbadmin_ostate-list").val(),
           "countryId":$("#pbadmin_ocountry-list").val()
          },
      "homeAddressDto": {
               
           "line1":$("#pbadmin__haLine1").val(),
           "line2":$("#pbadmin__haLine2").val(),
           "line3":$("#pbadmin__haLine3").val(),
           "postalCode":$("#pbadmin_hapostal").val(),
           "villageId":$("#pbadmin_hvillage-list").val(),
           "wardId":$("#pbadmin_hward-list").val(),
           "cityId":$("#pbadmin_hcity-list").val(),
           "districtId":$("#pbadmin_hdistrict-list").val(),
           "stateId":$("#pbadmin_hstate-list").val(),
           "countryId":$("#pbadmin_hcountry-list").val()
          }

    }
$.ajax({
  type: "POST",
  url:"/ajax/pbadmin/save",
  data: JSON.stringify(post_data),
  contentType: "application/json; charset=utf-8",
  dataType: "JSON",
  success: function(data){
  console.log(JSON.stringify(data, null, 4));
  alert("PbAdmin Added"+data.personId);
  }
});



}


$(document).ready(function(){
	

$('#node_add_btn0').on('click',function(){
alert($(even.target).val());;

})
	

});


/*******************Update Selected node***********************************/

function update_selected_node(){

var selected_node =  $('#js_tree').jstree('get_selected');

var post_data = {
   "id":selected_node[0],
   "name":$("#node_title").val(),
   "locationTypeId":$('#'+selected_node[0]).attr('loc_typeid'),  
   "latitude":$('#node_lat').val(),
   "longitude":$('#node_long').val()
  };
  
  if($('#'+selected_node[0]).attr('p_id') != 'null'){
	post_data.parentLocationId = $('#'+selected_node[0]).attr('p_id');
  }
$.ajax({
  type: "POST",
  url:"/ajax/location/save",
  data: JSON.stringify(post_data),
  contentType: "application/json; charset=utf-8",
  dataType: "JSON",
  success: function(data){
  //alert(JSON.stringify(data, null, 4));
  $('#js_tree').jstree('set_text',data.id, data.name+data.locationTypeId);
  
  }
});
}

function runMyFunction(event){
   
	var target = event.target || event.srcElement;
	var pbtypeId = $('#'+target.id).attr('value');
	var selected_node =  $('#js_tree').jstree('get_selected');
	var locationId = selected_node[0];
	
	if($('#'+target.id).hasClass('current')){
					
		$.ajax({
		  type: "GET",
		  url:"/ajax/pbadmin/getcurrent/"+locationId+"/"+pbtypeId,
		  dataType: "JSON",
		  success: function(data){
		   //alert(JSON.stringify(data));	
		   if(data){
		    $("#current_pbadmin_content").html("<tr><td>Pb Admin ID "+data.id+"</td><tr><td>PersonID "+data.personId+"</td></tr>");
		   }else {$("#current_pbadmin_content").html("No Political Admin found for this location.");} 
		  }
		  });
		
	}
	
	if($('#'+target.id).hasClass('allpbadmin')){
					
		$.ajax({
		  type: "GET",
		  url:"/ajax/pbadmin/get/"+locationId+"/"+pbtypeId,
		  dataType: "JSON",
		  success: function(data){
		   //alert(JSON.stringify(data));	
		   if(data){
			var all_pbadmin = "";
			for(var i=0; i< data.length; i++){
				all_pbadmin += "<tr><td>Pb Admin ID "+data[i].id+"</td><tr><td>PersonID "+data[i].personId+"</td></tr>"
			}
		    $("#all_pbadmin_content").html(all_pbadmin);
		   }else {$("#all_pbadmin_content").html("No Political Admin found for this location.");} 
		  }
		  });
		
	}
	
	if($('#'+target.id).hasClass('person')){
		$.ajax({
		  type: "GET",
		  url:"/ajax/party/getall",
		  dataType: "JSON",
		  success: function(data){
		   //alert(JSON.stringify(data));	
		   
		   var party_list = "";
		   for(var i=0; i<data.length;i++)
		   {
			  party_list += "<option id='"+data[i].id+"'>"+data[i].name+"</option>";
		   
		   }		   
		    $("#party_list").html(party_list);		    	  
		  }
		  
		  });
	
	/**********************Grab values from the selected person*********************/
	
		$('#pbadmin_locationId').val(locationId);
		$('#pbadmin_personId').val($('#'+target.id).attr('value'));
		$('#pbadmin_email').val($('#'+target.id).attr('email'));
		$('#pbadmin_llandline1').val($('#'+target.id).attr('landlineNumber1'));
		$('#pbadmin_llandline2').val($('#'+target.id).attr('landlineNumber1'));
		$('#pbadmin_mobile1').val($('#'+target.id).attr('mobileNumber1'));
		$('#pbadmin_mobile2').val($('#'+target.id).attr('mobileNumber2'));
		$('#pbadmin__haLine1').val($('#'+target.id).attr('line1'));
		$('#pbadmin__haLine2').val($('#'+target.id).attr('line2'));
		$('#pbadmin__haLine3').val($('#'+target.id).attr('line3'));
		$('#pbadmin_hapostal').val($('#'+target.id).attr('postalCode'));
	
	}
	
	$('#new_node_loc_typeid').val($('#'+target.id).attr('value'));
	return true;
	console.log($('#new_node_loc_typeid').attr('value'));
}
