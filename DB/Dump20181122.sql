-- MySQL dump 10.13  Distrib 5.7.17, for Win64 (x86_64)
--
-- Host: localhost    Database: csi5380
-- ------------------------------------------------------
-- Server version	5.7.20-log

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
-- Table structure for table `address`
--

DROP TABLE IF EXISTS `address`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `address` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `user_id` int(10) unsigned NOT NULL DEFAULT '0',
  `street` varchar(100) NOT NULL,
  `province` varchar(20) NOT NULL,
  `country` varchar(20) NOT NULL,
  `zip` varchar(20) NOT NULL,
  `phone` varchar(20) DEFAULT NULL,
  `type` enum('billing','shipping') DEFAULT 'billing',
  PRIMARY KEY (`id`),
  KEY `FK_address_user` (`user_id`),
  CONSTRAINT `FK_address_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `address`
--

LOCK TABLES `address` WRITE;
/*!40000 ALTER TABLE `address` DISABLE KEYS */;
INSERT INTO `address` VALUES (7,4,'Bronson','On','Canada','12781k','1234561212','shipping'),(8,4,'King','On','Canada','1rc781','9876540000','billing'),(9,7,'Bronson','On','Canada','r8881k','4445612124','shipping'),(10,7,'King','On','Canada','1rc781','1176540000','billing'),(11,8,'94 Granton','Ontario','Canada','k2b1x3','6132557300','shipping'),(12,8,'94 Granton','Ontario','Canada','k2b1x3','6132557300','billing'),(13,9,'ericc','eric','eric','100100','1001001001','shipping'),(14,9,'hi','hihi','hihi','100100','1001001001','billing'),(15,10,'Bronson','On','Canada','12781k','1234561212',NULL),(16,10,'King','On','Canada','1rc781','9876540000',NULL),(17,11,'11 nl','wb','in','12345678','1234567890','shipping'),(18,11,'11 nl','wb','in','12345677','1234567890','billing');
/*!40000 ALTER TABLE `address` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `book`
--

DROP TABLE IF EXISTS `book`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `book` (
  `bookid` varchar(20) NOT NULL,
  `title` varchar(100) NOT NULL,
  `price` int(10) unsigned NOT NULL,
  `author` varchar(100) NOT NULL,
  `category` enum('Biography and Memoir','Business and Finance','Computers','Entertainment','History','Fiction','Science Fiction','Self-Help','Health','Science and Nature','Poetry') NOT NULL,
  `sentiment_score` double NOT NULL,
  PRIMARY KEY (`bookid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `book`
--

LOCK TABLES `book` WRITE;
/*!40000 ALTER TABLE `book` DISABLE KEYS */;
INSERT INTO `book` VALUES ('0132350882','CLEAN CODE: A HANDBOOK OF AGILE SOFTWARE CRAFTSMANSHIP',1599,'Robert C. Martin','Computers',2.00040494),('0786965614','MONSTER MANUAL',2000,'Wizards Rpg Team','Entertainment',2.346356),('1118008189','HTML AND CSS: DESIGN AND BUILD WEBSITES',1599,'Jon Duckett','Computers',1.3658698),('1476795924','LEADERSHIP: IN TURBULENT TIMES',2000,'Doris Kearns Goodwin','History',0.3856826),('1501175513','FEAR: TRUMP IN THE WHITE HOUSE',2000,'Bob Woodward','Biography and Memoir',3.939845798),('1627794247','ROBIN',1599,'Dave Itzkoff','Biography and Memoir',1.4237243);
/*!40000 ALTER TABLE `book` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `po`
--

DROP TABLE IF EXISTS `po`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `po` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `lname` varchar(20) NOT NULL,
  `fname` varchar(20) NOT NULL,
  `status` enum('ORDERED','PROCESSED','DENIED') NOT NULL,
  `address` int(10) unsigned NOT NULL,
  PRIMARY KEY (`id`),
  KEY `address` (`address`),
  CONSTRAINT `po_ibfk_1` FOREIGN KEY (`address`) REFERENCES `address` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `po`
--

LOCK TABLES `po` WRITE;
/*!40000 ALTER TABLE `po` DISABLE KEYS */;
/*!40000 ALTER TABLE `po` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `poitem`
--

DROP TABLE IF EXISTS `poitem`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `poitem` (
  `id` int(10) unsigned NOT NULL,
  `bookid` varchar(20) NOT NULL,
  `price` int(10) unsigned NOT NULL,
  PRIMARY KEY (`id`,`bookid`),
  KEY `id` (`id`),
  KEY `bookid` (`bookid`),
  CONSTRAINT `poitem_ibfk_1` FOREIGN KEY (`id`) REFERENCES `po` (`id`) ON DELETE CASCADE,
  CONSTRAINT `poitem_ibfk_2` FOREIGN KEY (`bookid`) REFERENCES `book` (`bookid`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `poitem`
--

LOCK TABLES `poitem` WRITE;
/*!40000 ALTER TABLE `poitem` DISABLE KEYS */;
/*!40000 ALTER TABLE `poitem` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `user_name` varchar(50) NOT NULL,
  `password` varchar(50) NOT NULL,
  `first_name` varchar(50) NOT NULL,
  `last_name` varchar(50) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (4,'kghosh','12345','Kay','Ghosh'),(7,'kate','12345','Kate','Ghosh'),(8,'Sareh','Soleimani','Sareh','Soleimani'),(9,'eric','hithere','eric','ericc'),(10,'moumitag','12345','Moumita','Ghosh'),(11,'sormi','12345','Sormi','Das');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `visitevent`
--

DROP TABLE IF EXISTS `visitevent`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `visitevent` (
  `day` varchar(8) NOT NULL,
  `bookid` varchar(20) NOT NULL,
  `eventtype` enum('VIEW','CART','PURCHASE') NOT NULL,
  KEY `bookid` (`bookid`),
  CONSTRAINT `visitevent_ibfk_1` FOREIGN KEY (`bookid`) REFERENCES `book` (`bookid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `visitevent`
--

LOCK TABLES `visitevent` WRITE;
/*!40000 ALTER TABLE `visitevent` DISABLE KEYS */;
INSERT INTO `visitevent` VALUES ('12202018','1627794247','VIEW'),('12242018','1627794247','CART'),('12252018','1627794247','PURCHASE'),('10202018','0132350882','VIEW'),('12242018','0132350882','CART'),('12252018','0132350882','PURCHASE'),('01202018','1118008189','VIEW'),('01242018','1118008189','CART'),('02252018','1118008189','PURCHASE');
/*!40000 ALTER TABLE `visitevent` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2018-11-22 17:20:31
