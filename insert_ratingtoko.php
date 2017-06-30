<?php
require "koneksi.php";
$respon = array();
$rating = $_POST["rating"];
$ulasan = $_POST["ulasan"];
$ID_toko = $_POST["ID_toko"];

$qry = "insert into tb_ratingtoko (rating, ulasan, ID_toko) values
		('$rating','$ulasan','$ID_toko');";
$res = mysqli_query($link, $qry);
if($res){
	$respon["sukses"]=1;
	$respon["pesan"] = "Sukses";
	echo json_encode($respon);
} else{
	$respon["sukses"]=0;
	$respon["pesan"] = "Error";
	echo json_encode($respon);
}


?>