<?php
	require "koneksi.php";
	$respon=array();
	$nama_kendaraan = $_POST["nama_kendaraan"];
	$no_mesin = $_POST["no_mesin"];
	$no_plat = $_POST["no_plat"];
	
	$qry1 = "INSERT INTO tb_kendaraan(nama_kendaraan, no_mesin, no_plat) VALUES('$nama_kendaraan', '$no_mesin', '$no_plat')";
	$res1 = mysqli_query($link, $qry1);
	if($res1){
			$respon["sukses"]=1;
			$respon["pesan"] = "Sukses";
			echo json_encode($respon);
		//echo json_encode($user);
	} else {
		$respon["sukses"]=0;
		$respon["pesan"] = "Invalid";
		echo json_encode($respon);
	}
	$link->close();
?>
