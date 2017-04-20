<?php

 
// include db connect class
require_once 'db_connect.php';
 
// connecting to db
$db = new DB_CONNECT();

$markers = array();
$result = mysql_query("SELECT nama_pura_kawitan,alamat,lat,lng FROM tb_pura_kawitan WHERE status='valid'");

if(mysql_num_rows($result) > 0){
	while($row = mysql_fetch_array($result)){
			$namapurakawitan = $row['nama_pura_kawitan'];
            $alamat = $row['alamat'];
            $lat = $row['lat'];
	        $lng = $row['lng'];
            $data= array("namapurakawitan"=>$namapurakawitan,"alamat"=>$alamat,"lat"=>$lat,"lng"=>$lng,"alamat"=>$alamat);
            $marker[] = $data;
	}

        $markers = array("markers"=>$marker);
        echo json_encode($markers);
}


?>
