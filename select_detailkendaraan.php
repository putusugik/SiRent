<?php
	require "koneksi.php";
	$respon = array();
	if(isset ($_GET["id"])){
		$id = $_GET['id'];
		$qry = "SELECT tb_dttoko_kendaraan.`ID_toko`, tb_toko.`id_pemilik`, tb_dttoko_kendaraan.`ID_kendaraan`, tb_kendaraan.`nama_kendaraan`, tb_kendaraan.`no_plat`, tb_kendaraan.`no_mesin`,
				tb_merk.`merk_kendaraan`, tb_tipekendaraan.`tipe_kendaraan`, tb_dttoko_kendaraan.`harga_sewa`, tb_dttoko_kendaraan.`jumlah`, tb_dttoko_kendaraan.`deskripsi`,
				tb_dttoko_kendaraan.`gambar`
				FROM tb_dttoko_kendaraan
				INNER JOIN tb_toko ON tb_dttoko_kendaraan.`ID_toko`=tb_toko.`ID`
				INNER JOIN tb_kendaraan ON tb_dttoko_kendaraan.`ID_kendaraan` = tb_kendaraan.`ID`
				INNER JOIN tb_merk ON tb_kendaraan.`ID_merk` = tb_merk.`ID`
				INNER JOIN tb_tipekendaraan ON tb_kendaraan.`ID_tipe` = tb_tipekendaraan.`ID`
				WHERE id_pemilik = $id ;";
		$result = mysqli_query($link, $qry);
		
			if(mysqli_num_rows($result) > 0){
				$respon["kendaraan"] = array();
				while ($row = mysqli_fetch_array($result)){
				//$result = mysqli_fetch_array($result);
					$kendaraan = array();
					$kendaraan["ID_toko"] = $row["ID_toko"];
					$kendaraan["id_pemilik"] = $row["id_pemilik"];
					$kendaraan["id_kendaraan"] = $row["ID_kendaraan"];
					$kendaraan["nama_kendaraan"] = $row["nama_kendaraan"];
					$kendaraan["no_plat"] = $row["no_plat"];
					$kendaraan["no_mesin"] = $row["no_mesin"];
					$kendaraan["merk_kendaraan"] = $row["merk_kendaraan"];
					$kendaraan["tipe_kendaraan"] = $row["tipe_kendaraan"];
					$kendaraan["harga_sewa"] = $row["harga_sewa"];
					$kendaraan["jumlah"] = $row["jumlah"];
					$kendaraan["deskripsi"] = $row["deskripsi"];
					array_push($respon["kendaraan"], $kendaraan);
				}
				$respon["sukses"] = 1;
				$respon["pesan"] = "Kendaraan";
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
