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
$filepath = "http://project.it.unud.ac.id/purakawitan/gambar/";
// check for post data
if (isset($_GET["id_pura_kawitan"])) {
    $id_pura_kawitan = $_GET['id_pura_kawitan'];
    //get rating

    // get a product from products table
    //$resultx = mysql_query("SELECT *FROM tb_pura_kawitan WHERE id_pura_kawitan = $id_pura_kawitan");
    $query = "SELECT
    tb_pura_kawitan.id_pura_kawitan
    , tb_pura_kawitan.nama_pura_kawitan
    , tb_pura_kawitan.sejarah
    , tb_pura_kawitan.tahun_berdiri
    , tb_pura_kawitan.pendiri
    , tb_pura_kawitan.tahun_renovasi
    , tb_pura_kawitan.lat
    , tb_pura_kawitan.lng
    , tb_pura_kawitan.alamat
    , tb_kabupaten.id_kabupaten
    , tb_kabupaten.nama_kabupaten
    , tb_kecamatan.id_kecamatan
    , tb_kecamatan.nama_kecamatan
    , tb_desa.id_desa
    , tb_desa.nama_desa
    , tb_leluhur.id_leluhur
    , tb_leluhur.nama_leluhur
    , tb_pura_kawitan.nama_pemangku
    , tb_katagori_pura.id_katagori_pura
    , tb_katagori_pura.nama_katagori_pura
    , tb_pura_kawitan.parents_id
    , tb_pura_kawitan.gambar

FROM
    tb_kecamatan
    LEFT JOIN tb_kabupaten 
        ON (tb_kecamatan.id_kabupaten = tb_kabupaten.id_kabupaten)
    LEFT JOIN tb_desa 
        ON (tb_desa.id_kecamatan = tb_kecamatan.id_kecamatan)
    LEFT JOIN tb_pura_kawitan 
        ON (tb_pura_kawitan.id_desa = tb_desa.id_desa)
    LEFT JOIN tb_leluhur 
        ON (tb_pura_kawitan.id_leluhur = tb_leluhur.id_leluhur)
    LEFT JOIN tb_katagori_pura 
        ON (tb_pura_kawitan.id_katagori_pura = tb_katagori_pura.id_katagori_pura)
    WHERE tb_pura_kawitan.id_pura_kawitan = '$id_pura_kawitan'";
    $result = mysql_query($query) or die(mysql_error());
    //echo $query;
    if (!empty($result)) {
        // check for empty result
        if (mysql_num_rows($result) > 0) {
 
            $resultx = mysql_fetch_array($result);
 
            $product = array();
            $product["id_pura_kawitan"] = $resultx["id_pura_kawitan"];
            $product["nama_pura_kawitan"] = $resultx["nama_pura_kawitan"];
            $product["sejarah"] = $resultx["sejarah"];
            $product["tahun_berdiri"] = $resultx["tahun_berdiri"];
            $product["pendiri"] = $resultx["pendiri"];
            $product["tahun_renovasi"] = $resultx["tahun_renovasi"];
            $product["lat"] = $resultx["lat"];
            $product["lng"] = $resultx["lng"];
            $product["alamat"] = $resultx["alamat"];
            $product["id_leluhur"] = $resultx["id_leluhur"];
            $product["nama_leluhur"] = $resultx["nama_leluhur"];           
            $product["id_katagori_pura"] = $resultx["id_katagori_pura"];
            $product["nama_katagori_pura"] = $resultx["nama_katagori_pura"];
            $product["nama_pemangku"] = $resultx["nama_pemangku"];        
            $product["parents_id"] = $resultx["parents_id"];
            $product["id_kabupaten"] = $resultx["id_kabupaten"];
            $product["nama_kabupaten"] = $resultx["nama_kabupaten"];
            $product["id_kecamatan"] = $resultx["id_kecamatan"];
            $product["nama_kecamatan"] = $resultx["nama_kecamatan"];
            $product["id_desa"] = $resultx["id_desa"];
            $product["nama_desa"] = $resultx["nama_desa"];
            $product["gambar"] = $filepath.$resultx["gambar"];                      
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