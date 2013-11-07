<?php
if(isset($_GET['user']) && isset($_GET['password']) && isset($_GET['otherUser']) && isset($_GET['message'])) {
	include 'globalFunctions.php';
	$user      = strtolower($_GET['user']);
	$password  = $_GET['password'];
	$otherUser = strtolower($_GET['otherUser']);
	$message   = trim($_GET['message']);

	$messageLen = strlen($message);
	if ($messageLen == 0) {
		return;
	}
	$message = mysqli_real_escape_string($con, $message);

	sameUser($user, $otherUser);
	$con = establishConnection();
	userAuthentication($user, $password, $con);
	if (encountered($user, $otherUser, $con)) {
		while ($messageLen > 0) {
			$messageShort = substr($message, 0, max(254,$messageLen));
			$add_message = "Insert into Message
							Values ('$user', '$otherUser', now(), '$message');";
			mysqli_query($con, $add_message) or die('Failed: '.$add_message);
			$messageLen = strlen($message);
		}
	}

	@mysqli_close($con);
}
?>
