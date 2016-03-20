<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html>
<html>
<head>

<title>Autocomplete in java web application using Jquery and
	JSON</title>
<script src="//code.jquery.com/jquery-1.10.2.js"></script>
<script src="//code.jquery.com/ui/1.10.4/jquery-ui.js"></script>
<script src="autocompleter.js"></script>
<link rel="stylesheet" href="jquery-ui.css">
<!-- User defied css -->
<link rel="stylesheet" href="style.css">
<link
	href="https://maxcdn.bootstrapcdn.com/bootswatch/3.3.6/cerulean/bootstrap.min.css"
	rel="stylesheet"
	integrity="sha384-fUMURLTdEcpeYHly3PAwggI3l2UvdHNg/I+8LRph7hLDcAZm77YfDx3Tjum9d3vK"
	crossorigin="anonymous">


</head>
<body onload="geoFindMe()" >
	<br />
	<br />
	<!-- 	<div class="search-container">



		<label for="inputEmail" class="pull-left">Search: </label> <input
			type="text" id="search" name="search" class="search col-lg-4" />

	</div> -->
	<div class="container">
		<img src="search.png" class="center">
		<form class="form-horizontal col-lg-12" action="SearchResult" method='get' >

			<div class="form-group">
				<div class="ui-widget">
					<label class="col-lg-1 control-label">Search: </label>
					<div class="col-lg-10">
						<input type="text" id="search" name="search" class="form-control" />
						<input type="hidden" id ="zipcode" name="zipCode" value="">
						<input type="hidden" id ="latlong" name="geoLocation" value="">
						<input type="submit" id="submit" name="submit" value="Search" class="col-lg-4" />
					
						
						
					</div>
				</div>

			</div>
		</form>

		<!-- Table for result disply  -->
		<div class="col-lg-11">
		<table class="table table-striped table-hover">
			<thead>
				<tr>
					<th> Description </th>
					<th> Scoring </th>
					<th> Doc Path </th>
				</tr>
			</thead>
			<tbody>
			<c:forEach items="${items}" var="i">
				<tr>
					<td> ${i.description }</td>
					<td>Overall: ${i.score } <br/> TF-IDF: ${i.tfIdf } <br/> PageRanking: ${i.pageRanking }</td>
					<td><a href="${i.location}">${i.location }</a></td>
				</tr>
			</c:forEach>

			</tbody>
		</table>
		</div>
	</div>
</body>
<script>
function geoFindMe() {
	
	    if (navigator.geolocation) {
	        navigator.geolocation.getCurrentPosition(showPosition);
	        showPosition();
	    } else {
	        x.innerHTML = "Geolocation is not supported by this browser.";
	    }
	
	function showPosition(position) {
	    console.log("Latitude: " + position.coords.latitude+" Longitude: "+ position.coords.longitude);
	  	//make ajax request to url http://api.geonames.org/findNearbyPostalCodesJSON?lat=34.0677337%20&lng=-118.1668624&username=demo
	  			var latitude=position.coords.latitude;
	    		var longitude=position.coords.longitude;
	    		var str="lat :"+latitude+" lon:"+longitude;
				$('#latlong').attr('value',str);
	  			var url="http://api.geonames.org/findNearbyPostalCodesJSON?lat="+latitude+"&lng="+longitude+"&username=demo";
	  	$.ajax(url, {
			type : 'POST',
			success : function(response) {
				console.log("Response :"+response.postalCodes[1].postalCode);
				$('#zipcode').attr('value',response.postalCodes[1].postalCode); 
				

			}

		})
	}
}

</script>
</html>
