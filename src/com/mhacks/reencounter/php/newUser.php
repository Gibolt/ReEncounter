<?php
if(isset($_GET['user']) && isset($_GET['password']) && isset($_GET['email'])) {
	include 'globalFunctions.php';
	$user     = strtolower($_GET['user']);
	$password = $_GET['password'];
	$email    = strtolower($_GET['email']);

	$con = establishConnection();

	$insert_user = "Insert Into Users
					Values ('$user', '$email', '$password');";
	mysqli_query($con, $insert_user) or die('Failed submission:  '.$insert_user);

	$insert_user_info = "Insert Into UserInfo
					     Values ('$user', '$email', '', '', '', '', '');";
	mysqli_query($con, $insert_user_info) or die('Failed submission:  '.$insert_user_info);

	@mysqli_close($con);
}
?>