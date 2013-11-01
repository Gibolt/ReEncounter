<?php
if(isset($_GET['user']) && isset($_GET['password'])) {
	include 'globalFunctions.php';
	$user      = strtolower($_GET['user']);
	$password  = $_GET['password'];

	$con = establishConnection();
	userAuthentication($user, $password, $con);

	header('Content-type: application/json');		
	$posts = array();
	array_push($posts, $user);
	echo json_encode(array('user'=>$posts));

	@mysqli_close($con);
}
?>
