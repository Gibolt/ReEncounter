<?php
if(isset($_GET['user'])) {
	$user     = strtolower($_GET['user']);
	$password = $_GET['password'];
	$min_num  = intval($_GET['num']);
	$format   = "json";

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
	$result_for_user = mysqli_query($con, $select_for_user) or die('Failed submission:  '.$select_for_user);
		header('Content-type: application/json');
		
	$posts = array();
	while($post = mysqli_fetch_assoc($result_for_user)) {
		$times = intval($post['Times']);
		if( $times >= 2 && $times >= $min_num) {
			$posts[] = array('post'=>$post);
		}
	}
	
	if($format == 'json') {
		echo json_encode(array('posts'=>$posts));
	}
	else {
		header('Content-type: text/xml');
		echo '<posts>';
		foreach($posts as $index => $post) {
			if(is_array($post)) {
				foreach($post as $key => $value) {
					echo '<',$key,'>';
					if(is_array($value)) {
						foreach($value as $tag => $val) {
							echo '<',$tag,'>',htmlentities($val),'</',$tag,'>';
						}
					}
					echo '</',$key,'>';
				}
			}
		}
		echo '</posts>';
	}

	@mysqli_close($con);
}
?>
