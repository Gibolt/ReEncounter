<?php
if(isset($_GET['user']) && isset($_GET['password']) && isset($_GET['otherUser']) && isset($_GET['timestamp'])) {
	include 'globalFunctions.php';
	$user      = strtolower($_GET['user']);
	$password  = $_GET['password'];
	$otherUser = strtolower($_GET['otherUser']);
	$time      = $_GET['timestamp'];

	sameUser($user, $otherUser);
	$con = establishConnection();
	userAuthentication($user, $password, $con);

	if (strcasecmp($user, $otherUser) > 0) {
		$temp = $user;
		$uesr = $otherUser;
		$otherUser = $temp;
	}

	if (encountered($user, $otherUser, $con)) {
		$delete_from_encounter = "Delete from Encounter
								  Where User1='$user' And User2='$otherUser' And time='$time';";
		$delete_from_details = "Delete from EncounterDetails
								Where User1='$user' And User2='$otherUser' And time='$time';";
		$reduce_proximity_count = "Update ProximityCount
								   Set times=times-1
								   Where User1='$user' And User2='$otherUser' Limit 1;";

		mysqli_query($con, $delete_from_encounter) or die('Failed: '.$delete_from_encounter);
		mysqli_query($con, $delete_from_details) or die('Failed: '.$delete_from_details);
		mysqli_query($con, $reduce_proximity_count) or die('Failed: '.$reduce_proximity_count);	
	}
		
	@mysqli_close($con);
}
?>
