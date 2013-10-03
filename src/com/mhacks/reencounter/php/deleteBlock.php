<?php
if(isset($_GET['user']) && isset($_GET['password']) && isset($_GET['otherUser'])) {
	$user      = strtolower($_GET['user']);
	$password  = $_GET['password'];
	$otherUser = strtolower($_GET['otherUser']);
	
	$con = mysqli_connect('reencounter.cyzculuyt8xu.us-west-2.rds.amazonaws.com:3306','admin','encounter') or die('Cannot connect to the DB');
	mysqli_select_db($con,'ReEncounterDb') or die('Cannot select the DB');

	if ($user == $otherUser) {
		echo "Invalid entry";
		return;
	}
	
	$select_user = "Select * From User
					Where User = '$user' And Password='$password';";
	if (mysqli_query($con, $select_user)) {
		echo "User Authentication Failed";
		return;
	}
	
	if (strcasecmp($user, $otherUser) < 0) {
		$select_for_blocked = "Select Block1 from Blocked 
						       Where User1 = '$user' And User2 = '$otherUser' And Block1 = 1;";			
		$result_for_blocked = mysqli_query($con, $select_for_blocked) or die('Failed: '.$select_for_blocked);
	
		if ($mysqli_num_rows($result_for_blocked) > 0) {
			$delete_from_block = "Delete from Encounter
								      Where User1='$user' And User2='$otherUser';"; 
			mysqli_query($con, $delete_from_block) or die('Failed: '.$delete_from_block);
		}
	}
	else {
		$select_for_blocked = "Select Block2 from Blocked 
						       Where User1 = '$otherUser' And User2 = '$user' And Block2 = 1;";			
		$result_for_blocked = mysqli_query($con, $select_for_blocked) or die('Failed: '.$select_for_blocked);
	
		if ($mysqli_num_rows($result_for_blocked) > 0) {
			$delete_from_block = "Delete from Encounter
								      Where User1='$otherUser' And User2='$user';"; 
			mysqli_query($con, $delete_from_block) or die('Failed: '.$delete_from_block);
		}	
	}
	
	@mysqli_close($con);
}
?>