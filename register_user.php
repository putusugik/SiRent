<?php
	require "koneksi.php";
	$respon=array();
	$user_name = $_POST["user_name"];
	$user_pass = $_POST["user_pass"];
	$nama_depan = $_POST["nama_depan"];
	$nama_belakang = $_POST["nama_belakang"];
	$email = $_POST["email"];
	$qry1 = "insert into tb_user (nama_user, user_pass, user_registered) values ('$user_name', '$user_pass', now());";
	$res1 = mysqli_query($link, $qry1);
	$qrytmp = "CALL random_code_verify();";
	$restm mysqli_query($link, $qrytmp);
	if($res1){
		$lastid = mysqli_insert_id($link);
		$qry2 = "insert into tb_profiluser (nama_depan, nama_belakang, email, ID_user) values ('$nama_depan', '$nama_belakang', '$email', '$lastid');";
		$res2 = mysqli_query($link, $qry2);
			if($res2) {
				$respon["sukses"]=1;
				$respon["pesan"] = "Sukses";
				smptmailer($email);
				echo json_encode($respon);
			}//echo json_encode($user);
	} else {
		$respon["sukses"]=0;
		$respon["pesan"] = "Invalid";
		echo json_encode($respon);
	}
	$link->close();
	
	function smtpmailer($to, $body) { 
		global $error;
		require("class.phpmailer.php");
		$mail = new PHPMailer();  // create a new object
		$mail->IsSMTP(); // enable SMTP
		$mail->SMTPDebug = 0;  // debugging: 1 = errors and messages, 2 = messages only
		$mail->SMTPAuth = true;  // authentication enabled
		$mail->SMTPSecure = 'ssl'; // secure transfer enabled REQUIRED for GMail
		$mail->Host = 'smtp.gmail.com';
		$mail->Port = 465; 
		$mail->Username = 'e.ticketing.kereta.api@gmail.com';  
		$mail->Password = 'ogerganteng';           
		$mail->SetFrom('e.ticketing.kereta.api@gmail.com', 'Sistem Informasi Kereta Api');
		$mail->Subject = 'Validasi Akun';
		$mail->Body =  $body;
		$mail->IsHTML(true);
		$mail->AddAddress($to);
		if(!$mail->Send()) {
			$error = 'Mail error: '.$mail->ErrorInfo; 
			return false;
		} else {
			$error = 'Message sent!';
			return true;
		}
	}
?>
