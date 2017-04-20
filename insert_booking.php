<?php
	require "koneksi.php";
	$respon=array();
	$ID_toko = $_POST["ID_toko"];
	$ID_user = $_POST["ID_user"];
	$tgl_sewa = $_POST["tgl_sewa"];
	$tgl_kembali = $_POST["tgl_kembali"];
	$ID_kend = $_POST["ID_kendaraan"];
	
	$qry1 = "INSERT INTO tb_rental (ID_toko, ID_user, tgl_sewa, tgl_kembali, `status`) VALUES ('$ID_toko', '$ID_user', '$tgl_sewa','$tgl_kembali', '1');";
	$res1 = mysqli_query($link, $qry1);
	if($res1){
		$lid = mysqli_insert_id($link);
		$qry2 = "INSERT INTO tb_dtrental (ID_rental, ID_kendaraan, `status`) VALUES ('#lid','$ID_kend','1');";
		$res2 = mysql_query($link, $qry2);
		if (res2){
			$respon["sukses"]=1;
			$respon["pesan"] = "Sukses";
			echo json_encode($respon);
		}
		//echo json_encode($user);
	} else {
		$respon["sukses"]=0;
		$respon["pesan"] = "Invalid";
		echo json_encode($respon);
	}
	$link->close();
?>
