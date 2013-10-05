<?php
if(isset($_GET['user']) && isset($_GET['password']) && isset($_GET['otherUser'])) {
	include 'globalFunctions.php';
	$user      = strtolower($_GET['user']);
	$password  = $_GET['password'];
	$otherUser = strtolower($_GET['otherUser']);

	sameUser($user, $otherUser);
	$con = establishConnection();
	userAuthentication($user, $password, $con);

	if (strcasecmp($user, $otherUser) < 0) {
		$select_for_blocked = "Select Block1 from Blocked
						       Where User1 = '$user' And User2 = '$otherUser' And Block1 = 1 Limit 1;";
		$result_for_blocked = mysqli_query($con, $select_for_blocked) or die('Failed: '.$select_for_blocked);
		if (mysqli_num_rows($result_for_blocked) > 0) {
			$delete_from_block = "Delete from Blocked
								  Where User1='$user' And User2='$otherUser' Limit 1;";
			mysqli_query($con, $delete_from_block) or die('Failed: '.$delete_from_block);
		}
	}
	else {
		$select_for_blocked = "Select Block2 from Blocked
						       Where User1 = '$otherUser' And User2 = '$user' And Block2 = 1 Limit 1;";
		$result_for_blocked = mysqli_query($con, $select_for_blocked) or die('Failed: '.$select_for_blocked);

		if (mysqli_num_rows($result_for_blocked) > 0) {
			$delete_from_block = "Delete from Blocked
								  Where User1='$otherUser' And User2='$user' Limit 1;";
			mysqli_query($con, $delete_from_block) or die('Failed: '.$delete_from_block);
		}
	}

	@mysqli_close($con);
}
?>