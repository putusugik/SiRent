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
if (isset($_GET["id_user"])) {
    $id_user = $_GET['id_user'];
    //get rating

    // get a product from products table
    //$resultx = mysql_query("SELECT *FROM tb_pura_kawitan WHERE id_pura_kawitan = $id_pura_kawitan");
    $query = "SELECT
    tb_pengurus.id_pengurus
    , tb_pengurus.id_user
    , tb_pengurus.id_pura_kawitan
    , tb_pura_kawitan.nama_pura_kawitan
    , tb_pura_kawitan.alamat
    
FROM
    tb_pura_kawitan
    LEFT JOIN tb_pengurus 
        ON (tb_pengurus.id_pura_kawitan = tb_pura_kawitan.id_pura_kawitan)
    WHERE tb_pengurus.id_user = '$id_user' AND tb_pengurus.status = 'operator'";
    $result = mysql_query($query) or die(mysql_error());
    //echo $query;
        // check for empty result
        if (mysql_num_rows($result) > 0) {
 // products node
            $response["product"] = array();
 
            while ($resultx = mysql_fetch_array($result)) {
 
            $product = array();
            $product["id_pengurus"] = $resultx["id_pengurus"];
            $product["id_user"] = $resultx["id_user"];
            $product["id_pura_kawitan"] = $resultx["id_pura_kawitan"];
            $product["namapurakawitan"] = $resultx["nama_pura_kawitan"];
            $product["alamat"] = $resultx["alamat"];
            
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