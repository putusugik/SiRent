<?php
   	require_once  'db_connect.php';
	$db = new DB_CONNECT();

    $base=$_REQUEST['image'];
    //$pid = $_REQUEST['pid'];
    $filename = $_REQUEST['filename'];
    $id_user = $_REQUEST['id_user'];
    $binary=base64_decode($base);
    header('Content-Type: bitmap; charset=utf-8');

try {
	$file = fopen("../gambar/".$filename, 'wb');
    fwrite($file, $binary);
    mysql_query("UPDATE tb_user set foto = '$filename' where
        id_user = '$id_user'");
    fclose($file);
    echo 'Image upload complete, Please check your php file directory';
    

} catch (Exception $e) {
    echo 'Caught exception: ',  $e->getMessage(), "\n";
}

 

?>
    