<?php
if(isset($_GET['user']) && isset($_GET['timestamp']) && isset($_GET['lat']) && isset($_GET['long'])) {
	echo "Entered main code<br>";
	$user = strtolower($_GET['user']);
	$password = strtolower($_GET['password']);
	$lat  = floatval($_GET['lat']);
	$long = floatval($_GET['long']);
	#$time = date("Y-m-d H:i:s", strtotime($_POST['timestamp']));
	$time = $_GET['timestamp'];
	$format = strtolower($_GET['format']) == 'json' ? 'json' : 'xml'; //xml is the default

	echo "Variables loaded<br>";
	
	$con = mysqli_connect('reencounter.cyzculuyt8xu.us-west-2.rds.amazonaws.com:3306','admin','encounter') or die('Cannot connect to the DB');
	mysqli_select_db($con,'ReEncounterDb') or die('Cannot select the DB');
	echo "Database Connected<br>";
	
	$select_user = "Select * From User
					Where User = '$user' And Password='$password';";
	if (mysqli_query($con, $select_user)) {
		echo "User Failed to Match!!!!!!!!!!<br>Exiting...";
		return;
	}
	echo "User Matched<br>";
	
	$select_timestmp = "Select User, Latitude, Longitude From Timestmp 
						Where Time = '$time';";
	$result_select_timestmp = mysqli_query($con, $select_timestmp) or die('Failed submission:  '.$select_timestamp);

	$test_query = "Select * From Timestmp;";
	$test_query_result = mysqli_query($con, $select_timestmp) or die('Failed submission:  '.$test_query);
	
	$lines = mysqli_num_rows($result_select_timestmp);
	echo "Timestamp selected, $lines matched<br>";
	echo "Current timestamp: $time<br>";
	
	if(mysqli_num_rows($result_select_timestmp)) {
#		$insert_encounter = "";
#		$insert_encounter_details = "";
		echo "Entering While Loop<br>";
		while($timestamp = mysqli_fetch_assoc($result_select_timestmp)) {
			#$other_time = $timestamp["Time"];
			$other_user = $timestamp["User"];
			$other_lat =  $timestamp["Latitude"];
			$other_long = $timestamp["Longitude"];
			$proximity = distance($lat, $long, $other_lat, $other_long, "M");
			echo "Proximity is: $proximity. Timestamp is: $time.<br>";
			if ($proximity < 1) { # Within a 1 mile radius
                            #Check to see if encounter between users for the day is already added into database
                            $current_date = date('Y-m-d H:i:s');
                            $query_for_encounter_check = "Select *
                                    From Encounter 
                                    Where DATE(Time) = '$current_date' AND User1 = '$other_user' AND User2 = '$user';";
                            $encounter_query_result = mysqli_query($con, $query_for_encounter_check) or die('Failed submission:  '.$query_for_encounter_check);
                            if(mysqli_num_rows($encounter_query_result))  {
				if (strcasecmp($user, $other_user) < 0) {
					addProximityCount($user, $other_user, $con);
					
					$insert_encounter = "Insert Into Encounter
										Values ('$user', '$other_user', '$time');\n";	
					$insert_encounter_details = "Insert Into EncounterDetails
										Values ('$user', '$other_user', '$time', $proximity, $lat, $long, $other_lat, $other_long);\n";		

					mysqli_query($con, $insert_encounter) or die('Failed encounter submission:  '.$insert_encounter);
					mysqli_query($con, $insert_encounter_details) or die('Failed encounter submission:  '.$insert_encounter_details);
				}
				else {
					addProximityCount($other_user, $user, $con);
					
					$insert_encounter = "Insert Into Encounter
										Values ('$other_user', '$user', '$time');\n";	
					$insert_encounter_details = "Insert Into EncounterDetails
										Values ('$other_user', '$user', '$time', $proximity, $other_lat, $other_long, $lat, $long);\n";	
										
					mysqli_query($con, $insert_encounter) or die('Failed encounter submission:  '.$insert_encounter);
					mysqli_query($con, $insert_encounter_details) or die('Failed encounter submission:  '.$insert_encounter_details);
				}			
                        }
			}
		}
		echo "Exited While Loop<br>";
#		if ($insert_encounter != "") {
#			mysqli_query($con, $insert_encounter) or die('Failed encounter submission:  '.$insert_encounter);
#			mysqli_query($con, $insert_encounter_details) or die('Failed encounter submission:  '.$insert_encounter_details);
#		}
	}
	
	$insert_timestmp = "Insert Into Timestmp
						Values ('$user', '$time', $lat, $long);";
	$result = mysqli_query($con, $insert_timestmp) or die('Failed submission:  '.$insert_timestmp);

	echo "Inserting into Timestamp<br>";


	/* output in necessary format */
/*	if($format == 'json') {
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
	}*/

	/* disconnect from the db */
	@mysqli_close($con);
}

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
	$select_proximity = "Select * From ProximityCount 
						Where User1 = '$user1' and User2 = '$user2';";
	$result_proximity = mysqli_query($con, $select_proximity) or die('Failed submission:  '.$select_proximity);
	$insert_proximity = "";
	if (!mysqli_num_rows($result_proximity)) {
		$insert_proximity = "Insert Into ProximityCount
							Values ('$user1', '$user2', 1);";
	} 
	else {
		$insert_proximity = "Update ProximityCount
							Set times=times+1
							Where user1='$user1' and user2 = '$user2';";
	}
	mysqli_query($con, $insert_proximity) or die('Failed submission:  '.$insert_proximity);
}


?>
