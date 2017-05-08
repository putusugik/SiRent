<?php
	$respon=array();
	require "koneksi.php";
		
		$qry1 = "UPDATE tb_dttoko_kendaraan 
				INNER JOIN tb_dtrental ON tb_dtrental.`ID_kendaraan` = tb_dttoko_kendaraan.`ID_kendaraan`
				INNER JOIN tb_rental ON tb_rental.`ID` = tb_dtrental.`ID_rental`
				SET tb_dttoko_kendaraan.`st_sewa` = 3 WHERE tb_rental.`tgl_kembali`<NOW(); ";
		$res1 = mysqli_query($link, $qry1);
		if($res1){
			$qry2 = "UPDATE tb_rental SET tb_rental.`status` = 4 WHERE tb_rental.`tgl_kembali`<NOW(); ";
			$res2 = mysqli_query($link, $qry2);
			if($res2){
				
				$respon["sukses"]=1; 
				$respon["pesan"] = "Sukses";
				echo json_encode($respon);	
			}
		}
		else {
		  $respon["sukses"]=0;
		  $respon["pesan"] = mysqli_error($link);
		  echo json_encode($respon);	
		}



?>
