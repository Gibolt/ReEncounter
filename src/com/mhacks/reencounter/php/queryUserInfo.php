<?php
if(isset($_GET['user']) && isset($_GET['password'])) {
	include 'globalFunctions.php';
	$user      = strtolower($_GET['user']);
	$password  = $_GET['password'];
	$info_user = strtolower($_GET['infoUser']);

	$con = establishConnection();
	userAuthentication($user, $password, $con);

	$select_user_info = "";
	if (strcasecmp($user,$info_user) != 0) {
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
