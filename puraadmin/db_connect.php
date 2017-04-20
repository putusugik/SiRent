<?php
 
/**
 * A class file to connect to database
 */
class DB_CONNECT {
 
    // constructor
    function __construct() {
        // connecting to database
        $this->connect();
    }
 
    // destructor
    function __destruct() {
        // closing db connection
        $this->close();
    }
 
    /**
     * Function to connect with database
     */
    function connect() {
        // import database connection variables
 
        // Connecting to mysql database
        $con = mysql_connect("db-hosting.unud.ac.id", "c10udayana", "resatya123") or die(mysql_error());
 
        // Selecing database
        $db = mysql_select_db("c15purakawitan") or die(mysql_error());
        // returing connection cursor
        return $con;
    }
 
    /**
     * Function to close db connection
     */
    function close() {
        // closing db connection
        mysql_close();
    }
 
}
 
?>