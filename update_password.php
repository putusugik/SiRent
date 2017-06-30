<?php
	$respon=array();
	$ID_user = $_POST['ID_user'];
    $pass_lama = $_POST['pass_lama'];
    $pass_baru = $_POST['pass_baru'];
		
	require "koneksi.php";
		
		$qry1 = "SELECT user_pass FROM tb_user WHERE user_pass = '$pass_lama';";
		$res1 = mysqli_query($link, $qry1) or die (mysqli_query($link));
		$temp = mysqli_num_rows($res1);
		
		if($temp==1){
			$qry2 = "UPDATE tb_user SET user_pass='$pass_baru' WHERE ID = '$ID_user';";
			$res2 = mysqli_query($link, $qry2);
			if($res2){
				$respon["sukses"]=1;
				$respon["pesan"] = "Sukses";
				echo json_encode($respon);	
			}
		} else {
		  $respon["sukses"]=0;
		  $respon["pesan"] = "Password tidak cocok";
		  echo json_encode($respon);	
		}



?>
