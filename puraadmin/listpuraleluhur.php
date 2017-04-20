<?php
 
/*
 * Following code will get single product details
 * A product is identified by product id (pid)
 */
 
// array for JSON response
$response = array();
 
// include db connect class
require_once  'db_connect.php';
 
// connecting to db
$db = new DB_CONNECT();

// check for post data
if (isset($_GET["id_leluhur"])) {
    $id_leluhur = $_GET['id_leluhur'];
    //get rating

    // get a product from products table
    //$resultx = mysql_query("SELECT *FROM tb_pura_kawitan WHERE id_pura_kawitan = $id_pura_kawitan");
    $query = "SELECT *FROM tb_pura_kawitan WHERE id_leluhur = '$id_leluhur' AND status = 'valid' ORDER BY nama_pura_kawitan";
    $result = mysql_query($query) or die(mysql_error());
    //echo $query;
        // check for empty result
        if (mysql_num_rows($result) > 0) {
 // products node
            $response["product"] = array();
 
            while ($resultx = mysql_fetch_array($result)) {
 
            $product = array();
            $product["id"] = $resultx["id_pura_kawitan"];
            $product["namapurakawitan"] = $resultx["nama_pura_kawitan"];
            $product["alamat"] = $resultx["alamat"];
             $product["lat"] = $resultx["lat"];
            $product["lng"] = $resultx["lng"];
            
            
            array_push($response["product"], $product);
            }
            $response["success"] = 1;
 
            // echoing JSON response
            echo json_encode($response);
        } else {
            // no product found
            $response["success"] = 0;
            $response["message"] = "No product foundss";
 
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