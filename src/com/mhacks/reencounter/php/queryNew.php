<?php
if(isset($_GET['user'])) {
	$user     = strtolower($_GET['user']);
	$password = $_GET['password'];
	$min_num  = 2;
	$max_num  = intval($_GET['num']);
	
	if (max_num < min_num) {
		return;
	}
	
	$con = mysqli_connect('reencounter.cyzculuyt8xu.us-west-2.rds.amazonaws.com:3306','admin','encounter') or die('Cannot connect to the DB');
	mysqli_select_db($con,'ReEncounterDb') or die('Cannot select the DB');

	$select_user = "Select * From User
					Where User = '$user' And Password='$password';";
	if (mysqli_query($con, $select_user)) {
		return;
	}
	
	$select_for_user = "Select User2 As Other_user, Times from ProximityCount 
						Where User1 = '$user'
						Union All
						Select User1 As Other_user, Times from ProximityCount 
						Where User2 = '$user'
						Order By Other_user;";			
	$result_for_user = mysqli_query($con, $select_for_user) or die('Failed: '.$select_for_user);
		
	$posts = array();
	while($post = mysqli_fetch_assoc($result_for_user)) {
		$times = intval($post['Times']);
		if( $times >= min_num && $times <= $max_num) {
			array_push($posts,$post);
		}
	}
	
	header('Content-type: application/json');
	echo json_encode(array('posts'=>$posts));

	@mysqli_close($con);
}
?>
