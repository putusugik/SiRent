<?php
require "koneksi.php";
$respon = array();
$qry = "SELECT ID, merk_kendaraan from tb_merk";
$result = mysqli_query($link, $qry);
while($row = mysqli_fetch_assoc($result)){
	
	$tmp[] = $row;
	
}
echo json_encode($tmp);

?>