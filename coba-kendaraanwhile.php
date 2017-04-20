<?php
require "koneksi.php";
	$respon = array();
	$tmp = array();
	if(isset ($_GET["id"])){
		$id = $_GET['id'];
		$qry = "SELECT tb_toko.`ID`, tb_toko.`id_pemilik`,tb_toko.`nama_toko`, tb_profiluser.`nama_belakang`, tb_kendaraan.`nama_kendaraan`,
				tb_dttoko_kendaraan.`harga_sewa`, tb_dttoko_kendaraan.`jumlah`, tb_merk.`merk_kendaraan`, tb_tipekendaraan.`tipe_kendaraan`
				FROM
				`tb_toko`

				INNER JOIN tb_profiluser ON tb_toko.`id_pemilik`=tb_profiluser.`ID_user`
				INNER JOIN tb_dttoko_kendaraan ON tb_toko.`ID`=tb_dttoko_kendaraan.`ID_toko`
				INNER JOIN tb_kendaraan ON tb_dttoko_kendaraan.`ID_kendaraan`=tb_kendaraan.`ID`
				INNER JOIN tb_merk ON tb_kendaraan.`kode_merkkendaraan` = tb_merk.`kode_kendaraan`
				INNER JOIN tb_tipekendaraan ON tb_kendaraan.`kode_tipekendaraan` = tb_tipekendaraan.`kode_tipekendaraan`
				WHERE id_pemilik = $id ;";
		$result = mysqli_query($link, $qry) or die (mysqli_error($link));
		while($row = mysqli_fetch_assoc($result)){
			//$tmp = $row;
			array_push($tmp, $row);
			
		}
	}else {
				$respon["sukses"] = 0;
				$respon["pesan"] = "Required field's is missing";
				echo json_encode ($respon);
			}
echo json_encode($tmp);
?>