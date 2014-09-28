var map, heatmap;
var mapData;
var markerClusterer;
var markerList = [];
var bounds;
var imageUrl = 'http://chart.apis.google.com/chart?cht=mm&chs=24x32&' + 'chco=FFFFFF,008CFF,000000&ext=.png';

function initialize() {
	var mapOptions = {
		zoom: 13,
		center: new google.maps.LatLng(28.61, 77.23),
		mapTypeId: google.maps.MapTypeId.ROADMAP
	};

	map = new google.maps.Map(document.getElementById('map-canvas'), mapOptions);

	//Default map view set to heatmap
	createData();
}

function setMapData(data) {
	mapData = data;
	bounds = new google.maps.LatLngBounds ();
	for(var i=0; i<mapData.length; i++) {
		bounds.extend (mapData[i]);
	}
	map.fitBounds(bounds);
}

function clearMap() {
	for(var i=0; i<markerList.length; i++) {
		markerList[i].setMap(null);
	}
	markerList = [];
	if(markerClusterer !== null && typeof markerClusterer != 'undefined') {
		markerClusterer.clearMarkers();
		markerClusterer = null;
	}
	if(heatmap !== null && typeof heatmap != 'undefined') {
		heatmap.setMap(null)
			heatmap = null;
	}
}

function createHeatmap() {
	clearMap();
	var pointArray = new google.maps.MVCArray(mapData);

	heatmap = new google.maps.visualization.HeatmapLayer({
		data: pointArray
	});

	heatmap.setMap(map);
}

function createMarker() {
	clearMap();
	for(var i=0; i<mapData.length; i++) {
		var marker = new google.maps.Marker({
			position: mapData[i],
			map: map,
		});
		markerList.push(marker);    
	}
}

function createCluster() {
	clearMap();
	var markerImage = new google.maps.MarkerImage(imageUrl, new google.maps.Size(24, 32));
	for(var i=0; i<mapData.length; i++) {
		var marker = new google.maps.Marker({
			position: mapData[i],
			icon: markerImage
		});
		markerList.push(marker);    
	}
	markerClusterer = new MarkerClusterer(map, markerList, {
		maxZoom: 14,
			gridSize: 50,
			//styles: styles[style]
	});

	//Change the calculator function as needed
	markerClusterer.setCalculator(function(markers, numStyles) {
		var index = 0;
		var count = markers.length;
		var dv = count;
		while (dv !== 0) {
			dv = parseInt(dv / 10, 10);
			index++;
		}

		index = Math.min(index, numStyles);
		return {
			text: count,
		index: index
		};
	});
}

function getData(url) {
	var path = 'http://dev.eswaraj.com/api/complaint/location/'+url;
	$.ajax({
		type: "GET",
		url: path,
		contentType: "application/json; charset=utf-8",
		dataType: "JSON",
		success: function(data){
			var d = [];
			for(var i=0; i<data.length; i++) {
				d.push(new google.maps.LatLng(data[i].lattitude, data[i].longitude));
			}
			setMapData(d);
			createHeatmap();
		}
	});
}

function createData(){
	var currentData = [];
	for(var i=0; i<complaints.length; i++){
		currentData.push(new google.maps.LatLng(complaints[i].lat,complaint[i].lng));
	}
	setMapData(currentData);
	createHeatmap();
}

google.maps.event.addDomListener(window, 'load', initialize);

