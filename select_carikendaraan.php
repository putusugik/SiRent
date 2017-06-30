<?php
error_reporting(E_ALL ^ (E_NOTICE | E_WARNING));
	require "koneksi.php";
	$respon = array();
	$nama = $_GET['nama'];
	$jenis = $_GET['jenis'];
	$order = $_GET['order'];
	$searchnama = " ";
	$searchjnis = " ";
	$searchordr = " ";
	//if(isset ($_GET["id"])){
		//$id = $_GET['id'];
	if(isset($_GET['nama']))
	{
		$searchnama .= "&& tb_kendaraan.`nama_kendaraan` LIKE '%$nama%'";
	}
	if(isset($_GET['jenis'])){
		$searchjnis .= "&& tb_tipekendaraan.`ID`='$jenis' ";
	}
	if(isset($_GET['order'])){
		if($_GET['order'] == 0){
			$searchordr .=  "ORDER BY tb_dttoko_kendaraan.harga_sewa ASC ";
		} else {
			$searchordr .=  "ORDER BY tb_dttoko_kendaraan.harga_sewa DESC ";
		}
		
	}
		$qry = "SELECT tb_dttoko_kendaraan.`ID_kendaraan`, tb_kendaraan.`nama_kendaraan`, tb_kendaraan.`no_plat`, tb_tipekendaraan.`tipe_kendaraan`, tb_merk.`merk_kendaraan`,
				tb_toko.`nama_toko`, tb_dttoko_kendaraan.`harga_sewa`, tb_dttoko_kendaraan.`jumlah`, tb_dttoko_kendaraan.`deskripsi`, tb_dttoko_kendaraan.`gambar`
				FROM `tb_kendaraan`
				INNER JOIN tb_dttoko_kendaraan ON tb_kendaraan.`ID`=tb_dttoko_kendaraan.`ID_kendaraan`
				INNER JOIN tb_toko ON tb_dttoko_kendaraan.`ID_toko`=tb_toko.`ID`
				INNER JOIN tb_merk ON tb_kendaraan.`ID_merk`=tb_merk.`ID`
				INNER JOIN tb_tipekendaraan ON tb_kendaraan.`ID_tipe`=tb_tipekendaraan.`ID`
				WHERE tb_dttoko_kendaraan.st_sewa=0";
				$qry .= $searchnama;
				$qry .= $searchjnis;
				$qry .= $searchordr;
		$result = mysqli_query($link, $qry);
			if(mysqli_num_rows($result) > 0){
				$respon["kendaraan"] = array();
				while($row = mysqli_fetch_array($result)){
					$kendaraan = array();
					$kendaraan["ID_kendaraan"] = $row["ID_kendaraan"];
					$kendaraan["nama_kendaraan"] = $row["nama_kendaraan"];
					$kendaraan["no_plat"] = $row["no_plat"];
					$kendaraan["tipe_kendaraan"] = $row["tipe_kendaraan"];
					$kendaraan["merk_kendaraan"] = $row["merk_kendaraan"];
					$kendaraan["nama_toko"] = $row["nama_toko"];
					$kendaraan["harga_sewa"] = $row["harga_sewa"];
					$kendaraan["jumlah"] = $row["jumlah"];
					$kendaraan["deskripsi"] = $row["deskripsi"];
					$kendaraan["gambar"] = $row["gambar"];
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