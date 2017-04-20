<?php
 
/*
 * Following code will update a product information
 * A product is identified by product id (pid)
 */
 
// array for JSON response
$response = array();

// check for required fields
if (isset($_POST['id_pura_kawitan']) && isset($_POST['nama_pura_kawitan']) && isset($_POST['sejarah']) && isset($_POST['tahun_berdiri']) && isset($_POST['pendiri']) && isset($_POST['tahun_renovasi'])  && isset($_POST['lat']) && isset($_POST['lng']) && isset($_POST['alamat']) && isset($_POST['id_leluhur']) && isset($_POST['id_katagori_pura'])  && isset($_POST['nama_pemangku']) && isset($_POST['parents_id']) && isset($_POST['id_desa'])) {
    $id_pura_kawitan = $_POST['id_pura_kawitan'];
    $nama_pura_kawitan = $_POST['nama_pura_kawitan'];
    $sejarah = $_POST['sejarah'];
    $tahun_berdiri = $_POST['tahun_berdiri'];
    $pendiri = $_POST['pendiri'];
    $tahun_renovasi = $_POST['tahun_renovasi'];
    $lat = $_POST['lat'];
    $lng = $_POST['lng'];
    $alamat = $_POST['alamat'];
    $id_leluhur = $_POST['id_leluhur'];
    $id_katagori_pura = $_POST['id_katagori_pura'];
    $nama_pemangku = $_POST['nama_pemangku'];
    $parents_id = $_POST['parents_id'];
    $id_desa = $_POST['id_desa'];
    
   
 
    // include db connect class
    require_once 'db_connect.php';
 
    // connecting to db
    $db = new DB_CONNECT();
 
    // mysql update row with matched pid
    $result = mysql_query("UPDATE tb_pura_kawitan SET nama_pura_kawitan = '$nama_pura_kawitan', sejarah = '$sejarah', tahun_berdiri = '$tahun_berdiri', pendiri = '$pendiri', tahun_renovasi = '$tahun_renovasi', lat = '$lat', lng = '$lng', alamat = '$alamat', id_leluhur = '$id_leluhur', id_katagori_pura = '$id_katagori_pura', nama_pemangku = '$nama_pemangku', parents_id = '$parents_id', id_desa = '$id_desa' WHERE id_pura_kawitan = $id_pura_kawitan");
 
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
    $response["message"] = "Required field(s) is missing 
        .{$_POST['id_pura_kawitan']} .{$_POST['nama_pura_kawitan']} 
        .{$_POST['sejarah']} .{$_POST['tahun_berdiri']} .{$_POST['pendiri']} 
        .{$_POST['tahun_renovasi']} .{$_POST['lat']} .{$_POST['lng']} 
        .{$_POST['alamat']} .{$_POST['id_leluhur']} .{$_POST['id_katagori_pura']} .{$_POST['nama_pemangku']} 
        .{$_POST['parents_id']} .{$_POST['id_desa']}
    ";
 
    // echoing JSON response
    echo json_encode($response);
}
?>