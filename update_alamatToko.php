<?php
require "koneksi.php";
$respon=array();
$ID = $_POST["id_pemilik"];
$lat = $_POST["lat"];
$lng = $_POST["lng"];
$qry1 = "UPDATE tb_toko SET lat='$lat', lng='$lng' WHERE id_pemilik = $ID;";
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
