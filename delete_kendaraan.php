<?php
require "koneksi.php";
$ID = $_POST["ID"];
$qry1 = "DELETE FROM tb_dttoko_kendaraan WHERE ID = '$ID';";
$res1 = mysqli_query($link, $qry1);
  if($res1){
    $respon["sukses"]=1;
    $respon["pesan"] = "Sukses";
    echo json_encode($respon);
  }
  else {
    $respon["sukses"]=0;
    $respon["pesan"] = mysqli_error($link);
    echo json_encode($respon);
  }
$link->close();
?>
