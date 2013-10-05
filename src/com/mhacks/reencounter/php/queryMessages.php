<?php
if(isset($_GET['user']) && isset($_GET['password']) && isset($_GET['otherUser'])) {
	include 'globalFunctions.php';
	$user      = strtolower($_GET['user']);
	$password  = $_GET['password'];
	$otherUser = strtolower($_GET['otherUser']);

	sameUser($user, $otherUser);
	$con = establishConnection();
	userAuthentication($user, $password, $con);

	$select_messages_all = "Select * from Message
							Where (Sender='$user' And Recipient='$otherUser') Or (Sender='$otherUser' And Recipient='$user')
							Order By Time;";
	$result_messages_all = mysqli_query($con, $select_messages_all) or die('Failed: '.$select_messages_all);

	header('Content-type: application/json');		
	$posts = array();
	while($post = mysqli_fetch_assoc($result_messages_all)) {
		array_push($posts, $post);
	}
	echo json_encode(array('posts'=>$posts));

	@mysqli_close($con);
}
?>