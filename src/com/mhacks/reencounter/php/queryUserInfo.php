<?php
if(isset($_GET['user'])) {
	$user     = strtolower($_GET['user']);
	$password = $_GET['password'];
	$info_user= strtolower($_GET['infoUser']);
	
	$con = mysqli_connect('reencounter.cyzculuyt8xu.us-west-2.rds.amazonaws.com:3306','admin','encounter') or die('Cannot connect to the DB');
	mysqli_select_db($con,'ReEncounterDb') or die('Cannot select the DB');

	$select_user = "Select * From User
					Where User = '$user' And Password='$password';";
	if (mysqli_query($con, $select_user)) {
		return;
	}
	
	$select_user_info = "";
	if (strcmp($user,$info_user)) {
		$select_for_user = "Select User1 As User From ProximityCount
					Where User1 = '$info_user' And User2 = '$user' And Times >= 2";	
		if (strcasecmp($user, $info_user) < 0) {
			$select_for_user = "Select User2 As User From ProximityCount
						Where User1 = '$user' And User2 = '$info_user' And Times >= 2";	
		}	
		$select_user_info = "Select * from UserInfo
						Where User In ($select_for_user);";
	} 
	else {
		$select_user_info = "Select * from UserInfo
						Where User = '$user';";
	}
	
	$result_for_user = mysqli_query($con, $select_user_info) or die('Failed: '.$select_user_info);

	header('Content-type: application/json');		
	$posts = array();
	while($post = mysqli_fetch_assoc($result_for_user)) {
		array_push($posts, $post);
	}
	echo json_encode(array('posts'=>$posts));

	@mysqli_close($con);
}
?>
