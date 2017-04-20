<?php
	$respon=array();
		
	$nama_kendaraan = $_POST['nama_kendaraan'];
    $no_mesin = $_POST['no_mesin'];
    $no_plat = $_POST['no_plat'];
	$ID_merk = $_POST['ID_merk'];
	$ID_tipe = $_POST['ID_tipe'];
	$ID_toko = $_POST['ID_toko'];
	$harga_sewa = $_POST['harga_sewa'];
	$jml = $_POST['jumlah'];
	$desk = $_POST['deskripsi'];
	$ID_kendaraan = $_POST['ID_kendaraan'];
	
	require "koneksi.php";
		
		$qry1 = "UPDATE tb_dttoko_kendaraan SET harga_sewa='$harga_sewa', jumlah='$jml', deskripsi='$desk' WHERE ID_kendaraan='$ID_kendaraan';
				 UPDATE tb_kendaraan SET nama_kendaraan='$nama_kendaraan', no_mesin='$no_mesin', no_plat='$no_plat', ID_merk='$ID_merk', ID_tipe='$ID_tipe' WHERE ID='$ID_kendaraan';";
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



?>
