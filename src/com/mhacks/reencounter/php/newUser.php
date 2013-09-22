<?php
if(isset($_GET['user']) && isset($_GET['password']) && isset($_GET['email'])) {
	$user     = strtolower($_GET['user']);
	$password = $_GET['password'];
	$email    = strtolower($_GET['email']);

	$con = mysqli_connect('reencounter.cyzculuyt8xu.us-west-2.rds.amazonaws.com:3306','admin','encounter') or die('Cannot connect to the DB');
	mysqli_select_db($con,'ReEncounterDb') or die('Cannot select the DB');

	$select_user = "Select * from User 
					Where User = '$user' And Email = '$email';";
	if(mysqli_query($con, $select_user)) {
		return;
	}

	$insert_user = "Insert Into Users
					Values ('$user', '$email', '$password');";
	mysqli_query($con, $insert_user) or die('Failed submission:  '.$insert_user);

	$insert_user_info = "Insert Into UserInfo
					Values ('$user', '', '', '', '', '');";
	mysqli_query($con, $insert_user_info) or die('Failed submission:  '.$insert_user_info);

	@mysqli_close($con);
}
?>