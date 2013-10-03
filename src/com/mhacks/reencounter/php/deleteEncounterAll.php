<?php
if(isset($_GET['user']) && isset($_GET['password']) && isset($_GET['otherUser'])) {
	$user      = strtolower($_GET['user']);
	$password  = $_GET['password'];
	$otherUser = strtolower($_GET['otherUser']);
	$user1     = $otherUser;
	$user2     = $user;
	
	$con = mysqli_connect('reencounter.cyzculuyt8xu.us-west-2.rds.amazonaws.com:3306','admin','encounter') or die('Cannot connect to the DB');
	mysqli_select_db($con,'ReEncounterDb') or die('Cannot select the DB');

	$select_user = "Select * From User
					Where User = '$user' And Password='$password';";
	if (mysqli_query($con, $select_user)) {
		echo "User Authentication Failed";
		return;
	}
	
	if (strcasecmp($user, $other_user) < 0) {
		$user1 = $user;
		$user2 = $otherUser;
	}
	
	$select_for_times = "Select Times from ProximityCount 
						 Where User1 = '$user1' And User2 = '$user2' And Times > 1";			
	$result_for_times = mysqli_query($con, $select_for_times) or die('Failed: '.$select_for_times);
	
	if ($mysqli_num_rows($result_for_times) > 0) {
		$delete_from_encounter = "Delete from Encounter
								  Where User1='$user1' And User2='$user2';"; 
		$reduce_proximity_count = "Update ProximityCount
								   Set times=0
								   Where User1='$user1' And User2='$user2';"; 
					
		mysqli_query($con, $delete_from_encounter) or die('Failed: '.$delete_from_encounter);
		mysqli_query($con, $reduce_proximity_count) or die('Failed: '.$reduce_proximity_count);
	}
	else {
		echo "Other user does not exist";
	}
	
	@mysqli_close($con);
}
?>
