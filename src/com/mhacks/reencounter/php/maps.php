<?php
if(isset($_GET['user1']) && isset($_GET['user2']) && isset($_GET['password'])) {
	include 'globalFunctions.php';
	$user      = strtolower($_GET['user1']);
	$otherUser = strtolower($_GET['user2']);
	$password  = $_GET['password'];

	sameUser($user, $otherUser);
	$con = establishConnection();
	userAuthentication($user, $password, $con);

	if (encountered($user, $otherUser, $con)) {
		$select_encounter_details = "";
		if (strcasecmp($user, $otherUser) < 0) {
			$select_encounter_details = "Select Time, Latitude1 as lat, Longitude1 as lon, Distance from EncounterDetails 
										 Where User1 = '$user' And User2 = '$otherUser';";						
		}
		else {
			$select_encounter_details = "Select Time, Latitude2 as lat, Longitude2 as lon, Distance from EncounterDetails 
										 Where User1 = '$otherUser' And User2 = '$user';";
		}
		$result_encounter_details = mysqli_query($con, $select_encounter_details) or die('Failed submission:  '.$select_encounter_details);

		header('Content-type: application/json');	
		$posts = array();
		while($post = mysqli_fetch_assoc($result_encounter_details)) {
			array_push($posts,$post);
		}
		echo json_encode(array('posts'=>$posts));
	}

	@mysqli_close($con);
}
?>
