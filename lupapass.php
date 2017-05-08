<?php
	require "koneksi.php";
	$respon=array();
	$email = $_POST["email"];
	$qry1 = "x";
	$res1 = mysqli_query($link, $qry1);
	
	if($res1){
		$lastid = mysqli_insert_id($link);
		echo $lastid;
		$qry12 = "UPDATE tb_user SET ver_code=concat(
              substring('ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789', rand(@seed:=round(rand('$lastid')*4294967296))*36+1, 1),
              substring('ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789', rand(@seed:=round(rand(@seed)*4294967296))*36+1, 1),
              substring('ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789', rand(@seed:=round(rand(@seed)*4294967296))*36+1, 1),
              substring('ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789', rand(@seed:=round(rand(@seed)*4294967296))*36+1, 1),
              substring('ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789', rand(@seed:=round(rand(@seed)*4294967296))*36+1, 1),
              substring('ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789', rand(@seed:=round(rand(@seed)*4294967296))*36+1, 1),
              substring('ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789', rand(@seed:=round(rand(@seed)*4294967296))*36+1, 1),
              substring('ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789', rand(@seed)*36+1, 1)
             )
			WHERE ID='$lastid';";
		$res3 = mysqli_query ($link, $qry12);
		$qry2 = "insert into tb_profiluser (nama_depan, nama_belakang, email, ID_user) values ('$nama_depan', '$nama_belakang', '$email', '$lastid');";
		$res2 = mysqli_query($link, $qry2);
			if($res2) {
				$respon["sukses"]=1;
				$respon["pesan"] = "Sukses";
				sendCode ($email, $link);
				echo json_encode($respon);
			}//echo json_encode($user);
	} else {
		$respon["sukses"]=0;
		$respon["pesan"] = "Invalid";
		echo json_encode($respon);
	}
	//$link->close();
	
	
	function sendCode ($mail, $link){
		$res3 = mysqli_query($link, "SELECT tb_user.`ID`, tb_user.`ver_code`, tb_profiluser.`email` 
							FROM tb_user 
							INNER JOIN tb_profiluser ON tb_user.`ID`=tb_profiluser.`ID_user`
							WHERE email='$mail';");
		$row2=mysqli_fetch_array($res3);
		$id_login=$row2['ID'];
		$validation_code=$row2['ver_code'];
		$body="Silakan klik link dibawah untuk melakukan validasi akun. 
		<br> <a href= 'http://sirent.esy.es/verification.php?id=$id_login&code=$validation_code'> Klik </a>";
		smtpmailer($mail, $body);
	}
	
	function smtpmailer($to, $body) { 
		global $error;
		require("class.phpmailer.php");
		$mail = new PHPMailer();  // create a new object
		$mail->IsSMTP(); // enable SMTP
		$mail->SMTPDebug = 1;  // debugging: 1 = errors and messages, 2 = messages only
		$mail->SMTPAuth = true;  // authentication enabled
		$mail->SMTPSecure = 'tls'; // secure transfer enabled REQUIRED for GMail
		$mail->Host = 'smtp.gmail.com';
		$mail->Port = 587; 
		$mail->Priority = 1;
		$mail->Username = 'ta.sirent@gmail.com';  
		$mail->Password = 'sugiksirent';           
		$mail->SetFrom('ta.sirent@gmail.com', 'Sistem Informasi Rental Kendaraan');
		$mail->Subject = 'Validasi Akun';
		$mail->Body =  $body;
		$mail->IsHTML(true);
		$mail->AddAddress($to);
		if(!$mail->Send()) {
			$error = 'Mail error: '.$mail->ErrorInfo; 
			echo $error;
			return false;
		} else {
			$error = 'Message sent!';
			return true;
		}
	}
?>
