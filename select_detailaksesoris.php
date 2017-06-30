<?php
	require "koneksi.php";
	$respon = array();
	$IDkend = $_POST["ID_kendaraan"];
		$qry = "SELECT tb_dtAksKendaraan.`ID_kendaraan` , tb_dtAksKendaraan.`ID_aksesoris` , tb_kendaraan.`nama_kendaraan`,
				tb_aksesoris.`nama_aksesoris` , tb_dtAksKendaraan.`harga` 
				FROM tb_dtAksKendaraan
				INNER JOIN tb_kendaraan ON tb_dtAksKendaraan.`ID_kendaraan` = tb_kendaraan.`ID` 
				INNER JOIN tb_aksesoris ON tb_dtAksKendaraan.`ID_aksesoris` = tb_aksesoris.`ID` 
				WHERE tb_dtAksKendaraan.`ID_kendaraan` =`$IDkend`";
		$result = mysqli_query($link, $qry);
			if(mysqli_num_rows($result) > 0){
				$respon["aksesoris"] = array();
				while ($row = mysqli_fetch_array($result)){
				//$result = mysqli_fetch_array($result);
					$aksesoris = array();
					$aksesoris["ID_aksesoris"] = $row["ID_aksesoris"];
					$aksesoris["ID_kendaraan"] = $row["ID_kendaraan"];
					$aksesoris["nama_kendaraan"] = $row["nama_kendaraan"];
					$aksesoris["nama_aksesoris"] = $row["nama_aksesoris"];
					$aksesoris["harga"] = $row["harga"];
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
