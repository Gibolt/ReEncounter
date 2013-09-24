<?php
if(isset($_GET['user1']) && isset($_GET['user2'])) {
	$user       = strtolower($_GET['user1']);
	$other_user = strtolower($_GET['user2']);
	$password   = $_GET['password'];

	$con = mysqli_connect('reencounter.cyzculuyt8xu.us-west-2.rds.amazonaws.com:3306','admin','encounter') or die('Cannot connect to the DB');
	mysqli_select_db($con,'ReEncounterDb') or die('Cannot select the DB');

	$select_user = "Select * From User
					Where User = '$user' And Password='$password';";
	if (mysqli_query($con, $select_user)) {
		return;
	}
	
	if (strcasecmp($user, $other_user) > 0) {
		$temp = $other_user;
		$other_user = $user;
		$user = $temp;
	}
	
	$select_encounter_details = "Select * from EncounterDetails 
						Where User1 = '$user' And User2 = '$other_user';";						
	$result_encounter_details = mysqli_query($con, $select_encounter_details) or die('Failed submission:  '.$select_encounter_details);
		
	$posts = array();
	while($post = mysqli_fetch_assoc($result_encounter_details)) {
		array_push($posts,$post);
	}
	header('Content-type: application/json');	
	echo json_encode(array('posts'=>$posts));

	@mysqli_close($con);
}
?>
