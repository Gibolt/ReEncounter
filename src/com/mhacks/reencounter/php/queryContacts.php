<?php
if(isset($_GET['user']) && isset($_GET['password'])) {
	include 'globalFunctions.php';
	$user      = strtolower($_GET['user']);
	$password  = $_GET['password'];

	$con = establishConnection();
	userAuthentication($user, $password, $con);

	$select_contacts = "Select Contact from Contacts
						Where User='$user'";
	$result_contacts = mysqli_query($con, $select_contacts) or die('Failed: '.$select_contacts);

	header('Content-type: application/json');		
	$posts = array();
	while($post = mysqli_fetch_assoc($result_contacts)) {
		array_push($posts, $post);
	}
	echo json_encode(array('posts'=>$posts));

	@mysqli_close($con);
}
?>
