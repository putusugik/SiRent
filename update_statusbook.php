<?php
	require "koneksi.php";
	$respon = array();
	if(isset($_POST['confirm'] && !empty ($_POST['confirm'])){
		$confirm = $_POST["confirm"];
		$ID = $_POST["Iid"];
		$mysql_qry = "UPDATE tb_rental SET `status` = 2 where ID = $ID";
		$result = mysqli_query($link ,$mysql_qry);
		if($result){
			$respon["sukses"]=1;
			$respon["pesan"] = "Sukses";
			echo json_encode($respon);
		}else {
			$respon["sukses"]=0;
			$respon["pesan"] = mysqli_error($link);
			echo json_encode($respon);
		}
	}else{
		$ID = $_POST["id"];
		$mysql_qry = "UPDATE tb_rental SET `status` = 3 where ID = $ID";
		$result = mysqli_query($link ,$mysql_qry);
		if($result){
			$respon["sukses"]=1;
			$respon["pesan"] = "Sukses";
			echo json_encode($respon);
		}else {
			$respon["sukses"]=0;
			$respon["pesan"] = mysqli_error($link);
			echo json_encode($respon);
		}
	}
?>