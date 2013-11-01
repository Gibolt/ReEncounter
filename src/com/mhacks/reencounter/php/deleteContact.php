<?php
if(isset($_GET['user']) && isset($_GET['password']) && isset($_GET['otherUser'])) {
	include 'globalFunctions.php';
	$user      = strtolower($_GET['user']);
	$password  = $_GET['password'];
	$otherUser = strtolower($_GET['otherUser']);

	sameUser($user, $otherUser);
	$con = establishConnection();
	userAuthentication($user, $password, $con);

	$delete_contact = "Delete from Contacts
					   Where User='$user' And Contact='$otherUser'
					   Limit 1;";
	mysqli_query($con, $delete_contact) or die('Failed: '.$delete_contact);

	@mysqli_close($con);
}
?>
