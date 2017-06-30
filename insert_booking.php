<?php
	require "koneksi.php";
	$respon=array();
	$ID_toko = $_POST["ID_toko"];
	$ID_user = $_POST["ID_user"];
	$tgl_sewa = $_POST["tgl_sewa"];
	$tgl_kembali = $_POST["tgl_kembali"];
	$alamat = $_POST["alamat"];
	$no_hp = $_POST["no_hp"];
	$ID_kend = $_POST["ID_kendaraan"];
	
	$idsurf = $_POST['idsurf'];
	$idhanak = $_POST['idhanak'];
	$idhstandar = $_POST['idhstandar'];
	$idhfull = $_POST['idhfull'];
	$idjegois = $_POST['idjegois'];
	$idjstandar = $_POST['idjstandar'];
	$idgps = $_POST['idgps'];
	$idbhelm = $_POST['idbhelm'];
	$idphone = $_POST['idphone'];
	
	$hsurf = $_POST['hsurf'];
	$hhanak = $_POST['hhanak'];
	$hhstandar = $_POST['hhstandar'];
	$hhfull = $_POST['hhfull'];
	$hjegois = $_POST['hjegois'];
	$hjstandar = $_POST['hjstandar'];
	$hgps = $_POST['hgps'];
	$hbhelm = $_POST['hbhelm'];
	$hphone = $_POST['hphone'];
	
	$qry1 = "INSERT INTO tb_rental (ID_toko, ID_user, tgl_sewa, tgl_kembali, alamat, status, nohp) VALUES ('$ID_toko', '$ID_user', '$tgl_sewa','$tgl_kembali', '$alamat','1','$no_hp');";
	$res1 = mysqli_query($link, $qry1);
	if($res1){
		$lid = mysqli_insert_id($link);
		$qry2 = "INSERT INTO tb_dtrental (ID_rental, ID_kendaraan, `status`) VALUES ('$lid','$ID_kend','1');";
		$lid2 = mysqli_insert_id($link);
		$res2 = mysqli_query($link, $qry2);
		$qry3 = "UPDATE tb_dttoko_kendaraan SET st_sewa=1 WHERE ID_kendaraan='$ID_kend';";
		$res3 = mysqli_query($link, $qry3);
		$qry4 = "UPDATE tb_user SET sts_trx=1 WHERE ID='$ID_user';";
		$res4 = mysqli_query($link, $qry4);
		
				if(!empty($idsurf)){
					$qry3 = "INSERT INTO tb_dtrental_aksesoris (ID_dtrental, ID_aksesoris, harga) VALUES ('$lid2','$idsurf','$hsurf')";
					$res3 = mysqli_query($link, $qry3);
				}
				if(!empty($idhanak)){
					$qry3 = "INSERT INTO tb_dtrental_aksesoris (ID_dtrental, ID_aksesoris, harga) VALUES ('$lid2','$idhanak','$hhanak')";
					$res3 = mysqli_query($link, $qry3);
				}
				if(!empty($idhfull)){
					$qry3 = "INSERT INTO tb_dtrental_aksesoris (ID_dtrental, ID_aksesoris, harga) VALUES ('$lid2','$idhfull','$hhfull')";
					$res3 = mysqli_query($link, $qry3);
				}
				if(!empty($idhstandar)){
					$qry3 = "INSERT INTO tb_dtrental_aksesoris (ID_dtrental, ID_aksesoris, harga) VALUES ('$lid2','$idhstandar','$hhstandar')";
					$res3 = mysqli_query($link, $qry3);
				}
				if(!empty($idjstandar)){
					$qry3 = "INSERT INTO tb_dtrental_aksesoris (ID_dtrental, ID_aksesoris, harga) VALUES ('$lid2','$idjstandar','$hjstandar')";
					$res3 = mysqli_query($link, $qry3);
				}
				if(!empty($idjegois)){
					$qry3 = "INSERT INTO tb_dtrental_aksesoris (ID_dtrental, ID_aksesoris, harga) VALUES ('$lid2','$idjegois','$hjegois')";
					$res3 = mysqli_query($link, $qry3);
				}
				if(!empty($idgps)){
					$qry3 = "INSERT INTO tb_dtrental_aksesoris (ID_dtrental, ID_aksesoris, harga) VALUES ('$lid2','$idgps','$hgps')";
					$res3 = mysqli_query($link, $qry3);
				}
				if(!empty($idbhelm)){
					$qry3 = "INSERT INTO tb_dtrental_aksesoris (ID_dtrental, ID_aksesoris, harga) VALUES ('$lid2','$idbhelm','$hbhelm')";
					$res3 = mysqli_query($link, $qry3);
				}
				if(!empty($idphone)){
					$qry3 = "INSERT INTO tb_dtrental_aksesoris (ID_dtrental, ID_aksesoris, harga) VALUES ('$lid2','$idphone','$hphone')";
					$res3 = mysqli_query($link, $qry3);
				}
		
		if ($res2){
			$respon["sukses"]=1;
			$respon["pesan"] = "Sukses";
			echo json_encode($respon);	
		}
	} else {
		$respon["sukses"]=0;
		$respon["pesan"] = "Invalid";
		echo json_encode($respon);
	}
	
	$query = "SELECT tb_user.`ID` , tb_user.`token` , tb_toko.`ID`, tb_toko.`nama_toko` 
			FROM tb_user
			INNER JOIN tb_toko ON tb_user.`ID` = tb_toko.`id_pemilik`
			where tb_toko.`ID` = $ID_toko";
	$result = mysqli_query($link, $query);
	if(mysqli_num_rows($result)>0){
		$respon["token"] = array();
		$token = array();
		while($row = mysqli_fetch_array($result)){
			//$token["token"]=$row["token"];
			array_push($token, $row["token"]);
			array_push($respon["token"], $token);
			echo "TOKEN".$row["token"];
		}
		$data=array( "title" => "Booking Baru", 
						"ID" => "BKG",
						"text" => "Sewa mulai tanggal ".$tgl_sewa." sampai ".$tgl_kembali,);
		$respon["sukses"] = 1;
		$respon["pesan"] = "Token";
		sendMessage($data, $token);
		echo json_encode($respon);
	}else {
		$respon["sukses"] = 0;
		$respon["pesan"] = "Tidak ada aksesoris";
		echo json_encode ($respon);
	}
	
	function sendMessage($data, $target){
	//FCM api URL
		$url = 'https://fcm.googleapis.com/fcm/send';
		//api_key available in Firebase Console -> Project Settings -> CLOUD MESSAGING -> Server key
		$server_key = 'AIzaSyAQc0jw57rxGm91rvyAIoZBdPntl_s-Kwo';
					
		$fields = array();
		$fields['data'] = $data;
		if(is_array($target)){
			$fields['registration_ids'] = $target;
		}else{
			$fields['to'] = $target;
		}
		/*$fields['notification']= array("body" => "Sesuatu Bergerak",
								      "title" => "Motion Detector"
    							);*/
		//header with content_type api key
		$headers = array(
			'Content-Type:application/json',
		  'Authorization:key='.$server_key
		);
					
		$ch = curl_init();
		curl_setopt($ch, CURLOPT_URL, $url);
		curl_setopt($ch, CURLOPT_POST, true);
		curl_setopt($ch, CURLOPT_HTTPHEADER, $headers);
		curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
		curl_setopt($ch, CURLOPT_SSL_VERIFYHOST, 0);
		curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, false);
		curl_setopt($ch, CURLOPT_POSTFIELDS, json_encode($fields));
		$result = curl_exec($ch);
		if ($result === FALSE) {
			die('FCM Send Error: ' . curl_error($ch));
		}
		curl_close($ch);
		return $result;
	}
	
	
?>
