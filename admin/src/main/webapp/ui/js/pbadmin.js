var all_pbtype = new Object();

$(function(){
	$("#menu").load("../ui/sidebar_menu.html"); 
});

function addLocation(node)
{
	loc_hash[node.id] = node.name;
	//console.log(node.name);
	if(node.children)
	{
		//console.log("Error:"+node.name);
		for(var i=0; i<node.children.length; i++)
		{
			addLocation(node.children[i]);
		}
	}
}

function fetch_pbtype(){
	$.ajax({
		type: "GET",
		url:"/ajax/pbtype/get",
		dataType: "JSON",
		success: function(data){
			//console.log(JSON.stringify(data));
			all_pbtype = data;
		}
	});
}

function fetch_ltype(){
	$.ajax({
		type: "GET",
	url:"/ajax/locationtype/get",
	dataType: "JSON",
	success: function(data){
		//console.log(JSON.stringify(data));
		addLocation(data);
		return data;
	}
	});
}

function fetch_plist(){
	//Populate party list
	$.ajax({
		type: "GET",
		url:"/ajax/party/getall",
		dataType: "JSON",
		success: function(data){
			//alert(JSON.stringify(data));	

			var party_list = "";
			for(var i=0; i<data.length;i++) {
				party_list += "<option id='"+data[i].id+"'>"+data[i].name+"</option>";

			}		   
			$("#party_list").html(party_list);		    	  
		}

	});
}

function get_pbtypeForLocationType(pbtypes,selected_loc_typeid){
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
	window.hash = new Object();
	window.loc_hash = new Object();
	var all_ltype = fetch_ltype();
	fetch_pbtype();
	fetch_plist();

	$("#person_search").autocomplete({		
		source: "/ajax/person/search/name/",
		minLength: 3,
		response: function( event, ui ) {
			data = ui.content;
			$('#users tbody').html("");
			for(var i=0; i<data.length; i++) {
				$('#users tbody').append("<tr>" +
					"<td><img src='" + data[i].profilePhoto + "' class='thumb'></td>" +
					"<td>" + data[i].id + "</td>" +
					"<td>" + data[i].name + "</td>" +
					"<td>" + "<button class='btn btn-primary blue' type='button' onClick='setPNodeId(event);' pid='" + data[i].id + "' id='P" + data[i].id + "' title='" + data[i].name + "'>Select Person</button>" + "</td>" +
					"</tr>" 
					);
			}
		}
	});

	$("#searchButtonP").click(function() {		
		var s = $('#person_search').val();
		$.ajax({
			type: "GET",
			url:"/ajax/person/search/name/"+s,
			data: "",
			contentType: "application/json; charset=utf-8",
			dataType: "JSON",
			success: function(data){
				$('#users tbody').html("");
				for(var i=0; i<data.length; i++) {
					$('#users tbody').append("<tr>" +
						"<td><img src='" + data[i].profilePhoto + "' class='thumb'></td>" +
						"<td>" + data[i].id + "</td>" +
						"<td>" + data[i].name + "</td>" +
						"<td>" + "<button class='btn btn-primary blue' type='button' onClick='setPNodeId(event);' pid='" + data[i].id + "' id='P" + data[i].id + "' title='" + data[i].name + "'>Select Person</button>" + "</td>" +
						"</tr>" 
						); 
				}
			}
		});
	});

	$("#node_search").autocomplete({		
		source: "/ajax/location/search/name/",
		minLength: 3,
		response: function( event, ui ) {
			data = ui.content;
			$('#nodes tbody').html("");
			for(var i=0; i<data.length; i++) {
				$('#nodes tbody').append("<tr>" +
					"<td>" + data[i].id + "</td>" +
					"<td>" + loc_hash[data[i].locationTypeId] + "</td>" + //change name to type when real API is available
					"<td>" + data[i].name + "</td>" +
					"<td>" + "<button class='btn btn-primary blue' type='button' onClick='setNNodeId(event);' pid='" + data[i].id + "' tid='" + data[i].locationTypeId + "' id='N" + data[i].id + "' title='" + data[i].name + "'>Select Location</button>" + "</td>" +
					"</tr>" 
					);
			}
		}
	});

	$("#searchButtonN").click(function() {		
		var s = $('#node_search').val();
		$.ajax({
			type: "GET",
			url:"/ajax/location/search/name/"+s,
			data: "",
			contentType: "application/json; charset=utf-8",
			dataType: "JSON",
			success: function(data){
				$('#nodes tbody').html("");
				for(var i=0; i<data.length; i++) {
					$('#nodes tbody').append("<tr>" +
						"<td>" + data[i].id + "</td>" +
						"<td>" + loc_hash[data[i].locationTypeId] + "</td>" + //change name to type when real API is available
						"<td>" + data[i].name + "</td>" +
						"<td>" + "<button class='btn btn-primary blue' type='button' onClick='setNNodeId(event);' pid='" + data[i].id + "' tid='" + data[i].locationTypeId + "' id='N" + data[i].id + "' title='" + data[i].name + "'>Select Location</button>" + "</td>" +
						"</tr>" 
						);//Change data[i].id to data[i].locationTypeId for tid when real API is available
				}
			}
		});
	});


	//get Root Node-India

	$.ajax({
		type: "GET",
		url:"/ajax/location/getroot",
		dataType: "JSON",
		success: function(data){
			//console.log(JSON.stringify(data));
			var root_node = 
	{		'text'    	:data.name+" Type: "+"Country",
		'id'  	 	:data.id,								   
		'li_attr'       :{'title':data.name,'loc_typeid':data.locationTypeId,'p_id':data.parentLocationId,'center_lat':data.latitude,'center_long':data.longitude,'boundaryFile':data.boundaryFile}  
	};

	$('#js_tree').jstree({ 'core' : {
		"check_callback" : true,
		'data' :root_node ,
		"plugins" : [ "types","contextmenu"]}}).bind("select_node.jstree", function (e, data) {  

		var parent = $('#js_tree').jstree('get_selected');

		$('#node_title').val($('#'+parent).attr('title'));

		var selected_loc_typeid =  $('#'+parent).attr('loc_typeid');
		populate(selected_loc_typeid,parent[0]);
		//var result,all_pbtype;
		////console.log(selected_loc_typeid);
		//$.when($.ajax({
		//	type: "GET",
		//	url:"/ajax/pbtype/get",
		//	dataType: "JSON",
		//	success: function(data){
		//		result = data;
		//	}
		//})).done(function( x ) {
		//	all_pbtype =  result;
		//	//console.log("from line 86"+all_pbtype);
		//	var pbtype_list = get_pbtypeForLocationType(all_pbtype,selected_loc_typeid);
		//	console.log(pbtype_list);
		//	var btn_html = "";
		//	var pbtype_list_content = "";

		//	for(var i=0;i < pbtype_list.length; i++){

		//		pbtype_list_content += "<option id='"+pbtype_list[i].id+"'>"+pbtype_list[i].shortName+"</option>";

		//		btn_html += "<tr><td><a id='current_pbadmin_btn"+i+"' onclick='return runMyFunction(event);' value='"+pbtype_list[i].id+"' class='btn pink current' href='#current_pbadmin' data-toggle='modal'>Current "+pbtype_list[i].shortName+"</a></td><td><a id='node_add_btn"+i+"' onclick='return runMyFunction(event);' value='"+pbtype_list[i].id+"' class='btn green add_child' href='#pb-list' data-toggle='modal'>All "+pbtype_list[i].shortName+"</a></td></tr>";

		//	}

		//	$('#pbtype_list').html(pbtype_list_content);
		//	$('#add_child_btn').html(btn_html);		


		//});


		if(!($('#'+parent).hasClass('jstree-open')) && !window.hash.hasOwnProperty('fake_node'+$('#'+parent).attr('id'))){
			new_node = {'text':'fake','id':'fake_node'+$('#'+parent).attr('id')};
			$('#js_tree').jstree(true).create_node(parent, new_node);
			window.hash['fake_node'+$('#'+parent).attr('id')] = 1;

		}
	}).bind("open_node.jstree",function(e,data){

		var parent = data.node.id;
		$("#js_tree").jstree("delete_node", $('#fake_node'+$('#'+parent).attr('id')));
		if($('#'+parent).closest("li").children("ul").length ==0){

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
						new_node = {'text':data[i].name+" Type :"+loc_hash[data[i].locationTypeId],'id':data[i].id,'li_attr':{'title':data[i].name,'loc_typeid':data[i].locationTypeId,'p_id':data[i].parentLocationId,'center_lat':data[i].latitude,'center_long':data[i].longitude,'boundaryFile':data[i].boundaryFile}};
						sel = $('#js_tree').jstree(true).create_node(selected_node, new_node);

					};

				}
			});


			$("#js_tree").jstree("open_node", selected_node);
		}

	});    


	$('#js_tree').jstree("select_node",root_node.id);	

		}
	});

});



/*******************Add a new Child Node***********************************/


function add_pbadmin(){

	var post_data = {
		"politicalBodyTypeId":$("#pbtype_list").val(),
		"locationId": $("#pbtype_list").val(),
		"personId": $("#pbadmin_personId").val(),
		"partyId": $("#party_list").val(),
		"email": $("#pbadmin_email").val(),
		"landLine1": $("#pbadmin_llandline1").val(),
		"landLine2": $("#pbadmin_llandline2").val(),
		"mobile1": $("#pbadmin_mobile1").val(),
		"mobile2": $("#pbadmin_mobile2").val(),
		"startDate": $("#pbadmin_startdate").val(), //date as milliseonds
		"endDate": $("#pbadmin_enddate").val(), //date as milliseonds
		"officeAddressDto": {
			"id": 33132,     
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
			"id": $("#pbtype_list").val(),     
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


function populate(loc_typeid, loc_id) {
	var pbtype_list = get_pbtypeForLocationType(all_pbtype,loc_typeid);
	//console.log(pbtype_list);
	var pbtype_list_content = "";
	$('#pbadmin_locationId').val(loc_id);
	$('#pbadmin_list_all').html('');
	$('#pbadmin_list_current').html('');

	for(var i=0;i < pbtype_list.length; i++){

		pbtype_list_content += "<option value='"+pbtype_list[i].id+"'>"+pbtype_list[i].shortName+"</option>";
		$('#pbadmin_list_current').append('<h2>Current '+pbtype_list[i].shortName+'</h2>');
		$('#pbadmin_list_all').append('<h2>All '+pbtype_list[i].shortName+'</h2>');
		get_pbadmin(loc_id,pbtype_list[i].id);
	}

	$('#pbtype_list').html(pbtype_list_content);
}

function setNNodeId(event){
	var target = event.target || event.srcElement;
	var locationId = $('#'+target.id).attr('pid');
	var pbtypeId = $('#'+target.id).attr('tid');
	$('#node_title').val($('#'+target.id).attr('title'));
	populate(pbtypeId, locationId);
}

function setPNodeId(event){
	var target = event.target || event.srcElement;
	var personId = $('#'+target.id).attr('pid');
	$('#person_title').val($('#'+target.id).attr('title'));
	$('#pbadmin_personId').val(personId);
}

function get_pbadmin(locationId,pbtypeId){

	//var target = event.target || event.srcElement;
	//var pbtypeId = $('#'+target.id).attr('value');
	//var selected_node =  $('#js_tree').jstree('get_selected');
	//var locationId = selected_node[0];

	//if($('#'+target.id).hasClass('current')){

		$.ajax({
			type: "GET",
		url:"/ajax/pbadmin/getcurrent/"+locationId+"/"+pbtypeId,
		dataType: "JSON",
		async: false,
		success: function(data){
			if(data){
				//$('#pbadmin_list_current').append('<p>'+data.name+'</p>');
				$('#pbadmin_list_current').append('<p>'+data.id+'</p>');
			}
		}
		});

		$.ajax({
			type: "GET",
		url:"/ajax/pbadmin/get/"+locationId+"/"+pbtypeId,
		dataType: "JSON",
		async: false,
		success: function(data){
			if(data){
				for(var i=0; i<data.length; i++){
					//$('#pbadmin_list_all').append('<p>'+data[i].name+'</p>');
					$('#pbadmin_list_all').append('<p>'+data[i].id+'</p>'); //MAke a get request with this id to get the person name and display that
				}
			}
		}
		});
	//}

	//if($('#'+target.id).hasClass('person')){
	//	$.ajax({
	//		type: "GET",
	//		url:"/ajax/party/getall",
	//		dataType: "JSON",
	//		success: function(data){
	//			//alert(JSON.stringify(data));	

	//			var party_list = "";
	//			for(var i=0; i<data.length;i++) {
	//				party_list += "<option id='"+data[i].id+"'>"+data[i].name+"</option>";

	//			}		   
	//			$("#party_list").html(party_list);		    	  
	//		}

	//	});

	/**********************Grab values from the selected person*********************/

	//$('#pbadmin_locationId').val(locationId);
	//$('#pbadmin_personId').val($('#'+target.id).attr('value'));
	//$('#pbadmin_email').val($('#'+target.id).attr('email'));
	//$('#pbadmin_llandline1').val($('#'+target.id).attr('landlineNumber1'));
	//$('#pbadmin_llandline2').val($('#'+target.id).attr('landlineNumber1'));
	//$('#pbadmin_mobile1').val($('#'+target.id).attr('mobileNumber1'));
	//$('#pbadmin_mobile2').val($('#'+target.id).attr('mobileNumber2'));
	//$('#pbadmin__haLine1').val($('#'+target.id).attr('line1'));
	//$('#pbadmin__haLine2').val($('#'+target.id).attr('line2'));
	//$('#pbadmin__haLine3').val($('#'+target.id).attr('line3'));
	//$('#pbadmin_hapostal').val($('#'+target.id).attr('postalCode'));

	//}

	//$('#new_node_loc_typeid').val($('#'+target.id).attr('value'));
	return true;
	//console.log($('#new_node_loc_typeid').attr('value'));
}
