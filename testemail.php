<?php
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
		$mail->Username = 'ta.sirent@gmail.com';  
		$mail->Password = 'sugiksirent';           
		$mail->SetFrom('admin.sirent@gmail.com', 'Sistem Informasi Rental Kendaraan');
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