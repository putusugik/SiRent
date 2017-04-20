<?php

$response = array();
 
// check for required fields
//echo isset($_POST['nama_pura_kawitan']);
    $id_pura_kawitan = $_POST['id_pura_kawitan'];
    $alasan = $_POST['alasan'];
    $id_user = $_POST['id_user'];
    $waktu = $_POST['waktu'];

   

    // include db connect class
    require_once 'db_connect.php';
 
    // connecting to db
    $db = new DB_CONNECT();
 
    // mysql inserting a new row
    $result = mysql_query("INSERT INTO tb_pengurus(id_pura_kawitan, id_user, alasan,  status, waktu) VALUES('$id_pura_kawitan', '$id_user', '$alasan', 'operator tidak valid', '$waktu')") or die(mysql_error());


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