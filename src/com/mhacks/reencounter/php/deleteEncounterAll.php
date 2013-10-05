<?php
if(isset($_GET['user']) && isset($_GET['password']) && isset($_GET['otherUser'])) {
	include 'globalFunctions.php';
	$user      = strtolower($_GET['user']);
	$password  = $_GET['password'];
	$otherUser = strtolower($_GET['otherUser']);

	sameUser($user, $otherUser);
	$con = establishConnection();
	userAuthentication($user, $password, $con);

	if (strcasecmp($user, $otherUser) > 0) {
		$temp = $user;
		$user = $otherUser;
		$otherUser = $temp;
	}

	if (encountered($user, $otherUser, $con)) {
		$delete_from_encounter = "Delete from Encounter
								  Where User1='$user' And User2='$otherUser';";
		$delete_from_details = "Delete from EncounterDetails
								Where User1='$user' And User2='$otherUser';";
		$clear_proximity_count = "Delete from ProximityCount
								  Where User1='$user' And User2='$otherUser';";

		mysqli_query($con, $delete_from_encounter) or die('Failed: '.$delete_from_encounter);
		mysqli_query($con, $delete_from_details) or die('Failed: '.$delete_from_details);
		mysqli_query($con, $clear_proximity_count) or die('Failed: '.$clear_proximity_count);
	}

	@mysqli_close($con);
}
?>
