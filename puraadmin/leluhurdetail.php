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
$filepath = "http://project.it.unud.ac.id/purakawitan/puraadmin/";
// check for post data
if (isset($_GET["id_leluhur"])) {
    $id_leluhur = $_GET['id_leluhur'];
    //get rating

    // get a product from products table
    //$resultx = mysql_query("SELECT *FROM tb_pura_kawitan WHERE id_pura_kawitan = $id_pura_kawitan");
    $query = "SELECT *FROM tb_leluhur WHERE id_leluhur = '$id_leluhur'";
    $result = mysql_query($query) or die(mysql_error());
  
    //echo $query;
    if (!empty($result)) {
        // check for empty result
        if (mysql_num_rows($result) > 0) {
 
            $resultx = mysql_fetch_array($result);
 
            $product = array();
            $product["id_leluhur"] = $resultx["id_leluhur"];
            $product["nama"] = $resultx["nama_leluhur"];
            $product["sejarah"] = $resultx["deskripsi"];
                               
            $response["success"] = 1;
 
            // user node
            $response["product"] = array();
 
            array_push($response["product"], $product);
 
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
        // no product found
        $response["success"] = 0;
        $response["message"] = "No product found";
 
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