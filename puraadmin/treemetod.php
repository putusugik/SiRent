<?php
 
/*
 * Following code will list all the products
 */
 
// array for JSON response
$response = array();
 
// include db connect class
require_once 'db_connect.php';
 
// connecting to db
$db = new DB_CONNECT();
 

if (isset($_GET["id_pura_kawitan"])) 
{
	
	$tingkat = 1;
	if (isset($_GET["tingkat"]))
	{
		if ($_GET["tingkat"] == 0)
		{
			$tingkat = 0;
		}
		else if ($_GET["tingkat"] > 0)
		{
			$tingkat = $_GET["tingkat"];
		}
		else
		{
			$tingkat = 1;
		}
	}

    $id_pura_kawitan = $_GET['id_pura_kawitan'];

    function getChilds($parentID, $level = 1)
    {
        $result = mysql_query("SELECT * FROM tb_pura_kawitan WHERE parents_id = '$parentID' AND status ='valid'") or die(mysql_error());
        $dump = array();
        while ($row1 = mysql_fetch_array($result)) {
                // temp user array
                $childproduct = array();
                $childproduct["id"] = $row1["id_pura_kawitan"];
                $childproduct["namapurakawitan"] = $row1["nama_pura_kawitan"];
                $childproduct["alamat"] = $row1["alamat"];
                $childproduct["lat"] = $row1["lat"];
                $childproduct["lng"] = $row1["lng"];
                $childproduct["parents_id"]=$row1["parents_id"];
                if(isset($childproduct["id"]))
                {
                	if ($level == 0)
	                {
	                	$childproduct["child"] = getChilds($childproduct["id"], 0);
	                }
	                else if ($level > 1)
	                {
	                	/*if ($level > 1)
				    	{
				    		$level--;
				    	}*/
	                	$childproduct["child"] = getChilds($childproduct["id"], $level - 1);
	                }	
	                else
	                {
	                	$childproduct["child"] = array();
	                }
                }
                else
                {
                	$childproduct["child"] = array();
                }
                
                
                array_push($dump, $childproduct);
        }
        return $dump;
    }

    $result = mysql_query("SELECT * FROM tb_pura_kawitan WHERE id_pura_kawitan = '$_GET[id_pura_kawitan]' AND status ='valid'") or die(mysql_error());

    if(mysql_num_rows($result) > 0 )
    {
        $response["products"] = array();
        while ($row0 = mysql_fetch_array($result)) 
        {
            // temp user array
            $product = array();
            $product["id"] = $row0["id_pura_kawitan"];
            $product["namapurakawitan"] = $row0["nama_pura_kawitan"];
            $product["alamat"] = $row0["alamat"];
            $product["lat"] = $row0["lat"];
            $product["lng"] = $row0["lng"];
            $product["parents_id"]=$row0["parents_id"];
            $product["child"] = getChilds($row0["id_pura_kawitan"], $tingkat);

            // push single product into final response array
            array_push($response["products"], $product);
            //$len = count($response["product"][0]["child"]);
        }
        $response["success"] = 1;
        echo json_encode($response);
    }
    else 
    {
    // no products found
        $response["success"] = 0;
        $response["message"] = "No object found";
 
    // echo no users JSON
        echo json_encode($response);
    }

}
else {
    // required field is missing
    $response["success"] = 0;
    $response["message"] = "Required field(s) is missing";
 
    // echoing JSON response
    echo json_encode($response);
}
?>