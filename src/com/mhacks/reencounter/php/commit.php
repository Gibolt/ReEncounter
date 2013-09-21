<?php
/* require the user as the parameter */
if(isset($_GET['user'])) {
	/* soak in the passed variable or set our own */
	$lat = floatval($_GET['lat']);
	$long = floatval($_GET['long']);
	$time = date("Y-m-d H:i:s", strtotime($_POST['timestamp']));
	$format = strtolower($_GET['format']) == 'json' ? 'json' : 'xml'; //xml is the default

	/* connect to the db */
	$con = mysqli_connect('localhost','admin','encounter') or die('Cannot connect to the DB');
	mysqli_query('reencounter',$con) or die('Cannot select the DB');


	$select_timestmp = "Select User, Latitude, Longitude From Timestmp 
						Where Time = '$time';";
	$result_select_timestmp = mysqli_query($select_timestmp,$con) or die('Failed submission:  '.$select_timestamp);

	$insert_encounter = "";
	if(mysqli_num_rows($result_select_timestmp)) {
		$insert_encounter = "";
		$insert_encounter_details = "";
		while($timestamp = mysqli_fetch_assoc($result_select_timestmp)) {
			#$other_time = $timestamp["Time"];
			$other_user = $timestamp["User"];
			$other_lat =  $timestamp["Latitude"];
			$other_long = $timestamp["Longitude"];
			$proximity = distance($lat, $long, $other_lat, $other_long, "M");
			if ($proximity < 1) { # Within a 1 mile radius
				if (strcasecmp($user, $other_user) < 0) {
					addProximityCount($user, $other_user, $con);
					$insert_encounter .= "Insert Into Encounter
										Values ('$user', '$other_user', '$time');\n";	
					$insert_encounter_details .= "Insert Into Encounter
										Values ('$user', '$other_user', '$time', $proximity, $lat, $long, $other_lat, $other_long);\n";			
				}
				else {
					addProximityCount($other_user, $user, $con);
					
					$insert_encounter .= "Insert Into Encounter
										Values ('$other_user', '$user', '$time');\n";	
					$insert_encounter_details .= "Insert Into Encounter
										Values ('$other_user', '$user', '$time', $proximity, $other_lat, $other_long, $lat, $long);\n";	
				}
				
			}
		}
		mysqli_query($insert_encounter,$con) or die('Failed encounter submission:  '.$insert_encounter);
		mysqli_query($insert_encounter_details,$con) or die('Failed encounter submission:  '.$insert_encounter_details);
	}
	
	if ($insert_encounter != "") {
			$result = mysqli_query($insert_timestmp,$con) or die('Failed encounter submission:  '.$query);
	}
	
	$insert_timestmp = "Insert Into Timestmp
		Values ('$user', '$time', $lat, $long);";
	$result = mysqli_query($insert_timestmp,$con) or die('Failed submission:  '.$query);



	/* output in necessary format */
	if($format == 'json') {
		header('Content-type: application/json');
		echo json_encode(array('posts'=>$posts));
	}
	else {
		header('Content-type: text/xml');
		echo '<posts>';
		foreach($posts as $index => $post) {
			if(is_array($post)) {
				foreach($post as $key => $value) {
					echo '<',$key,'>';
					if(is_array($value)) {
						foreach($value as $tag => $val) {
							echo '<',$tag,'>',htmlentities($val),'</',$tag,'>';
						}
					}
					echo '</',$key,'>';
				}
			}
		}
		echo '</posts>';
	}

	/* disconnect from the db */
	@mysqli_close($con);
	

/*::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
/*::                                                                         :*/
/*::  This routine calculates the distance between two points (given the     :*/
/*::  latitude/longitude of those points). It is being used to calculate     :*/
/*::  the distance between two locations using GeoDataSource(TM) Products    :*/
/*::                     													 :*/
/*::  Definitions:                                                           :*/
/*::    South latitudes are negative, east longitudes are positive           :*/
/*::                                                                         :*/
/*::  Passed to function:                                                    :*/
/*::    lat1, lon1 = Latitude and Longitude of point 1 (in decimal degrees)  :*/
/*::    lat2, lon2 = Latitude and Longitude of point 2 (in decimal degrees)  :*/
/*::    unit = the unit you desire for results                               :*/
/*::           where: 'M' is statute miles                                   :*/
/*::                  'K' is kilometers (default)                            :*/
/*::                  'N' is nautical miles                                  :*/
/*::  Worldwide cities and other features databases with latitude longitude  :*/
/*::  are available at http://www.geodatasource.com                          :*/
/*::                                                                         :*/
/*::  For enquiries, please contact sales@geodatasource.com                  :*/
/*::                                                                         :*/
/*::  Official Web site: http://www.geodatasource.com                        :*/
/*::                                                                         :*/
/*::         GeoDataSource.com (C) All Rights Reserved 2013		   		     :*/
/*::                                                                         :*/
/*::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
function distance($lat1, $lon1, $lat2, $lon2, $unit) {

  $theta = $lon1 - $lon2;
  $dist = sin(deg2rad($lat1)) * sin(deg2rad($lat2)) +  cos(deg2rad($lat1)) * cos(deg2rad($lat2)) * cos(deg2rad($theta));
  $dist = acos($dist);
  $dist = rad2deg($dist);
  $miles = $dist * 60 * 1.1515;
  $unit = strtoupper($unit);

  if ($unit == "K") {
    return ($miles * 1.609344);
  } else if ($unit == "N") {
      return ($miles * 0.8684);
    } else {
        return $miles;
      }
}
#echo distance(32.9697, -96.80322, 29.46786, -98.53506, "M") . " Miles<br>";
#echo distance(32.9697, -96.80322, 29.46786, -98.53506, "K") . " Kilometers<br>";
#echo distance(32.9697, -96.80322, 29.46786, -98.53506, "N") . " Nautical Miles<br>";


function addProximityCount($user1, $user2, $con) {
	$select_proximity = "Select * From ProximityCounter 
						Where User1 = '$user1' and User2 = '$user2';";
	$result_proximity = mysqli_query($select_proximity,$con) or die('Failed submission:  '.$select_proximity);
	$insert_proximity = "";
	if (!$result_proximity) {
		$insert_proximity = "Insert Into ProximityCount
							Values ('$user1', '$user2', 1);";
	} 
	else {
		$insert_proximity = "Update ProximityCount
							Set count=count+1
							Where user1='$user1' and user2 = '$user2';";
	}
	mysqli_query($insert_proximity,$con);
}


?>