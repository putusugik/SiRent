<?php
require "koneksi.php";
$respon=array();
$ID_user = $_POST["ID_user"];
$nama_depan = $_POST["nama_depan"];
$nama_belakang = $_POST["nama_belakang"];
$tempat_lahir = $_POST["tempat_lahir"];
$tgl_lahir = $_POST["tgl_lahir"];
$no_hp = $_POST["no_hp"];
$alamat = $_POST["alamat"];
$email = $_POST["email"];
$qry1 = "UPDATE tb_profiluser SET nama_depan='$nama_depan', nama_belakang='$nama_belakang', tempat_lahir='$tempat_lahir',
tgl_lahir='$tgl_lahir', no_hp='$no_hp', alamat='$alamat', email='$email' WHERE ID_user = $ID_user;";
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
