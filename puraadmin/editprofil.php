<?php
 
/*
 * Following code will update a product information
 * A product is identified by product id (pid)
 */
 
// array for JSON response
$response = array();

// check for required fields
if (isset($_POST['id_user']) && isset($_POST['username']) && isset($_POST['password']) && isset($_POST['nama_dpn']) && isset($_POST['nama_blkng']) && isset($_POST['alamat'])  && isset($_POST['email']) && isset($_POST['lat']) && isset($_POST['lng'])) {
    $id_user = $_POST['id_user'];
    $username = $_POST['username'];
    $password = $_POST['password'];
    $nama_depan = $_POST['nama_dpn'];
    $nama_belakang = $_POST['nama_blkng'];
    $alamat = $_POST['alamat'];
    $email = $_POST['email'];
    $lat = $_POST['lat'];
    $lng = $_POST['lng'];
   
   
    
   
 
    // include db connect class
    require_once 'db_connect.php';
 
    // connecting to db
    $db = new DB_CONNECT();
 
    // mysql update row with matched pid
    $result = mysql_query("UPDATE tb_user SET username = '$username', password = '$password', nama_depan = '$nama_depan', nama_belakang = '$nama_belakang', alamat = '$alamat', email = '$email', lat = '$lat', lng = '$lng' WHERE id_user = '$id_user'");
 
    // check if row inserted or not
    if ($result) {
        // successfully updated
        $response["success"] = 1;
        $response["message"] = "Places successfully updated.";
 
        // echoing JSON response
        echo json_encode($response);
    } else {
 
    }
} else {
    // required field is missing
    $response["success"] = 0;
    $response["message"] = "Required field(s) is missing";
 
    // echoing JSON response
    echo json_encode($response);
}
?>