<?php
if(isset($_GET['user']) && isset($_GET['password'])) {
	$user        = strtolower($_GET['user']);
	$password    = $_GET['password'];
	$email2      = strtolower($_GET['email2']);
	$phone       = intval($_GET['phone']);
	$phone2      = intval($_GET['phone2']);
	$description = $_GET['description'];

	$con = mysqli_connect('reencounter.cyzculuyt8xu.us-west-2.rds.amazonaws.com:3306','admin','encounter') or die('Cannot connect to the DB');
	mysqli_select_db($con,'ReEncounterDb') or die('Cannot select the DB');

	$select_user = "Select * from User 
					Where User = '$user' And Password = '$password';";
	$result_user = mysqli_query($con, $select_user) or die('Failed: '.$select_user);
	if(!$result_user) {
		return;
	}

	$update_user_info = "Update UserInfo
					Set Email2='$email2', Phone=$phone, Phone2=$phone2, Description='$description'
					Where User = '$user'";
	mysqli_query($con, $update_user_info) or die('Failed: '.$update_user_info);

	@mysqli_close($con);
}
?>