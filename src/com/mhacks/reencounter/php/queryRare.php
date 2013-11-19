<?php
if(isset($_GET['user']) && isset($_GET['password']) && isset($_GET['min']) && isset($_GET['max'])) {
    include 'globalFunctions.php';
    $user     = strtolower($_GET['user']);
    $password = $_GET['password'];
    $min      = intval($_GET['min']);
    $max      = intval($_GET['max']);

    if ($max < $min || $min < 2) {
        die();
    }

    $con = establishConnection();
    userAuthentication($user, $password, $con);

    $select_for_user = "Select User2 As otherUser, Times from ProximityCount 
                        Where User1 = '$user'
                        Union All
                        Select User1 As otherUser, Times from ProximityCount 
                        Where User2 = '$user'
                        Order By otherUser;";            
    $result_for_user = mysqli_query($con, $select_for_user) or die('Failed: '.$select_for_user);

    $posts = array();
    while($post = mysqli_fetch_assoc($result_for_user)) {
        $times = intval($post['Times']);
        if( $times > 2 && $times >= $min && $times <= $max) {
            array_push($posts, $post);
        }
    }

    header('Content-type: application/json');
    echo json_encode(array('posts'=>$posts));

    @mysqli_close($con);
}
?>
