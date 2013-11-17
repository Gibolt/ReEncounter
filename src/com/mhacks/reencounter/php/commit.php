<?php
if(isset($_GET['user']) && isset($_GET['password']) && isset($_GET['timestamp']) && isset($_GET['lat']) && isset($_GET['lon'])) {
	include 'globalFunctions.php';
	$user     = strtolower($_GET['user']);
	$password = strtolower($_GET['password']);
	$lat      = floatval($_GET['lat']);
	$lon      = floatval($_GET['lon']);
	$time     = $_GET['timestamp'];

	$defaultDegree = 1/69;
	$defaultDegreeLon = $defaultDegree/cos(deg2rad($lat));  # Converts optimization based on latitude
	$minLat = $lat-$defaultDegree;
	$maxLat = $lat+$defaultDegree;
	$minLon = $lon+$defaultDegreeLon;
	$maxLon = $lon-$defaultDegreeLon;

	$con = establishConnection();
	userAuthentication($user, $password, $con);
	echo "User Authenticated<br>";

	$select_timestmp = "Select User, Latitude, Longitude From Timestmp 
						Where Time = '$time' And Latitude > $minLat And Latitude < $maxLat And Longitude > $minLon And Longitude < $maxLon;";
	$result_timestmp = mysqli_query($con, $select_timestmp) or die('Failed: '.$select_timestamp);
	$lines = mysqli_num_rows($result_timestmp);
	echo "Timestamp selected, $lines matched<br>";
	echo "Current timestamp: $time<br>";

	if(mysqli_num_rows($result_timestmp)) {
		$select_for_block = "Select User2 As otherUser from Blocked 
							 Where User1 = '$user'
							 Union All
							 Select User1 As otherUser from Blocked 
							 Where User2 = '$user'
							 Order By otherUser;";
		$result_for_block = mysqli_query($con, $select_for_block) or die('Failed: '.$select_for_block);
		echo "Entering While Loop<br>";
		while($timestamp = mysqli_fetch_assoc($result_timestmp)) {
			$otherUser = $timestamp["User"];
			if ($user == $otherUser) {  # User already has submitted for this timestamp. Prevents failed submission
				echo "Timestamp already exists<br>";
				return;
			}
			if (containsUser($otherUser, $result_for_block)) {  # Block between users
				continue;
			}
			$otherLat  = $timestamp["Latitude"];
			$otherLon  = $timestamp["Longitude"];
			$proximity = distance($lat, $lon, $otherLat, $otherLon, $defaultUnit);
			echo "Proximity is: $proximity. Timestamp is: $time.<br>";
			if ($proximity < $defaultRadius) {  # Within a default radius
                #Check to see if encounter between users for the day is already added into database
                $current_date = date('Y-m-d H:i:s');
				if (strcasecmp($user, $otherUser) < 0) {
					$query_for_encounter_check = "Select *
												  From Encounter 
												  Where DATE(Time) = '$current_date' AND User1 = '$user' AND User2 = '$otherUser';";
                    $encounter_query_result = mysqli_query($con, $query_for_encounter_check) or die('Failed: '.$query_for_encounter_check);
					if(mysqli_num_rows($encounter_query_result) == 0)  {
						addProximityCount($user, $otherUser, $con);

						$insert_encounter = "Insert Into Encounter
											 Values ('$user', '$otherUser', '$time');";	
						$insert_encounter_details = "Insert Into EncounterDetails
											         Values ('$user', '$otherUser', '$time', $proximity, $lat, $lon, $otherLat, $otherLon);";		

						mysqli_query($con, $insert_encounter) or die('Failed: '.$insert_encounter);
						mysqli_query($con, $insert_encounter_details) or die('Failed: '.$insert_encounter_details);
					}
				}
				else {
					$query_for_encounter_check = "Select *
												  From Encounter 
												  Where DATE(Time) = '$current_date' AND User1 = '$otherUser' AND User2 = '$user';";
                    $encounter_query_result = mysqli_query($con, $query_for_encounter_check) or die('Failed: '.$query_for_encounter_check);
					if(mysqli_num_rows($encounter_query_result) == 0)  {
						addProximityCount($otherUser, $user, $con);

						$insert_encounter = "Insert Into Encounter
											 Values ('$otherUser', '$user', '$time');";	
						$insert_encounter_details = "Insert Into EncounterDetails
											         Values ('$otherUser', '$user', '$time', $proximity, $otherLat, $otherLon, $lat, $lon);";	

						mysqli_query($con, $insert_encounter) or die('Failed:  '.$insert_encounter);
						mysqli_query($con, $insert_encounter_details) or die('Failed:  '.$insert_encounter_details);
					}			
                }
			}
		}
		echo "Exited While Loop<br>";
	}

	$insert_timestmp = "Insert Into Timestmp
						Values ('$user', '$time', $lat, $lon);";
	$result = mysqli_query($con, $insert_timestmp) or die('Failed: '.$insert_timestmp);
	echo "Inserting into Timestamp<br>";

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

	if ($unit == "N") {
		return ($miles * 0.8684);
	} 
	else if ($unit == "K") {
		return ($miles * 1.609344);
    }
	else {
        return $miles;
    }
}

function addProximityCount($user1, $user2, $con) {
	$select_proximity = "Select * From ProximityCount 
						Where User1 = '$user1' and User2 = '$user2' Limit 1;";
	$result_proximity = mysqli_query($con, $select_proximity) or die('Failed: '.$select_proximity);
	$insert_proximity = "";
	if (!mysqli_num_rows($result_proximity)) {
		$insert_proximity = "Insert Into ProximityCount
							Values ('$user1', '$user2', 1);";
	}
	else {
		$insert_proximity = "Update ProximityCount
							Set times=times+1
							Where user1='$user1' and user2 = '$user2' Limit 1;";
	}
	mysqli_query($con, $insert_proximity) or die('Failed: '.$insert_proximity);
}

function containsUser($user, $result) {
	while($post = mysqli_fetch_assoc($result)) {
		if($user == $post['otherUser']) {
			return 1;
		}
	}
	return 0;
}
?>
