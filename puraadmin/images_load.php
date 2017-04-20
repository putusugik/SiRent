<?php
   	//require_once  'db_connect.php';
	//$db = new DB_CONNECT();
	$filepath = "http://project.it.unud.ac.id/purakawitan/gambar/";
    //$base=$_REQUEST['image'];
    //$pid = $_REQUEST['pid'];
    //$filename = $_REQUEST['filename'];
    $id_pura_kawitan = $_REQUEST['id_pura_kawitan'];
   /* $binary=base64_decode($base);
    header('Content-Type: bitmap; charset=utf-8');*/

    $dir = scandir("../gambar/".$id_pura_kawitan."/"); 
    //var_dump($dir);
	$file= array();
	for ($i = 2; $i<sizeof($dir); $i++)
		{
			array_push($file, $filepath.$id_pura_kawitan."/".$dir[$i]);
		}
		echo json_encode(array("products"=>$file));
?>
    
    