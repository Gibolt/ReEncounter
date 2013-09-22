<?php
if(isset($_GET['user']) && isset($_GET['password'])) {
	$user        = strtolower($_GET['user']);
	$password    = $_GET['password'];
	$email2      = strtolower($_GET['email2']);
	$phone1      = $_GET['phone1'];
	$phone2      = $_GET['phone2'];
	$description = $_GET['description'];

	$con = mysqli_connect('reencounter.cyzculuyt8xu.us-west-2.rds.amazonaws.com:3306','admin','encounter') or die('Cannot connect to the DB');
	mysqli_select_db($con,'ReEncounterDb') or die('Cannot select the DB');

	$select_user = "Select * from User 
					Where User = '$user' And Password = '$password';";
	if(!mysqli_query($con, $select_user)) {
		return;
	}

	$update_user_info = "Update UserInfo
					Set Email2='$email2', Phone1='$phone1', Phone2='$phone2', Description='$description');
					Where User = '$user'";
	mysqli_query($con, $update_user_info) or die('Failed submission:  '.$update_user_info);

	@mysqli_close($con);
}
?>