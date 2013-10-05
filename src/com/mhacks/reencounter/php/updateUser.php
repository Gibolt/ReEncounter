<?php
if(isset($_GET['user']) && isset($_GET['password'])) {
	include 'globalFunctions.php';
	$user        = strtolower($_GET['user']);
	$password    = $_GET['password'];
	$email2      = strtolower($_GET['email2']);
	$phone       = $_GET['phone'];
	$phone2      = $_GET['phone2'];
	$description = $_GET['description'];

	$con = establishConnection();
	userAuthentication($user, $password, $con);

	$update_user_info = "Update UserInfo
						 Set Email2='$email2', Phone='$phone', Phone2='$phone2', Description='$description'
						 Where User = '$user';";
	mysqli_query($con, $update_user_info) or die('Failed: '.$update_user_info);

	@mysqli_close($con);
}
?>