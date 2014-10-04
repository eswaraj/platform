var all_pbtype = {};

$(window).scroll(function () {
    $( "#header1" ).css( "display","inline");
    $( "#header1" ).css( "position","fixed");
});

$(window).scroll(function(){
    if ($(this).scrollTop() ==0) {
        $("#header1").css( "display","none");
    }
});

$(function(){
    //$("#menu").load("../ui/sidebar_menu.html"); 
    $("#menu_new").load("../ui/menu.html"); 
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
                party_list += "<option value='"+data[i].id+"'>"+data[i].name+"</option>";

            }		   
            $("#party_list").html(party_list);		    	  
        }

    });
}

function get_pbtypeForLocationType(pbtypes,selected_loc_typeid){
    var result = [];
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
    window.hash = {};
    window.loc_hash = {};
    var all_ltype = fetch_ltype();
    $('#searchResultsP').hide();
    $('#searchResultsN').hide();
    fetch_pbtype();
    fetch_plist();

    $("#pbadmin_startdate").datepicker();
    $("#pbadmin_enddate").datepicker();

    $("#person_search").autocomplete({		
        source: "/ajax/person/search/name/",
        minLength: 3,
        response: function( event, ui ) {
            $('#searchResultsP').show();
            $('#searchResultsN').hide();
            data = ui.content;
            $('#users tbody').html("");
            for(var i=0; i<data.length; i++) {
                $('#users tbody').append("<tr'>" +
                                         "<td><img src='" + data[i].profilePhoto + "' class='thumb'></td>" +
                                         "<td>" + data[i].id + "</td>" +
                                         "<td>" + data[i].name + "</td>" +
                                         "<td>" + "<button class='btn btn-primary blue' type='button' onClick='setPNodeId(event);' pid='" + data[i].id + "' id='P" + data[i].id + "' title='" + data[i].name + "'>Select</button>" + "</td>" +
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
                $('#searchResultsP').show();
                $('#searchResultsN').hide();
                $('#users tbody').html("");
                for(var i=0; i<data.length; i++) {
                    $('#users tbody').append("<tr>" +
                                             "<td><img src='" + data[i].profilePhoto + "' class='thumb'></td>" +
                                             "<td>" + data[i].id + "</td>" +
                                             "<td>" + data[i].name + "</td>" +
                                             "<td>" + "<button class='btn btn-primary blue' type='button' onClick='setPNodeId(event);' pid='" + data[i].id + "' id='P" + data[i].id + "' title='" + data[i].name + "'>Select</button>" + "</td>" +
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
            $('#searchResultsN').show();
            $('#searchResultsP').hide();
            data = ui.content;
            $('#nodes tbody').html("");
            for(var i=0; i<data.length; i++) {
                $('#nodes tbody').append("<tr>" +
                                         "<td>" + data[i].id + "</td>" +
                                         "<td>" + loc_hash[data[i].locationTypeId] + "</td>" + //change name to type when real API is available
                                         "<td>" + data[i].name + "</td>" +
                                         "<td>" + "<button class='btn btn-primary blue' type='button' onClick='setNNodeId(event);' pid='" + data[i].id + "' tid='" + data[i].locationTypeId + "' id='N" + data[i].id + "' title='" + data[i].name + "'>Select</button>" + "</td>" +
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
                $('#searchResultsN').show();
                $('#searchResultsP').hide();
                $('#nodes tbody').html("");
                for(var i=0; i<data.length; i++) {
                    $('#nodes tbody').append("<tr>" +
                                             "<td>" + data[i].id + "</td>" +
                                             "<td>" + loc_hash[data[i].locationTypeId] + "</td>" + //change name to type when real API is available
                                             "<td>" + data[i].name + "</td>" +
                                             "<td>" + "<button class='btn btn-primary blue' type='button' onClick='setNNodeId(event);' pid='" + data[i].id + "' tid='" + data[i].locationTypeId + "' id='N" + data[i].id + "' title='" + data[i].name + "'>Select</button>" + "</td>" +
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

                            }

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

    var start, end;
    alert('Start Date : '+ $("#pbadmin_startdate").val());
    if($("#pbadmin_startdate").val() === "") {
    	alert('Please enter Start Date');
    	return;
    }
    if($("#pbadmin_startdate").val() != "") {
        var d = new Date($("#pbadmin_startdate").val());
        start = d.getTime();
    }
    if($("#pbadmin_enddate").val() != "") {
        var d = new Date($("#pbadmin_enddate").val());
        end = d.getTime();
    }
    
    var post_data = {
        "id":$("#pbadmin_id").val(),
        "politicalBodyTypeId":$("#pbtype_list").val(),
        "locationId": $("#pbadmin_locationId").val(),
        "personId": $("#pbadmin_personId").val(),
        "partyId": $("#party_list").val(),
        "email": $("#pbadmin_email").val(),
        "landLine1": $("#pbadmin_llandline1").val(),
        "landLine2": $("#pbadmin_llandline2").val(),
        "mobile1": $("#pbadmin_mobile1").val(),
        "mobile2": $("#pbadmin_mobile2").val(),
        "fbPage": $("pbadmin_fbpage").val(),
        "fbAccount": $("pbadmin_fbaccount").val(),
        "twitterHandle": $("pbadmin_twitterhandle").val(),
        "startDate": start, //date as milliseonds
        "endDate": end, //date as milliseonds
        "officeAddressDto": {
            //"id": 33132,     
            "id" : "",
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
            //"id": $("#pbtype_list").val(),     
            "id" : "",
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

    };
    $.ajax({
        type: "POST",
        url:"/ajax/pbadmin/save",
        data: JSON.stringify(post_data),
        contentType: "application/json; charset=utf-8",
        dataType: "JSON",
        success: function(data){
            //console.log(JSON.stringify(data, null, 4));
            //var error = data.hasOwnProperty('message');
            //var error = data.message !== null;
            //if(error) {
            //    alert("Error in admin creation: " + data.message);
            //}
            //else {
                alert("PbAdmin Added"+data.personId);
            //}
        }
    });
}

function showEdit(event) {
    var target = event.target || event.srcElement;
    var locationId = $('#'+target.id).attr('location_id');
    var pbtypeId = $('#'+target.id).attr('pbtype_id');

    $.ajax({
        type: "GET",
        url:"/ajax/pbadmin/getcurrent/"+locationId+"/"+pbtypeId,
        dataType: "JSON",
        async: false,
        success: function(data){
            if(data){
                $("#pbadmin_id").val(data.id);
                $("#pbtype_list").val(data.politicalBodyTypeId);
                $("#pbadmin_locationId").val(data.locationId);
                $("#pbadmin_personId").val(data.personId);
                $("#party_list").val(data.partyId);
                $("#pbadmin_email").val(data.email);
                $("#pbadmin_llandline1").val(data.landLine1);
                $("#pbadmin_llandline2").val(data.landLine2);
                $("#pbadmin_mobile1").val(data.mobile1);
                $("#pbadmin_mobile2").val(data.mobile2);
                $("pbadmin_fbpage").val(data.fbPage);
                $("pbadmin_fbaccount").val(data.fbAccount);
                $("pbadmin_twitterhandle").val(data.twitterHandle);
                alert("Start date from server : " + data.startDate);
                $("#pbadmin_startdate").val(new Date(data.startDate)); //date as milliseonds
                $("#pbadmin_enddate").val(data.endDate); //date as milliseonds

                $("#pbadmin_oaId").val(data.officeAddressDto.id);
                $("#pbadmin_oaLine1").val(data.officeAddressDto.line1);
                $("#pbadmin_oaLine2").val(data.officeAddressDto.line2);
                $("#pbadmin_oaLine3").val(data.officeAddressDto.line3);
                $("#pbadmin_oapostal").val(data.officeAddressDto.postalCode);
                $("#pbadmin_ovillage-list").val(data.officeAddressDto.villageId);
                $("#pbadmin_oward-list").val(data.officeAddressDto.wardId);
                $("#pbadmin_ocity-list").val(data.officeAddressDto.cityId);
                $("#pbadmin_odistrict-list").val(data.officeAddressDto.districtId);
                $("#pbadmin_ostate-list").val(data.officeAddressDto.stateId);
                $("#pbadmin_ocountry-list").val(data.officeAddressDto.countryId);

                $("#pbadmin_haId").val(data.homeAddressDto.id);
                $("#pbadmin_haLine1").val(data.homeAddressDto.line1);
                $("#pbadmin_haLine2").val(data.homeAddressDto.line2);
                $("#pbadmin_haLine3").val(data.homeAddressDto.line3);
                $("#pbadmin_hapostal").val(data.homeAddressDto.postalCode);
                $("#pbadmin_hvillage-list").val(data.homeAddressDto.villageId);
                $("#pbadmin_hward-list").val(data.homeAddressDto.wardId);
                $("#pbadmin_hcity-list").val(data.homeAddressDto.cityId);
                $("#pbadmin_hdistrict-list").val(data.homeAddressDto.districtId);
                $("#pbadmin_hstate-list").val(data.homeAddressDto.stateId);
                $("#pbadmin_hcountry-list").val(data.homeAddressDto.countryId);

                //Show the modal form
                $("#assign_pbadmin").modal('show');
            }
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
        $('#pbadmin_list_current').append('Current '+pbtype_list[i].shortName);
        $('#pbadmin_list_current').append('<br>');
        $('#pbadmin_list_all').append('All '+pbtype_list[i].shortName);
        $('#pbadmin_list_all').append('<br>');
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
                $.ajax({
                    type: "GET",
                    url:"/ajax/person/get/"+data.personId,
                    dataType: "JSON",
                    async: false,
                    success: function(d){
                        if(d){
                            $('#pbadmin_list_current').append('<p>'+d.name+'<input type="button" class="admin_edit" onclick="showEdit(event);" id="admin_'+data.personId+'_'+pbtypeId+'" location_id="'+locationId+'" pbtype_id="'+pbtypeId+'" value="Edit"></input></p>');
                        }
                    }
                });
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
                    $.ajax({
                        type: "GET",
                        url:"/ajax/person/get/"+data[i].personId,
                        dataType: "JSON",
                        async: false,
                        success: function(d){
                            if(d){
                                $('#pbadmin_list_all').append('<p>'+d.name+'</p>');
                            }
                        }
                    });
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
