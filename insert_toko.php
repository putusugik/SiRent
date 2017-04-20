<?php
require "koneksi.php";
$respon=array();
$ID_user = $_POST["ID_user"];
$nama_toko = $_POST["nama_toko"];
$jam_buka = $_POST["jam_buka"];
$jam_tutup = $_POST["jam_tutup"];
$no_hp = $_POST["no_hp"];
$alamat = $_POST["alamat"];
$email = $_POST["email"];
$qry1 = "INSERT INTO tb_toko (nama_toko, id_pemilik, alamat, no_telp, email, jam_buka, jam_tutup) VALUES
		('$nama_toko','$ID_user','$alamat','$no_hp','$email','$jam_buka','$jam_tutup');";
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
