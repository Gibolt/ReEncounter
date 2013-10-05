<?php
if(isset($_GET['user']) && isset($_GET['password']) && isset($_GET['otherUser']) && isset($_GET['message'])) {
	include 'globalFunctions.php';
	$user      = strtolower($_GET['user']);
	$password  = $_GET['password'];
	$otherUser = strtolower($_GET['otherUser']);
	$message   = $_GET['message'];

	sameUser($user, $otherUser);
	$con = establishConnection();
	userAuthentication($user, $password, $con);

	if (encountered($user, $otherUser, $con)) {
		$add_message = "Insert into Message
					    Values ('$user', '$otherUser', now(), '$message');";
		mysqli_query($con, $add_message) or die('Failed: '.$add_message);
	}

	@mysqli_close($con);
}
?>