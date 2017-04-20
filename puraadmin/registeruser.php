 <?php

$response = array();
 
// check for required fields
//echo isset($_POST['nama_pura_kawitan']);
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
 
    // mysql inserting a new row
    $result = mysql_query("INSERT INTO tb_user(username, password, nama_depan, nama_belakang, alamat, email, lat, lng, level) VALUES('$username', '$password', '$nama_depan', '$nama_belakang', '$alamat', '$email','$lat', '$lng','user')") or die(mysql_error());


    if ($result) {
        // successfully inserted into database
        $response["success"] = 1;
        $response["message"] = mysql_insert_id();
        // echoing JSON response
        echo json_encode($response);
    } else {
        // failed to insert row
        $response["success"] = 0;
        $response["message"] = "Oops! An error occurred.";
 
        // echoing JSON response
        echo json_encode($response);
    }
 
?>