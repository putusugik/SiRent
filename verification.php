<?php
require "koneksi.php";

	$id=$_GET['id'];//dari mana ada id
	$code=$_GET['code'];
	$query=mysql_query("Select code from tb_login where id_login='$id'");
	$row=mysql_fetch_array($query);
	$row_code=$row['code'];
	if ($code==$row_code) {
		$query=mysql_query("update tb_login set validasi='Sudah' where id_login='$id'") or die(mysql_error());
		header("location:index.php?validasi=true");
	} else echo 'FATAL ERROR';
?>