<?php
	require "koneksi.php";
	$respon = array();
	$ID = $_POST["ID"];
	$mysql_qry = "UPDATE tb_user SET `status` = 2 where ID = $ID";
	$result = mysqli_query($link ,$mysql_qry);
	if($result){
		$respon["sukses"]=1;
		$respon["pesan"] = "Sukses";
		echo json_encode($respon);
	}else {
		$respon["sukses"]=0;
		$respon["pesan"] = mysqli_error($link);
		echo json_encode($respon);
	}
	
?>