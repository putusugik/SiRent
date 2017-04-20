<?php
	require "koneksi.php";
	$respon = array();
	$user_name = $_POST["user_name"];
	$user_pass = $_POST["user_pass"];
	$statususer = $_POST["status"];
	$mysql_qry = "SELECT * FROM tb_user where nama_user='$user_name' AND user_pass ='$user_pass';";
	$result = mysqli_query($link ,$mysql_qry);
	if (!empty($result)){
		if(mysqli_num_rows($result) > 0) {
			$result = mysqli_fetch_array($result);
			$user = array();
			$user["ID"]=$result["ID"];
			$user["user_name"]=$result["nama_user"];
			$user["user_pass"]=$result["user_pass"];
			$user["status"]=$result["status"];
			$user["sukses"]=1;
			$respon["sukses"]=1;
			$respon["user"] = array();
			//array_push($respon["user"], $user);
			echo json_encode($user);
		}
			else {
				$respon["sukses"]=0;
				$respon["pesan"] = "Invalid User";
				echo json_encode($respon);
			}
	}
	
?>