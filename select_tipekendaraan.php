<?php
require "koneksi.php";
$respon = array();
$qry = "SELECT ID, tipe_kendaraan from tb_tipekendaraan";
$result = mysqli_query($link, $qry);
if(mysqli_num_rows($result) > 0){
	$respon["tipe"] = array();
	while($row = mysqli_fetch_assoc($result)){
		$tipe = array();
		$tipe ["ID"] = $row["ID"];
		$tipe ["tipe_kendaraan"] = $row["tipe_kendaraan"];
		array_push($respon["tipe"], $tipe);
	//$tmp[] = $row;
	}
	$respon["sukses"] = 1;
	echo json_encode($respon);
} else {
	$respon["sukses"] = 0;
	$respon["pesan"] = "No object found";
	echo json_encode($respon);
}
//echo json_encode($tmp);

?>