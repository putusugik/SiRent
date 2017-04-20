<?php
   	require_once  'db_connect.php';
	$db = new DB_CONNECT();

    $base=$_REQUEST['image'];
    //$pid = $_REQUEST['pid'];
    $filename = $_REQUEST['filename'];
    $id_pura_kawitan = $_REQUEST['id_pura_kawitan'];
    $binary=base64_decode($base);
    header('Content-Type: bitmap; charset=utf-8');

try {
    if(file_exists("../gambar/".$id_pura_kawitan))
    {
        //echo "ada";
        //exit();
    }
    else
    {
        //echo "tidak ada";
        mkdir('../gambar/'.$id_pura_kawitan);
        //exit();
    }
	$file = fopen("../gambar/".$id_pura_kawitan."/".$filename, 'wb');
    fwrite($file, $binary);
    //mysql_query("UPDATE tb_pura_kawitan set gambar = '$filename' where
        //id_pura_kawitan = '$id_pura_kawitan'");
    fclose($file);
    echo 'Image upload complete, Please check your php file directory';
    

} catch (Exception $e) {
    echo 'Caught exception: ',  $e->getMessage(), "\n";
}

 

?>
    
    