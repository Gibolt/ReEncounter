<?php
if(isset($_GET['user']) && isset($_GET['password']) && isset($_GET['otherUser'])) {
	include 'globalFunctions.php';
	$user      = strtolower($_GET['user']);
	$password  = $_GET['password'];
	$otherUser = strtolower($_GET['otherUser']);

	sameUser($user, $otherUser);
	$con = establishConnection();
	userAuthentication($user, $password, $con);

	if (encountered($user, $otherUser, $con)) {
		if (strcasecmp($user, $otherUser) < 0) {
			$add_block = "Insert into Blocked
						  Values ('$user', '$otherUser', 1, 0);";
			mysqli_query($con, $add_block) or die('Failed: '.$add_block);
		}
		else {
			$add_block = "Insert into Blocked
						  Values ('$otherUser', '$user', 0, 1);";
			mysqli_query($con, $add_block) or die('Failed: '.$add_block);	
		}
	}

	@mysqli_close($con);
}
?>