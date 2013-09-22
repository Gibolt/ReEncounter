<?php
if(isset($_POST['user']) && isset($_POST['password']) ) {
	$user     = strtolower($_POST['user']);
	$password = $_POST['password'];
	$max_num  = intval($_POST['num']);

	$con = mysqli_connect('reencounter.cyzculuyt8xu.us-west-2.rds.amazonaws.com:3306','admin','encounter') or die('Cannot connect to the DB');
	mysqli_select_db($con,'ReEncounterDb') or die('Cannot select the DB');

	$select_for_user = "Select User2 As Other_user, Times from ProximityCount 
						Where User1 = '$user'
						Union All
						Select User1 As Other_user, Times from ProximityCount 
						Where User2 = '$user'
						Order By Other_user";					
	$result_for_user = mysqli_query($con, $select_for_user) or die('Failed submission:  '.$select_for_user);
	
	
	$posts = array();
	$result_row_count = mysqli_num_rows($result_for_user);
	if(result_row_count >= 2 && result_row_count < $max_num) {
		
		while($post = mysql_fetch_assoc($result_for_user)) {
			$posts[] = array('post'=>$post);
		}
	}
	
	if($format == 'json') {
		header('Content-type: application/json');
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