<?php
if(isset($_GET['user']) && isset($_GET['password'])) {
	include 'globalFunctions.php';
	$user      = strtolower($_GET['user']);
	$password  = $_GET['password'];
	$otherUser = strtolower($_GET['otherUser']);

	sameUser($user, $otherUser);
	$con = establishConnection();
	userAuthentication($user, $password, $con);

	$select_message_update = "Select LastCheck from MessageUpdate
							  Where User='$user'
							  Limit 1";
	$select_messages = "Select * from Message
						Where Recipient='$user' And Time >= ($select_message_update)
						Order By Time Desc;";
	$date = date('Y-m-d H:i:s',time());
	$update_message_update = "Update MessageUpdate
							  Set LastCheck='$date'
							  Where User='$user'
							  Limit 1;";
	$result_messages = mysqli_query($con, $select_messages) or die('Failed: '.$select_messages);
	mysqli_query($con, $update_message_update) or die('Failed: '.$update_message_update);

	header('Content-type: application/json');		
	$posts = array();
	while($post = mysqli_fetch_assoc($result_messages)) {
		array_push($posts, $post);
	}
	echo json_encode(array('posts'=>$posts));

	@mysqli_close($con);
}
?>
