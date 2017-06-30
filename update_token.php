<?php
$respon=array();
require "koneksi.php";
$id = $_POST["ID_user"];
$token = $_POST["token"];

	$qry1 = "UPDATE tb_user SET token = '$token' WHERE ID = $id;";
	$res1 = mysqli_query($link, $qry1);
	if($res1){
		$respon["sukses"]=1;
		$respon["pesan"] = "Sukses";
		echo json_encode($respon);
	} else {
		$respon["sukses"]=0;
		$respon["pesan"] = mysqli_error($link);
		echo json_encode($respon);	
	}

?>
