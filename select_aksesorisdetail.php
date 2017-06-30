<?php
	require "koneksi.php";
	$respon = array();
	if(isset ($_GET["id"])){
		$id = $_GET['id'];
		$idRent = $_GET['idRent'];
		$qry = "SELECT tb_dtrental_aksesoris.`ID_dtrental` , tb_dtrental_aksesoris.`ID_aksesoris` , tb_aksesoris.`nama_aksesoris` , tb_kendaraan.`nama_kendaraan` 
				FROM tb_dtrental_aksesoris
				INNER JOIN tb_dtrental ON tb_dtrental_aksesoris.`ID_dtrental` = tb_dtrental.`ID` 
				INNER JOIN tb_aksesoris ON tb_dtrental_aksesoris.`ID_aksesoris` = tb_aksesoris.`ID` 
				INNER JOIN tb_kendaraan ON tb_dtrental.`ID_kendaraan` = tb_kendaraan.`ID` 
				WHERE tb_dtrental.`ID_kendaraan` =$id";
		$result = mysqli_query($link, $qry);
		
			if(mysqli_num_rows($result) > 0){
				$respon["aksesoris"] = array();
				while ($row = mysqli_fetch_array($result)){
				//$result = mysqli_fetch_array($result);
					$booking = array();
					$booking["ID_dtrental"] = $row["ID"];
					$booking["ID_kendaraan"] = $row["ID_kendaraan"];
					$booking["nama_aksesoris"] = $row["nama_aksesoris"];
					$booking["nama_kendaraan"] = $row["nama_kendaraan"];
					array_push($respon["aksesoris"], $booking);
				}
				$respon["sukses"] = 1;
				$respon["pesan"] = "Aksesoris";
				echo json_encode($respon);
			} else {
				$respon["sukses"] = 0;
				$respon["pesan"] = "Tidak ada Kendaraan1";
				echo json_encode ($respon);
			}
		
	} else {
				$respon["sukses"] = 0;
				$respon["pesan"] = "Required field's is missing";
				echo json_encode ($respon);
			}
?>
