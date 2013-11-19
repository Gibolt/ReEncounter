<?php
if(isset($_GET['user']) && isset($_GET['password'])) {
    include 'globalFunctions.php';
    $user     = strtolower($_GET['user']);
    $password = $_GET['password'];
    $name     = $_GET['name'];
    $email    = (isset($_GET['email']))  ? strtolower($_GET['email'])   : '';
    $email2   = (isset($_GET['email2'])) ? strtolower($_GET['email2'])  : '';
    $phone    = (isset($_GET['phone']))  ? $_GET['phone']  : '';
    $phone2   = (isset($_GET['phone2'])) ? $_GET['phone2']  : '';
    $desc     = (isset($_GET['description'])) ? $_GET['description']  : '';
    $message  = (isset($_GET['message']))? $_GET['message']  : 0;
    if ($message != 1) $message = 0;

    $con = establishConnection();
    userAuthentication($user, $password, $con);

    $update_user_info = "Update UserInfo
                         Set Message=$message";
    if ($name != '')   $update_user_info .= ", Name='$name'";
    if ($email != '')  $update_user_info .= ", Email='$email'";
    if ($email2 != '') $update_user_info .= ", Email2='$email2'";
    if ($phone != '')  $update_user_info .= ", Phone='$phone'";
    if ($phone2 != '') $update_user_info .= ", Phone2='$phone2'";
    if ($desc != '')   $update_user_info .= ", Description='$desc'";
    $update_user_info .= "Where User = '$user' Limit 1;";
    mysqli_query($con, $update_user_info) or die('Failed: '.$update_user_info);

    header('Content-type: application/json');        
    $posts = array();
    array_push($posts, $user);
    echo json_encode(array('user'=>$posts));

    @mysqli_close($con);
}
?>