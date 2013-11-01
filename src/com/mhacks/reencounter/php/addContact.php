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
		$add_contact = "Insert into Contacts
					    Values ('$user', '$otherUser');";
		mysqli_query($con, $add_contact) or die('Failed: '.$add_contact);
	}

	@mysqli_close($con);
}
?>
