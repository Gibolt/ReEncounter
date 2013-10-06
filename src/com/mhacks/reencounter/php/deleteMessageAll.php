<?php
if(isset($_GET['user']) && isset($_GET['password']) && isset($_GET['otherUser'])) {
	include 'globalFunctions.php';
	$user      = strtolower($_GET['user']);
	$password  = $_GET['password'];
	$otherUser = strtolower($_GET['otherUser']);

	sameUser($user, $otherUser);
	$con = establishConnection();
	userAuthentication($user, $password, $con);

	$delete_message_all = "Delete from Message
						   Where (Sender='$user' And Recipient='$otherUser') Or (Sender='$otherUser' And Recipient='$user');";
	mysqli_query($con, $delete_message_all) or die('Failed: '.$delete_message_all);
	
	@mysqli_close($con);
}
?>
