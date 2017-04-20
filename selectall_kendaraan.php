<?php
	require "koneksi.php";
	$respon = array();
	//if(isset ($_GET["id"])){
		//$id = $_GET['id'];
		$qry = "SELECT tb_toko.`ID`, tb_toko.`id_pemilik`,tb_toko.`nama_toko`, tb_profiluser.`nama_belakang`, tb_kendaraan.`nama_kendaraan`,
				tb_dttoko_kendaraan.`harga_sewa`, tb_dttoko_kendaraan.`jumlah`, tb_merk.`merk_kendaraan`, tb_tipekendaraan.`tipe_kendaraan`
				FROM
				db_sirent.`tb_toko`

				INNER JOIN tb_profiluser ON tb_toko.`id_pemilik`=tb_profiluser.`ID_user`
				INNER JOIN tb_dttoko_kendaraan ON tb_toko.`ID`=tb_dttoko_kendaraan.`ID_toko`
				INNER JOIN tb_kendaraan ON tb_dttoko_kendaraan.`ID_kendaraan`=tb_kendaraan.`ID`
				INNER JOIN tb_merk ON tb_kendaraan.`kode_merkkendaraan` = tb_merk.`kode_kendaraan`
				INNER JOIN tb_tipekendaraan ON tb_kendaraan.`kode_tipekendaraan` = tb_tipekendaraan.`kode_tipekendaraan`;";
		$result = mysqli_query($link, $qry);
			if(mysqli_num_rows($result) > 0){
				$respon["kendaraan"] = array();
				while($row = mysqli_fetch_array($result)){
					$kendaraan = array();
					$kendaraan["id"] = $row["ID"];
					$kendaraan["id_pemilik"] = $row["id_pemilik"];
					$kendaraan["nama_toko"] = $row["nama_toko"];
					$kendaraan["nama_belakang"] = $row["nama_belakang"];
					$kendaraan["nama_kendaraan"] = $row["nama_kendaraan"];
					$kendaraan["harga_sewa"] = $row["harga_sewa"];
					$kendaraan["jumlah"] = $row["jumlah"];
					$kendaraan["merk_kendaraan"] = $row["merk_kendaraan"];
					$kendaraan["tipe_kendaraan"] = $row["tipe_kendaraan"];
					array_push($respon["kendaraan"], $kendaraan);
				}
				$respon["sukses"] = 1;
				$respon["pesan"] = "Kendaraan";
				echo json_encode($respon);
			} else {
				$respon["sukses"] = 0;
				$respon["pesan"] = "Tidak ada Kendaraan";
				echo json_encode ($respon);
			}
	/*}else {
				$respon["sukses"] = 0;
				$respon["pesan"] = "Required field's is missing";
				echo json_encode ($respon);
			}*/
?>