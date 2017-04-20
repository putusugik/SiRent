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
 
if (isset($_GET["id_user"])) {
    $id_user = $_GET['id_user'];


$result = mysql_query("SELECT *FROM tb_pura_kawitan WHERE id_user='$id_user' AND status='tidak valid' ORDER BY nama_pura_kawitan") or die(mysql_error());
 
// check for empty result
if (mysql_num_rows($result) > 0) {
    // looping through all results
    // products node
    $response["products"] = array();
 
    while ($row = mysql_fetch_array($result)) {
        // temp user array
        $product = array();
        $product["id"] = $row["id_pura_kawitan"];
            $product["namapurakawitan"] = $row["nama_pura_kawitan"];
            $product["alamat"] = $row["alamat"];
            $product["lat"] = $row["lat"];
            $product["lng"] = $row["lng"];
        // push single product into final response array
        array_push($response["products"], $product);
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
    echo json_encode($response);
    }
} else {
    // required field is missing
    $response["success"] = 0;
    $response["message"] = "Required field(s) is missing";
 
    // echoing JSON response
    echo json_encode($response);
}
?>