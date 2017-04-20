<?php
// array for JSON response
$response = array();

// check for required fields
if (isset($_POST['nama_kendaraan']) && isset($_POST['no_mesin']) && isset($_POST['no_plat'])) {

    $nama_kendaraan = $_POST['nama_kendaraan'];
    $no_mesin = $_POST['no_mesin'];
    $no_plat = $_POST['no_plat'];

    // include db connect class
    require "koneksi.php";

    // connecting to db


    // mysql inserting a new row
    $result = mysqli_query("INSERT INTO tb_kendaraan(nama_kendaraan, no_mesin, no_plat) VALUES('$nama_kendaraan', '$no_mesin', '$no_plat')");

    // check if row inserted or not
    if ($result) {
        // successfully inserted into database
        $response["success"] = 1;
        $response["message"] = "Product successfully created.";

        // echoing JSON response
        echo json_encode($response);
    } else {
        // failed to insert row
        $response["success"] = 0;
        $response["message"] = "Oops! An error occurred.";

        // echoing JSON response
        echo json_encode($response);
    }
} else {
    // required field is missing
    $response["success"] = 0;
    $response["message"] = "Required field(s) is missing";

    // echoing JSON response
    echo json_encode($response);
}
?>
