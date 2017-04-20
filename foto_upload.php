<?php
   	require_once  'koneksi.php';

    $base=$_REQUEST['image'];
    //$pid = $_REQUEST['pid'];
    $filename = $_REQUEST['filename'];
    $ID_user = $_REQUEST['id'];
    $binary=base64_decode($base);
    header('Content-Type: bitmap; charset=utf-8');

try {
	$file = fopen("../fotouser/".$filename, 'wb');
    fwrite($file, $binary);
    mysql_query("UPDATE tb_profiluser set foto_user = '$filename' where
        ID_user = '$ID_user'");
    fclose($file);
    echo 'Image upload complete, Please check your php file directory';
    

} catch (Exception $e) {
    echo 'Caught exception: ',  $e->getMessage(), "\n";
}

 

?>
    