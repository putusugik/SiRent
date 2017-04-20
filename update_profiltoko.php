<?php
require "koneksi.php";
$respon=array();
$ID = $_POST["id_pemilik"];
$nama_toko = $_POST["nama_toko"];
$alamat = $_POST["alamat"];
$no_telp = $_POST["no_telp"];
$email = $_POST["email"];
$jam_buka = $_POST["jam_buka"];
$jam_tutup = $_POST["jam_tutup"];
$lat = $_POST["lat"];
$lng = $_POST["lng"];
$qry1 = "UPDATE tb_toko SET nama_toko='$nama_toko', alamat='$alamat', no_telp='$no_telp', email='$email', jam_buka='$jam_buka', jam_tutup='$jam_tutup' WHERE id_pemilik = $ID;";
$res1 = mysqli_query($link, $qry1);
  if($res1){
      $respon["sukses"]=1;
      $respon["pesan"] = "Sukses";
      echo json_encode($respon);
  }//echo json_encode($user);
  else {
  $respon["sukses"]=0;
  $respon["pesan"] = mysqli_error($link);
  echo json_encode($respon);
}
$link->close();

?>
