<?php
	$respon=array();
	require "koneksi.php";
		$token = array();
		$qry1 = "SELECT ID_toko, ID_user, DATE_FORMAT(tb_rental.`tgl_kembali`, '%d-%m-%Y %H:%i:%s') AS tgl_kembali 
				FROM tb_rental WHERE DATE_FORMAT( tb_rental.`tgl_kembali` ,  '%d-%m-%Y' ) 
				= DATE_FORMAT( NOW( ) + INTERVAL 1 DAY ,  '%d-%m-%Y' )";
		$res1 = mysqli_query($link, $qry1);
		while ($row=mysqli_fetch_array($res1)){
			array_push ($token, $row['token']);
			$data=array( "title" => "SIRENT", 
						"text" => "Masa waktu sewa 1 hari lagi.",);
			sendMessage($data, $row['token']);
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
