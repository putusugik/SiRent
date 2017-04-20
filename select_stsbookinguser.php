<?php
	require "koneksi.php";
	$respon = array();
	if(isset ($_GET["id"])){
		$id = $_GET['id'];
		$qry = "SELECT tb_rental.`ID`, tb_dtrental.`ID_kendaraan`, tb_kendaraan.`nama_kendaraan`, tb_kendaraan.`no_plat`, tb_rental.`tgl_sewa`, tb_rental.`tgl_kembali`,
				tb_profiluser.`nama_belakang`, tb_status_rental.`status_rental`
				FROM tb_dtrental
				INNER JOIN tb_rental ON tb_dtrental.`ID_rental`=tb_rental.`ID`
				INNER JOIN tb_status_rental ON tb_dtrental.`status`=tb_status_rental.`ID`
				INNER JOIN tb_kendaraan ON tb_dtrental.`ID_kendaraan`=tb_kendaraan.`ID`
				INNER JOIN tb_profiluser ON tb_rental.`ID_user`=tb_profiluser.`ID_user`
				WHERE tb_rental.`status`=1 && tb_rental.`ID_user`='$id';";
		$result = mysqli_query($link, $qry);
		
			if(mysqli_num_rows($result) > 0){
				$respon["booking"] = array();
				while ($row = mysqli_fetch_array($result)){
				//$result = mysqli_fetch_array($result);
					$booking = array();
					$booking["ID_rental"] = $row["ID"];
					$booking["ID_kendaraan"] = $row["ID_kendaraan"];
					$booking["nama_kendaraan"] = $row["nama_kendaraan"];
					$booking["no_plat"] = $row["no_plat"];
					$booking["tgl_sewa"] = $row["tgl_sewa"];
					$booking["tgl_kembali"] = $row["tgl_kembali"];
					$booking["nama_belakang"] = $row["nama_belakang"];
					$booking["status_rental"] = $row["status_rental"];
					array_push($respon["booking"], $booking);
				}
				$respon["sukses"] = 1;
				$respon["pesan"] = "Booking";
				echo json_encode($respon);
			} else {
				$respon["sukses"] = 0;
				$respon["pesan"] = "Tidak ada Kendaraan1";
				echo json_encode ($respon);
			}
		
	} else {
				$respon["sukses"] = 0;
				$respon["pesan"] = "Required field's is missing";
				echo json_encode ($respon);
			}
?>
