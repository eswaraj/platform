var person;

$(function(){
	$("#menu").load("../ui/sidebar_menu.html"); 
});

function setNodeId(event) {
	var target = event.target || event.srcElement;
	$("#new_person_id").val($('#'+target.id).attr('pid'));
	$("#new_person_name").val($('#'+target.id).attr('pname'));
	$("#new_person_biodata").val($('#'+target.id).attr('biodata'));
	$("#new_person_dob").val($('#'+target.id).attr('dob'));
	$("#new_person_photo").val($('#'+target.id).attr('photo'));
	$("#new_person_gender").val($('#'+target.id).attr('gender'));
	$("#new_person_email").val($('#'+target.id).attr('email'));
	$("#new_person_ll1").val($('#'+target.id).attr('ll1'));
	$("#new_person_ll2").val($('#'+target.id).attr('ll2'));
	$("#new_person_mobile1").val($('#'+target.id).attr('mobile1'));
	$("#new_person_mobile2").val($('#'+target.id).attr('mobile2'));
	$("#new_person_addressId").val($('#'+target.id).attr('addressId'));
	$("#new_person_line1").val($('#'+target.id).attr('line1'));
	$("#new_person_line2").val($('#'+target.id).attr('line2'));
	$("#new_person_line3").val($('#'+target.id).attr('line3'));
	$("#new_person_postal").val($('#'+target.id).attr('postal'));
	$("#village-list").val($('#'+target.id).attr('villageId'));
	$("#ward-list").val($('#'+target.id).attr('wardId'));
	$("#city-list").val($('#'+target.id).attr('cityId'));
	$("#district-list").val($('#'+target.id).attr('districtId'));
	$("#state-list").val($('#'+target.id).attr('stateId'));
	$("#country-list").val($('#'+target.id).attr('countryId'));
}

$(document).ready(function(){
	$("#searchButton").click(function() {		
		var s = $('#person_search').val();
		$.ajax({
			type: "GET",
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
						"<td>" + "<a id='update" + i + "' class='btn blue' href='#new-person' data-toggle='modal' onClick='setNodeId(event);'" +"pid='"+data[i].id+"' pname='"+data[i].name+"' biodata='"+data[i].biodata+"' dob='"+data[i].dob+"' gender='"+data[i].gender+"' photo='"+data[i].photo+"' email='"+data[i].email+"' ll1='"+data[i].landlineNumber1+"' ll2='"+data[i].landlineNumber2+"' mobile1='"+data[i].mobileNumber1+"' mobile2='"+data[i].mobileNumber2+"' addressId='"+data[i].personAddress.id+"' line1='"+data[i].personAddress.line1+"' line2='"+data[i].personAddress.line2+"' line3='"+data[i].personAddress.line3+"' value='"+data[i].id+"' postal='"+data[i].personAddress.postalCode+"' villageId='"+data[i].personAddress.villageId+"' wardId='"+data[i].personAddress.wardId+"' cityId='"+data[i].personAddress.cityId+"' districtId='"+data[i].personAddress.districtId+"' stateId='"+data[i].personAddress.stateId+"' countryId='"+data[i].personAddress.countryId+"'"  + '>Update</a>' + "</td>" +
						"</tr>" 
						);
				}
			}
		});
	});

	$("#form-save").click(function() {		
		person.id = $('#new_person_id').val();
		person.name = $('#new_person_name').val();
		person.biodata = $('#new_person_biodata').val();
		person.dob = $('#new_person_dob').val();
		person.photo = $('#new_person_photo').val();
		person.gender = $('#new_person_gender').val();
		person.email = $('#new_person_email').val();
		person.landlineNumber1 = $('#new_person_ll1').val();
		person.landlineNumber2 = $('#new_person_ll2').val();
		person.mobileNumber1 = $('#new_person_mobile1').val();
		person.mobileNumber2 = $('#new_person_mobile2').val();
		person.personAddress.id = $('#new_person_addressId').val();
		person.personAddress.line1 = $('#new_person_line1').val();
		person.personAddress.line2 = $('#new_person_line2').val();
		person.personAddress.line3 = $('#new_person_line3').val();
		person.personAddress.postalCode = $('#new_person_postal').val();
		person.personAddress.villageId = $('#village-list').val();
		person.personAddress.wardId = $('#ward-list').val();
		person.personAddress.cityId = $('#city-list').val();
		person.personAddress.districtId = $('#district-list').val();
		person.personAddress.stateId = $('#state-list').val();
		person.personAddress.countryId = $('#country-list').val();
	});
});
