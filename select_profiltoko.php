<?php
	require "koneksi.php";
	$respon = array();
	$filepath = "http://sirent.esy.es/fotouser/";
	if(isset ($_GET["id"])){
		$id = $_GET['id'];
		$qry = "SELECT *FROM tb_toko WHERE id_pemilik = '$id';";
		$result = mysqli_query($link, $qry);
		if(!empty($result)){
			if(mysqli_num_rows($result) > 0){
				$result = mysqli_fetch_array($result);
				$profil = array();
				$profil["ID"] = $result["ID"];
				$profil["nama_toko"] = $result["nama_toko"];
				$profil["alamat"] = $result["alamat"];
				$profil["no_telp"] = $result["no_telp"];
				$profil["email"] = $result["email"];
				$profil["jam_buka"] = $result["jam_buka"];
				$profil["jam_tutup"] = $result["jam_tutup"];
				$profil["email"] = $result["email"];
				$profil["lat"] = $result["lat"];
				$profil["lng"] = $result["lng"];
				//$profil["foto_user"] = $filepath.$result["foto_user"];
				$profil["id"] = $result["id_pemilik"];
				
				$respon["sukses"] = 1;
				$respon["profil"] = array();
				array_push($respon["profil"], $profil);
				echo json_encode($respon);
			} else {
				$respon["sukses"] = 0;
				$respon["pesan"] = "Tidak ada profil";
				echo json_encode ($respon);
			}
		} else {
				$respon["sukses"] = 0;
				$respon["pesan"] = "Tidak ada profil (1)";
				echo json_encode ($respon);
			}
	} else {
				$respon["sukses"] = 0;
				$respon["pesan"] = "Required field's is missing";
				echo json_encode ($respon);
			}
?>
