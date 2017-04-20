<?php
   	require_once  'koneksi.php';

	$curdir = __FILE__;
	
    $base=$_REQUEST['image'];
    //$pid = $_REQUEST['pid'];
    $filename = $_REQUEST['filename'];
    $ID_kend = $_REQUEST['id'];
    $binary=base64_decode($base);
    header('Content-Type: bitmap; charset=utf-8');

try {
	$file = fopen("/home/u903714600/public_html/fotokendaraan/".$filename, 'wb');
    fwrite($file, $binary);
    $res = mysqli_query($link, "UPDATE tb_dttoko_kendaraan set gambar = '$filename' where
        ID_kendaraan = '$ID_kend'");
    fclose($file);
    echo 'Image upload complete, Please check your php file directory';
    

} catch (Exception $e) {
    echo 'Caught exception: ',  $e->getMessage(), "\n";
}

 

?>
    