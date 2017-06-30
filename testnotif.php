<?php
require "koneksi.php";
	$respon=array();
	$ID_toko = $_POST["ID_toko"];

	
	$query = "SELECT tb_user.`ID` , tb_user.`token` , tb_toko.`ID`, tb_toko.`nama_toko` 
			FROM tb_user
			INNER JOIN tb_toko ON tb_user.`ID` = tb_toko.`id_pemilik`
			where tb_toko.`ID` = $ID_toko";
	$result = mysqli_query($link, $query);
	if(mysqli_num_rows($result)>0){
		$respon["token"] = array();
		$token = array();
		while($row = mysqli_fetch_array($result)){
			$token["token"]=$row["token"];
			array_push($token, $row["token"]);
			array_push($respon["token"], $token);
		}
		$data=array( "title" => "Booking Kendaraan", 
		"text" => "Sewa dari tanggal $tgl_sewa",);
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
		$server_key = 'AAAA064H8WI:APA91bEH_QmcBbUy7wZyd8fGGZt0KLi83tzxqSeuHzLnIuyq
						15mQZGNBD-ykzhzhBN0rZpXhG_Ii799sNRHqXAim3hX7wFp7ZI2Cl1R7uOQ
						kBhC7lVWPlwjHlxI_Ab53tG3C2gXfLpoP';
					
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