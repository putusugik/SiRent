<?php
	$respon=array();
		
	$nama_kendaraan = $_POST['nama_kendaraan'];
    $no_mesin = $_POST['no_mesin'];
    $no_plat = $_POST['no_plat'];
	$ID_toko = $_POST['ID_toko'];
	$harga_sewa = $_POST['harga_sewa'];
	$jml = $_POST['jumlah'];
	$desk = $_POST['deskripsi'];
	$ID_kendaraan = $_POST['ID_kendaraan'];
	
	$idsurf = $_POST['idsurf'];
	$idhanak = $_POST['idhanak'];
	$idhstandar = $_POST['idhstandar'];
	$idhfull = $_POST['idhfull'];
	$idjegois = $_POST['idjegois'];
	$idjstandar = $_POST['idjstandar'];
	$idgps = $_POST['idgps'];
	$idbhelm = $_POST['idbhelm'];
	$idphone = $_POST['idphone'];
	
	$hsurf = $_POST['hsurf'];
	$hhanak = $_POST['hhanak'];
	$hhstandar = $_POST['hhstandar'];
	$hhfull = $_POST['hhfull'];
	$hjegois = $_POST['hjegois'];
	$hjstandar = $_POST['hjstandar'];
	$hgps = $_POST['hgps'];
	$hbhelm = $_POST['hbhelm'];
	$hphone = $_POST['hphone'];
	
	require "koneksi.php";
		
		$qry1 = "UPDATE tb_dttoko_kendaraan SET harga_sewa='$harga_sewa', jumlah='$jml', deskripsi='$desk' WHERE ID_kendaraan='$ID_kendaraan';";
		$res1 = mysqli_query($link, $qry1);
		if($res1){
			$qry2 = "UPDATE tb_kendaraan SET nama_kendaraan='$nama_kendaraan', no_mesin='$no_mesin', no_plat='$no_plat' WHERE ID='$ID_kendaraan';";
			$res2 = mysqli_query($link, $qry2);
			if($res2){
				
				if(!empty($idsurf)){
					$qry3 = "UPDATE tb_dtAksKendaraan SET harga='$hsurf' WHERE ID_aksesoris='$idsurf' AND ID_kendaraan='$ID_kendaraan'";
					$res3 = mysqli_query($link, $qry3);
				}
				if(!empty($idhanak)){
					$qry3 = "UPDATE tb_dtAksKendaraan SET harga='$hhanak' WHERE ID_aksesoris='$idhanak' AND ID_kendaraan='$ID_kendaraan'";
					$res3 = mysqli_query($link, $qry3);
				}
				if(!empty($idhfull)){
					$qry3 = "UPDATE tb_dtAksKendaraan SET harga='$hhfull' WHERE ID_aksesoris='$idhfull' AND ID_kendaraan='$ID_kendaraan'";
					$res3 = mysqli_query($link, $qry3);
				}
				if(!empty($idhstandar)){
					$qry3 = "UPDATE tb_dtAksKendaraan SET harga='$hhstandar' WHERE ID_aksesoris='$idhstandar' AND ID_kendaraan='$ID_kendaraan'";
					$res3 = mysqli_query($link, $qry3);
				}
				if(!empty($idjstandar)){
					$qry3 = "UPDATE tb_dtAksKendaraan SET harga='$hjstandar' WHERE ID_aksesoris='$idjstandar' AND ID_kendaraan='$ID_kendaraan'";
					$res3 = mysqli_query($link, $qry3);
				}
				if(!empty($idjegois)){
					$qry3 = "UPDATE tb_dtAksKendaraan SET harga='$hjegois' WHERE ID_aksesoris='$idjegois' AND ID_kendaraan='$ID_kendaraan'";
					$res3 = mysqli_query($link, $qry3);
				}
				if(!empty($idgps)){
					$qry3 = "UPDATE tb_dtAksKendaraan SET harga='$hgps' WHERE ID_aksesoris='$idgps' AND ID_kendaraan='$ID_kendaraan'";
					$res3 = mysqli_query($link, $qry3);
				}
				if(!empty($idbhelm)){
					$qry3 = "UPDATE tb_dtAksKendaraan SET harga='$hbhelm' WHERE ID_aksesoris='$idbhelm' AND ID_kendaraan='$ID_kendaraan'";
					$res3 = mysqli_query($link, $qry3);
				}
				if(!empty($idphone)){
					$qry3 = "UPDATE tb_dtAksKendaraan SET harga='$hphone' WHERE ID_aksesoris='$idphone' AND ID_kendaraan='$ID_kendaraan'";
					$res3 = mysqli_query($link, $qry3);
				}
				
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
