<?php
if(isset($_GET['user']) && isset($_GET['password']) && isset($_GET['max'])) {
    include 'globalFunctions.php';
    $user     = strtolower($_GET['user']);
    $password = $_GET['password'];
    $min      = 2;
    $max      = intval($_GET['max']);

    if ($max < $min) {
        die();
    }

    $con = establishConnection();
    userAuthentication($user, $password, $con);

    $select_for_user = "Select User2 As Other_user, Times from ProximityCount
                        Where User1 = '$user' And Times >= $min And Times <= $max
                        Union All
                        Select User1 As Other_user, Times from ProximityCount 
                        Where User2 = '$user' And Times >= $min And Times <= $max
                        Order By Other_user;";
    $result_for_user = mysqli_query($con, $select_for_user) or die('Failed: '.$select_for_user);

    $posts = array();
    while($post = mysqli_fetch_assoc($result_for_user)) {
        array_push($posts,$post);
    }

    header('Content-type: application/json');
    echo json_encode(array('posts'=>$posts));

    @mysqli_close($con);
}
?>
