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
	
	if (strcasecmp($user, $other_user) < 0) {
		$select_for_times = "Select Times from ProximityCount 
						 Where User1 = '$user' And User2 = '$otherUser' And Times > 1";			
		$result_for_times = mysqli_query($con, $select_for_times) or die('Failed: '.$select_for_times);
	
		if ($mysqli_num_rows($result_for_times) > 0) {
			$add_block = "Insert into Block
						  Values ('$user', '$otherUser', 1, 0);";
			mysqli_query($con, $add_block) or die('Failed: '.$add_block);
		}
	}
	else {
		$select_for_times = "Select Times from ProximityCount 
						 Where User1 = '$otherUser' And User2 = '$user' And Times > 1;";			
		$result_for_times = mysqli_query($con, $select_for_times) or die('Failed: '.$select_for_times);
		
		if ($mysqli_num_rows($result_for_times) > 0) {
			$add_block = "Insert into Block
						  Values ('$otherUser', '$user', 0, 1);";
			mysqli_query($con, $add_block) or die('Failed: '.$add_block);
		}		
	}
	else {
		echo "Other user does not exist";
	}
	
	@mysqli_close($con);
}
?>