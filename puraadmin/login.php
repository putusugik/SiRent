<?php
require_once 'db_connect.php';
$db = new DB_CONNECT();
    
    $password=$_POST["password"]; 
    $username=$_POST["username"]; 
    
    if (!empty($_POST)) { 
        if (empty($_POST['username']) || empty($_POST['password'])) 
        { 
            // Create some data that will be the JSON response 
            $response["success"] = 0; 
            $response["message"] = ""; 
            //shown to the users 
            die(json_encode($response)); 
        } 

      
        $query1 = "SELECT * FROM tb_user WHERE username = '$username' AND password='$password' AND level='user'"; 
        

        $sql1 = mysql_query($query1) or die(mysql_error()); 
        $row1 = mysql_num_rows($sql1);
        


        if ($row1!=0) {
            $data = mysql_fetch_array($sql1);
        	$response["success"] = 1; 
                    $response["message"] = "You have been sucessfully logged as user";
                    $response["id_user"] = $data['id_user'];
                    die(json_encode($response));
        } else {

        	$response["success"] = 0; 
            $response["message"] = "Invalid username or password"; 
            die(json_encode($response));
}}
         else { 
        $response["success"] = 0; 
        $response["message"] = "One or both of the fields are empty "; 
        die(json_encode($response)); 
    }
?>