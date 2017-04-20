-- MySQL dump 10.15  Distrib 10.0.28-MariaDB, for Linux (x86_64)
--
-- Host: localhost    Database: localhost
-- ------------------------------------------------------
-- Server version	10.0.28-MariaDB

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `tb_aksesoris`
--

DROP TABLE IF EXISTS `tb_aksesoris`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_aksesoris` (
  `ID` int(10) NOT NULL AUTO_INCREMENT,
  `nama_aksesoris` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_aksesoris`
--

/*!40000 ALTER TABLE `tb_aksesoris` DISABLE KEYS */;
/*!40000 ALTER TABLE `tb_aksesoris` ENABLE KEYS */;

--
-- Table structure for table `tb_dtrental`
--

DROP TABLE IF EXISTS `tb_dtrental`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_dtrental` (
  `ID` int(10) NOT NULL AUTO_INCREMENT,
  `ID_rental` int(10) DEFAULT NULL,
  `ID_kendaraan` int(10) DEFAULT NULL,
  `status` int(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (`ID`)
) ENGINE=MyISAM AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_dtrental`
--

/*!40000 ALTER TABLE `tb_dtrental` DISABLE KEYS */;
INSERT INTO `tb_dtrental` VALUES (3,1,26,2),(2,1,3,3);
/*!40000 ALTER TABLE `tb_dtrental` ENABLE KEYS */;

--
-- Table structure for table `tb_dttoko_kendaraan`
--

DROP TABLE IF EXISTS `tb_dttoko_kendaraan`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_dttoko_kendaraan` (
  `ID` int(10) NOT NULL AUTO_INCREMENT,
  `ID_toko` int(10) DEFAULT NULL,
  `ID_kendaraan` int(10) DEFAULT NULL,
  `harga_sewa` int(10) DEFAULT NULL,
  `jumlah` int(5) DEFAULT NULL,
  `deskripsi` text,
  `gambar` text,
  PRIMARY KEY (`ID`)
) ENGINE=MyISAM AUTO_INCREMENT=10 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_dttoko_kendaraan`
--

/*!40000 ALTER TABLE `tb_dttoko_kendaraan` DISABLE KEYS */;
INSERT INTO `tb_dttoko_kendaraan` VALUES (6,1,26,40000,1,'Irit dan masih baru barangnya bos',NULL),(3,1,3,150000,1,'Kenceng Browww',NULL),(7,1,27,250000,1,'Masih mulus dan aman',NULL),(8,2,28,40000,1,'Barang gress boss',NULL),(9,1,29,35000000,1,'mantap',NULL);
/*!40000 ALTER TABLE `tb_dttoko_kendaraan` ENABLE KEYS */;

--
-- Table structure for table `tb_gambar`
--

DROP TABLE IF EXISTS `tb_gambar`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_gambar` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `gambar` blob,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_gambar`
--

/*!40000 ALTER TABLE `tb_gambar` DISABLE KEYS */;
/*!40000 ALTER TABLE `tb_gambar` ENABLE KEYS */;

--
-- Table structure for table `tb_kendaraan`
--

DROP TABLE IF EXISTS `tb_kendaraan`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_kendaraan` (
  `ID` int(10) NOT NULL AUTO_INCREMENT,
  `nama_kendaraan` varchar(40) DEFAULT NULL,
  `no_mesin` char(12) DEFAULT NULL,
  `no_plat` varchar(8) DEFAULT NULL,
  `ID_merk` int(3) DEFAULT NULL,
  `ID_tipe` int(3) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=MyISAM AUTO_INCREMENT=31 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_kendaraan`
--

/*!40000 ALTER TABLE `tb_kendaraan` DISABLE KEYS */;
INSERT INTO `tb_kendaraan` VALUES (26,'Vario 125cc','123234345','DK6281AI',1,1),(3,'Avanza','111222333444','DK890OO',3,2),(4,'APV','999000888777','DK872BS',4,2),(6,'Vario CBS','1234555412','DK6281AI',1,1),(27,'Xenia','1236789','DK309JO',3,2),(14,'Supra Cetek','11111111','DK909AI',NULL,NULL),(15,'Legenda 110cc','19028379210','DK 0909 ',1,1),(16,'Legenda 250cc','123123123','DK 909 R',5,2),(17,'Lamborgini','121212121','DK1AI',4,2),(18,'BMZ','99999999','HH88IO',4,2),(19,'Viper','111111','DK1BL',2,2),(20,'Dodge','909090','DK8RI',3,2),(21,'Huhuhuhu','1212121','RI909',5,2),(22,'Jet Car','1111','DK0IO',5,2),(23,'Nascar','889900','DK9090',5,2),(28,'Beat','666655555','DK77KK',1,1),(29,'yuuuu','15162517','DK11Ai',3,2),(30,'','','',NULL,NULL);
/*!40000 ALTER TABLE `tb_kendaraan` ENABLE KEYS */;

--
-- Table structure for table `tb_merk`
--

DROP TABLE IF EXISTS `tb_merk`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_merk` (
  `ID` int(10) NOT NULL AUTO_INCREMENT,
  `merk_kendaraan` varchar(30) DEFAULT NULL,
  `kode_kendaraan` char(3) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=MyISAM AUTO_INCREMENT=6 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_merk`
--

/*!40000 ALTER TABLE `tb_merk` DISABLE KEYS */;
INSERT INTO `tb_merk` VALUES (1,'Honda','HND'),(2,'Yamaha','YMH'),(3,'Toyota','TYT'),(4,'Suzuki','SZK'),(5,'Kawasaki','KWS');
/*!40000 ALTER TABLE `tb_merk` ENABLE KEYS */;

--
-- Table structure for table `tb_profiluser`
--

DROP TABLE IF EXISTS `tb_profiluser`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_profiluser` (
  `ID` int(10) NOT NULL AUTO_INCREMENT,
  `nama_depan` varchar(20) DEFAULT NULL,
  `nama_belakang` varchar(20) DEFAULT NULL,
  `tempat_lahir` varchar(20) DEFAULT NULL,
  `tgl_lahir` date DEFAULT NULL,
  `no_hp` char(13) DEFAULT NULL,
  `alamat` text,
  `email` varchar(40) DEFAULT NULL,
  `foto_user` text NOT NULL,
  `ID_user` int(10) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `ID_user` (`ID_user`)
) ENGINE=MyISAM AUTO_INCREMENT=9 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_profiluser`
--

/*!40000 ALTER TABLE `tb_profiluser` DISABLE KEYS */;
INSERT INTO `tb_profiluser` VALUES (1,'Pt','Sugik','Denpasar','1995-05-25','081239943026','Jl. Tunjung Sari','gikikikikik@yahoo.com','1.jpg',1),(2,'Putu','Maong','Denpasar','1995-05-05','085434645','Jl. Tunjung Sariiii','putu@maongs.com','Screenshot_2017-01-26-22-37-34.jpg',2),(3,'Ketut','Joni',NULL,NULL,NULL,NULL,'joniketut@gmail.com','',10),(5,'Ketut','Garing',NULL,NULL,NULL,NULL,'ketutgaring@garing.com','',12),(6,'Cenk','Blonk',NULL,NULL,NULL,NULL,'cenk.blonk@wayang.id','',13),(7,'Test','Tester',NULL,NULL,NULL,NULL,'test123@ggmail.com','',14),(8,'Asd','Asd',NULL,NULL,NULL,NULL,'asd@gg.com','',15);
/*!40000 ALTER TABLE `tb_profiluser` ENABLE KEYS */;

--
-- Table structure for table `tb_rental`
--

DROP TABLE IF EXISTS `tb_rental`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_rental` (
  `ID` int(10) NOT NULL AUTO_INCREMENT,
  `ID_toko` int(10) DEFAULT NULL,
  `ID_user` int(10) DEFAULT NULL,
  `tgl_sewa` date DEFAULT NULL,
  `tgl_kembali` date DEFAULT NULL,
  `status` int(10) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `ID_toko` (`ID_toko`),
  KEY `ID_user` (`ID_user`),
  KEY `status` (`status`)
) ENGINE=MyISAM AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_rental`
--

/*!40000 ALTER TABLE `tb_rental` DISABLE KEYS */;
INSERT INTO `tb_rental` VALUES (1,1,2,'2016-11-02','2016-11-09',1);
/*!40000 ALTER TABLE `tb_rental` ENABLE KEYS */;

--
-- Table structure for table `tb_status_rental`
--

DROP TABLE IF EXISTS `tb_status_rental`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_status_rental` (
  `ID` int(10) NOT NULL AUTO_INCREMENT,
  `status_rental` varchar(20) DEFAULT NULL,
  `kode_status` char(3) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=MyISAM AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_status_rental`
--

/*!40000 ALTER TABLE `tb_status_rental` DISABLE KEYS */;
INSERT INTO `tb_status_rental` VALUES (1,'Booking','BKG'),(2,'Accepted','ACC'),(3,'Rejected','RJC');
/*!40000 ALTER TABLE `tb_status_rental` ENABLE KEYS */;

--
-- Table structure for table `tb_status_user`
--

DROP TABLE IF EXISTS `tb_status_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_status_user` (
  `ID` int(10) NOT NULL AUTO_INCREMENT,
  `status` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=MyISAM AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_status_user`
--

/*!40000 ALTER TABLE `tb_status_user` DISABLE KEYS */;
INSERT INTO `tb_status_user` VALUES (1,'Belum Verifikasi'),(2,'Verifikasi');
/*!40000 ALTER TABLE `tb_status_user` ENABLE KEYS */;

--
-- Table structure for table `tb_tipekendaraan`
--

DROP TABLE IF EXISTS `tb_tipekendaraan`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_tipekendaraan` (
  `ID` int(10) NOT NULL AUTO_INCREMENT,
  `tipe_kendaraan` varchar(10) DEFAULT NULL,
  `kode_tipekendaraan` char(3) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=MyISAM AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_tipekendaraan`
--

/*!40000 ALTER TABLE `tb_tipekendaraan` DISABLE KEYS */;
INSERT INTO `tb_tipekendaraan` VALUES (1,'Motor','MTR'),(2,'Mobil','MBL');
/*!40000 ALTER TABLE `tb_tipekendaraan` ENABLE KEYS */;

--
-- Table structure for table `tb_toko`
--

DROP TABLE IF EXISTS `tb_toko`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_toko` (
  `ID` int(10) NOT NULL AUTO_INCREMENT,
  `nama_toko` varchar(30) DEFAULT NULL,
  `id_pemilik` int(10) DEFAULT NULL,
  `alamat` text,
  `no_telp` char(13) DEFAULT NULL,
  `email` varchar(25) DEFAULT NULL,
  `jam_buka` time DEFAULT NULL,
  `jam_tutup` time DEFAULT NULL,
  `lat` double DEFAULT NULL,
  `lng` double DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `id_pemilik` (`id_pemilik`)
) ENGINE=MyISAM AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_toko`
--

/*!40000 ALTER TABLE `tb_toko` DISABLE KEYS */;
INSERT INTO `tb_toko` VALUES (1,'SugikRent',1,'Jl. Tunjung Sari','03618444057','sugi.almantara@yahoo.co.i',NULL,NULL,NULL,NULL),(2,'JoniRent',10,'Badung','081999988','joni@rent.com',NULL,NULL,NULL,NULL);
/*!40000 ALTER TABLE `tb_toko` ENABLE KEYS */;

--
-- Table structure for table `tb_tokonew`
--

DROP TABLE IF EXISTS `tb_tokonew`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_tokonew` (
  `ID` int(10) NOT NULL,
  `nama_toko` varchar(30) COLLATE utf8_unicode_ci NOT NULL,
  `id_pemilik` int(10) NOT NULL,
  `alamat` text COLLATE utf8_unicode_ci NOT NULL,
  `no_telp` char(13) COLLATE utf8_unicode_ci NOT NULL,
  `email` varchar(25) COLLATE utf8_unicode_ci NOT NULL,
  `jam_buka` time NOT NULL,
  `jam_tutup` time NOT NULL,
  `lat` double NOT NULL,
  `lng` double NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_tokonew`
--

/*!40000 ALTER TABLE `tb_tokonew` DISABLE KEYS */;
/*!40000 ALTER TABLE `tb_tokonew` ENABLE KEYS */;

--
-- Table structure for table `tb_user`
--

DROP TABLE IF EXISTS `tb_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_user` (
  `ID` int(10) NOT NULL AUTO_INCREMENT,
  `nama_user` varchar(50) DEFAULT NULL,
  `user_pass` varchar(20) DEFAULT NULL,
  `user_registered` datetime DEFAULT NULL,
  `status` int(2) DEFAULT '1',
  `token` varchar(100) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `status` (`status`)
) ENGINE=MyISAM AUTO_INCREMENT=16 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_user`
--

/*!40000 ALTER TABLE `tb_user` DISABLE KEYS */;
INSERT INTO `tb_user` VALUES (1,'sugik','asdfghj','2016-11-01 10:55:23',2,''),(2,'putu','qwertyu','2016-11-01 10:55:55',1,''),(8,'made','made123','2016-11-07 17:18:45',1,''),(9,'nyoman','nyoman123','2016-11-07 17:19:26',1,''),(10,'Joni123','asdf','2016-11-08 16:48:34',2,''),(12,'garing123','asdfg','2016-11-08 17:11:36',1,''),(13,'cenkblonk','cenkcenk','2016-11-09 19:52:41',1,''),(14,'test123','1234','2016-11-10 18:28:47',1,''),(15,'asd1','asdfg','2016-11-14 23:23:43',1,'');
/*!40000 ALTER TABLE `tb_user` ENABLE KEYS */;

--
-- Table structure for table `tbl_users`
--

DROP TABLE IF EXISTS `tbl_users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tbl_users` (
  `userID` int(11) NOT NULL AUTO_INCREMENT,
  `userName` varchar(20) NOT NULL,
  `userProfession` varchar(50) NOT NULL,
  `userPic` varchar(200) NOT NULL,
  PRIMARY KEY (`userID`)
) ENGINE=MyISAM AUTO_INCREMENT=53 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tbl_users`
--

/*!40000 ALTER TABLE `tbl_users` DISABLE KEYS */;
INSERT INTO `tbl_users` VALUES (52,'gffg','fdgdfgdf','978324.jpg'),(51,'Sugik','Mahasiswa','758055.jpg');
/*!40000 ALTER TABLE `tbl_users` ENABLE KEYS */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2017-03-03  2:12:36
