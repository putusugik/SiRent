<?php

$response = array();
 
// check for required fields
//echo isset($_POST['nama_pura_kawitan']);
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
    $nama_pemangku = $_POST['pemangku'];
    $parents_id = $_POST['parents_id'];
    $id_desa = $_POST['id_desa'];
    $id_user = $_POST['id_user'];
     $waktu = $_POST['waktu'];

   

    // include db connect class
    require_once 'db_connect.php';
 
    // connecting to db
    $db = new DB_CONNECT();
 
    // mysql inserting a new row
    $result = mysql_query("INSERT INTO tb_pura_kawitan(nama_pura_kawitan, sejarah, tahun_berdiri, pendiri,tahun_renovasi, lat, lng, alamat, id_leluhur, id_katagori_pura, nama_pemangku, parents_id, id_desa, id_user, status, waktu) VALUES('$nama_pura_kawitan', '$sejarah', '$tahun_berdiri', '$pendiri', '$tahun_renovasi', '$lat','$lng', '$alamat','$id_leluhur', '$id_katagori_pura', '$nama_pemangku', '$parents_id', '$id_desa','$id_user', 'tidak valid', '$waktu')") or die(mysql_error());


    if ($result) {
        // successfully inserted into database
        $response["success"] = 1;
        $response["message"] = mysql_insert_id();
        mkdir('../gambar/'.$response["message"]);
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