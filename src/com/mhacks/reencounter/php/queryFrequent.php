<?php
if(isset($_GET['user']) && isset($_GET['password']) && isset($_GET['num'])) {
	include 'globalFunctions.php';
	$user     = strtolower($_GET['user']);
	$password = $_GET['password'];
	$min_num  = intval($_GET['num']);

	if ($min_num < 2) {
		die();
	}

	$con = establishConnection();
	userAuthentication($user, $password, $con);

	$select_for_user = "Select User2 As Other_user, Times from ProximityCount 
						Where User1 = '$user'
						Union All
						Select User1 As Other_user, Times from ProximityCount 
						Where User2 = '$user'
						Order By Other_user;";
	$result_for_user = mysqli_query($con, $select_for_user) or die('Failed submission:  '.$select_for_user);

	$posts = array();
	while($post = mysqli_fetch_assoc($result_for_user)) {
		$times = intval($post['Times']);
		if( $times >= 2 && $times >= $min_num) {
			array($posts,$post);
		}
	}

	header('Content-type: application/json');
	echo json_encode(array('posts'=>$posts));

	@mysqli_close($con);
}
?>
