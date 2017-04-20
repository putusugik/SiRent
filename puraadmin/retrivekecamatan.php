<?php
 
/*
 * Following code will list all the products
 */
 
// array for JSON response
$response = array();
 
// include db connect class
require_once 'db_connect.php';
 
// connecting to db
$db = new DB_CONNECT();
$idKabupaten = 0;
if(isset($_POST['id_kabupaten']))
     $idKabupaten = $_POST['id_kabupaten'];


$query = "SELECT *FROM tb_kecamatan where id_kabupaten = {$idKabupaten} ORDER BY nama_kecamatan";
$result = mysql_query($query) or die(mysql_error());
 
// check for empty result
if (mysql_num_rows($result) > 0) {
    // looping through all results
    // products node
    $response["product"] = array();
 
    while ($row = mysql_fetch_array($result)) {
        // temp user array
        $product = array();
        $product["id_kecamatan"] = $row["id_kecamatan"];
         $product["nama_kecamatan"] = $row["nama_kecamatan"];
            
        // push single product into final response array
        array_push($response["product"], $product);
    }
    // success
    $response["success"] = 1;
 
    // echoing JSON response
    echo json_encode($response);
} else {
    // no products found
    $response["success"] = 0;
    $response["message"] = "No object found";
 
    // echo no users JSON
    //echo $query;
    echo json_encode($response);
}
?>