<?
// Creates database connection
function establishConnection() {
	$con = mysqli_connect('Endpoint','Username','Password') or die('Cannot connect to the DB');
	mysqli_select_db($con,'Dbname') or die('Cannot select the DB');
	return $con;
}

// Kills the submission if login credentials invalid
function userAuthentication($user, $password, $con) {
	$select_user = "Select 1 From Users
					Where User = '$user' And Password='$password' Limit 1;";
	$result_user = mysqli_query($con, $select_user) or die('Failed: '.$select_user);
	$rows = mysqli_num_rows($result_user);
	if ($rows == 0) {
		die();
	}
}

// Shorthand for running a query or killing if it fails
function query($con, $mysqlQuery) {
	return mysqli_query($con, $mysqlQuery) or die('Failed:  '.$mysqlQuery);
}

// Returns true if users are correlated with at least two encounters
function encountered($user, $otherUser, $con) {
	if (strcasecmp($user, $otherUser) > 0) {
		$temp = $user;
		$user = $otherUser;
		$otherUser = $temp;
	}
	$select_for_times = "Select Times from ProximityCount 
						 Where User1 = '$user' And User2 = '$otherUser' And Times > 1 Limit 1;";
	$result_for_times = mysqli_query($con, $select_for_times) or die('Failed: '.$select_for_times);
	if (mysqli_num_rows($result_for_times) > 0) {
		return 1;
	}
	else {
		return 0;
	}
}

// Kills the submission if the input users are the same
function sameUser($user, $otherUser) {
	if ($user == $otherUser) {
		die();
	}
}
?>