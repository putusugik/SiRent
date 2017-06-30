<?php
require "koneksi.php";
$respon = array();
$rating = $_POST["rating"];
$ulasan = $_POST["ulasan"];
$ID_user = $_POST["ID_user"];

$qry = "insert into tb_ratinguser (rating, ulasan, ID_user) values
		('$rating','$ulasan','$ID_user');";
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