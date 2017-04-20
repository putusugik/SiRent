<?php
//require "koneksi.php";
echo "bangsat";
$respon=array();
if(isset($_POST['nama_depan']) && isset($_POST['nama_belakang'])
	&& isset($_POST['tempat_lahir']) && isset($_POST['tgl_lahir']) && isset($_POST['no_hp'])
	&& isset($_POST['alamat']) && isset($_POST['email']) && isset($_POST['id'])){
		
		$nama_depan = $_POST["nama_depan"];
		$nama_belakang = $_POST["nama_belakang"];
		$tempat_lahir = $_POST["tempat_lahir"];
		$tgl_lahir = $_POST["tgl_lahir"];
		$no_hp = $_POST["no_hp"];
		$alamat = $_POST["alamat"];
		$email = $_POST["email"];
		$ID_user = $_POST["id"];
		
	require "koneksi.php";
		
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
}
	else{
		$respon["sukses"]=1;
		$respon["pesan"]= "Required fields is missing";
	}


?>
