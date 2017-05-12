<?php
	require "koneksi.php";
	$respon = array();
		$qry = "select *from tb_aksesoris;";
		$result = mysqli_query($link, $qry);
		
			if(mysqli_num_rows($result) > 0){
				$respon["aksesoris"] = array();
				while ($row = mysqli_fetch_array($result)){
				//$result = mysqli_fetch_array($result);
					$aksesoris = array();
					$aksesoris["ID_aksesoris"] = $row["ID"];
					$aksesoris["nama_aksesoris"] = $row["nama_aksesoris"];
					array_push($respon["aksesoris"], $aksesoris);
				}
				$respon["sukses"] = 1;
				$respon["pesan"] = "Aksesoris";
				echo json_encode($respon);
			}else {
				$respon["sukses"] = 0;
				$respon["pesan"] = "Tidak ada Kendaraan1";
				echo json_encode ($respon);
			}
?>
