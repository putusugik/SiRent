<?php
	require "koneksi.php";
	$respon = array();
	$filepath = "fotouser/";
	if(isset ($_GET["id"])){
		$id = $_GET['id'];
		$qry = "SELECT *from tb_profiluser
				WHERE ID_user LIKE $id ;";
		$result = mysqli_query($link, $qry);
		if(!empty($result)){
			if(mysqli_num_rows($result) > 0){
				$result = mysqli_fetch_array($result);
				$profil = array();
				$profil["ID"] = $result["ID"];
				$profil["nama_depan"] = $result["nama_depan"];
				$profil["nama_belakang"] = $result["nama_belakang"];
				$profil["tempat_lahir"] = $result["tempat_lahir"];
				$profil["tgl_lahir"] = $result["tgl_lahir"];
				$profil["no_hp"] = $result["no_hp"];
				$profil["alamat"] = $result["alamat"];
				$profil["email"] = $result["email"];
				$profil["foto_user"] = $filepath.$result["foto_user"];
				$profil["id"] = $result["ID_user"];
				
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
				$respon["pesan"] = "Tidak ada profil";
				echo json_encode ($respon);
			}
	} else {
				$respon["sukses"] = 0;
				$respon["pesan"] = "Required field's is missing";
				echo json_encode ($respon);
			}
?>
