$(function(){
	$("#menu").load("../ui/sidebar_menu.html"); 
});
$(document).ready(function(){
	$("#searchButton").click(function() {		
		var s = $('#person_search').val();
		$.ajax({
			type: "POST",
			url:"/ajax/person/search/name/"+s,
			data: "",
			contentType: "application/json; charset=utf-8",
			dataType: "JSON",
			success: function(data){
				for(var i=0; i<data.length; i++) {
					$('#users tbody').append("<tr>" +
						"<td>" + data[i].name + "</td>" +
						"<td>" + data[i].email + "</td>" +
						"<td>" + data[i].line1 + "</td>" +
						"<td>" + "<a id='update' class='btn blue' href='#new-person' data-toggle='modal' onClick='setNodeId();'" +" email='"+data[i].email+"' landlineNumber1='"+data[i].landlineNumber1+"' landlineNumber2='"+data[i].landlineNumber2+"' line1='"+data[i].line1+"' line2='"+data[i].line2+"' line3='"+data[i].line3+"' value='"+data[i].id+"' line2='"+data[i].line2+"' postalCode='"+data[i].postalCode+"' villageId='"+data[i].villageId+"' wardId='"+data[i].wardId+"' cityId='"+data[i].cityId+"' districtId='"+data[i].districtId+"' stateId='"+data[i].stateId+"' countryId='"+data[i].countryId+"'"  + '>Update</a>' + "</td>" +
						"</tr>" 
						);
				}
			}
		});
	});
});
