<?php
	$respon=array();
	require "koneksi.php";
		$ID_kend = $_POST["ID_kendaraan"];
		$qry1 = "UPDATE tb_dttoko_kendaraan SET st_sewa = 5 WHERE ID_kendaraan = $ID_kend; ";
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
