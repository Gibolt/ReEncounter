<?php
if(isset($_GET['user']) && isset($_GET['password']) && isset($_GET['otherUser']) && isset($_GET['timestamp']) && isset($_GET['who'])) {
	include 'globalFunctions.php';
	$user      = strtolower($_GET['user']);
	$password  = $_GET['password'];
	$otherUser = strtolower($_GET['otherUser']);
	$time      = $_GET['timestamp'];
	$who       = strtolower($_GET['who']);

	sameUser($user, $otherUser);
	$con = establishConnection();
	userAuthentication($user, $password, $con);

	if ($who == "sender") {
		$delete_message = "Delete from Message
						   Where Sender='$user' And Recipient='$otherUser' And Time = '$time' Limit 1;";
		mysqli_query($con, $delete_message) or die('Failed: '.$delete_message);
	}
	else if ($who == "recipient"){
		$delete_message = "Delete from Message
						   Where Sender='$otherUser' And Recipient='$user' And Time = '$time' Limit 1;";
		mysqli_query($con, $delete_message) or die('Failed: '.$delete_message);
	}

	@mysqli_close($con);
}
?>
