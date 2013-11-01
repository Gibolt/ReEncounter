<?php
if(isset($_GET['user']) && isset($_GET['password'])) {
	include 'globalFunctions.php';
	$user        = strtolower($_GET['user']);
	$password    = $_GET['password'];
	$name        = $_GET['name'];
	$email       = strtolower($_GET['email']);
	$email2      = strtolower($_GET['email2']);
	$phone       = $_GET['phone'];
	$phone2      = $_GET['phone2'];
	$message     = $_GET['message'];
	$description = $_GET['description'];
	if ($message != 1) $message = 0;

	$con = establishConnection();
	userAuthentication($user, $password, $con);

	$update_user_info = "Update UserInfo
						 Set Name='$name', Email='$email', Email2='$email2', Phone='$phone', Phone2='$phone2', Message=$message, Description='$description'
						 Where User = '$user' Limit 1;";
	mysqli_query($con, $update_user_info) or die('Failed: '.$update_user_info);

	@mysqli_close($con);
}
?>