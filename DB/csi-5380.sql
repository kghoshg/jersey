-- --------------------------------------------------------
-- Host:                         127.0.0.1
-- Server version:               5.7.20-log - MySQL Community Server (GPL)
-- Server OS:                    Win64
-- HeidiSQL Version:             9.4.0.5125
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;


-- Dumping database structure for csi5380
CREATE DATABASE IF NOT EXISTS `csi5380` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `csi5380`;

-- Dumping structure for table csi5380.address
CREATE TABLE IF NOT EXISTS `address` (
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
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;

-- Dumping data for table csi5380.address: ~2 rows (approximately)
/*!40000 ALTER TABLE `address` DISABLE KEYS */;
INSERT IGNORE INTO `address` (`id`, `user_id`, `street`, `province`, `country`, `zip`, `phone`, `type`) VALUES
	(7, 4, 'Bronson', 'On', 'Canada', '12781k', '1234561212', 'shipping'),
	(8, 4, 'King', 'On', 'Canada', '1rc781', '9876540000', 'billing');
/*!40000 ALTER TABLE `address` ENABLE KEYS */;

-- Dumping structure for table csi5380.book
CREATE TABLE IF NOT EXISTS `book` (
  `bookid` varchar(20) NOT NULL,
  `title` varchar(100) NOT NULL,
  `price` int(10) unsigned NOT NULL,
  `author` varchar(100) NOT NULL,
  `category` enum('Biography and Memoir','Business and Finance','Computers','Entertainment','History','Fiction','Science Fiction','Self-Help','Health','Science and Nature','Poetry') NOT NULL,
  PRIMARY KEY (`bookid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Dumping data for table csi5380.book: ~6 rows (approximately)
/*!40000 ALTER TABLE `book` DISABLE KEYS */;
INSERT IGNORE INTO `book` (`bookid`, `title`, `price`, `author`, `category`) VALUES
	('0132350882', 'CLEAN CODE: A HANDBOOK OF AGILE SOFTWARE CRAFTSMANSHIP', 1599, 'Robert C. Martin', 'Computers'),
	('0786965614', 'MONSTER MANUAL', 2000, 'Wizards Rpg Team', 'Entertainment'),
	('1118008189', 'HTML AND CSS: DESIGN AND BUILD WEBSITES', 1599, 'Jon Duckett', 'Computers'),
	('1476795924', 'LEADERSHIP: IN TURBULENT TIMES', 2000, 'Doris Kearns Goodwin', 'History'),
	('1501175513', 'FEAR: TRUMP IN THE WHITE HOUSE', 2000, 'Bob Woodward', 'Biography and Memoir'),
	('1627794247', 'ROBIN', 1599, 'Dave Itzkoff', 'Biography and Memoir');
/*!40000 ALTER TABLE `book` ENABLE KEYS */;

-- Dumping structure for table csi5380.po
CREATE TABLE IF NOT EXISTS `po` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `lname` varchar(20) NOT NULL,
  `fname` varchar(20) NOT NULL,
  `status` enum('ORDERED','PROCESSED','DENIED') NOT NULL,
  `address` int(10) unsigned NOT NULL,
  PRIMARY KEY (`id`),
  KEY `address` (`address`),
  CONSTRAINT `po_ibfk_1` FOREIGN KEY (`address`) REFERENCES `address` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

-- Dumping data for table csi5380.po: ~0 rows (approximately)
/*!40000 ALTER TABLE `po` DISABLE KEYS */;
/*!40000 ALTER TABLE `po` ENABLE KEYS */;

-- Dumping structure for table csi5380.poitem
CREATE TABLE IF NOT EXISTS `poitem` (
  `id` int(10) unsigned NOT NULL,
  `bookid` varchar(20) NOT NULL,
  `price` int(10) unsigned NOT NULL,
  PRIMARY KEY (`id`,`bookid`),
  KEY `id` (`id`),
  KEY `bookid` (`bookid`),
  CONSTRAINT `poitem_ibfk_1` FOREIGN KEY (`id`) REFERENCES `po` (`id`) ON DELETE CASCADE,
  CONSTRAINT `poitem_ibfk_2` FOREIGN KEY (`bookid`) REFERENCES `book` (`bookid`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Dumping data for table csi5380.poitem: ~0 rows (approximately)
/*!40000 ALTER TABLE `poitem` DISABLE KEYS */;
/*!40000 ALTER TABLE `poitem` ENABLE KEYS */;

-- Dumping structure for table csi5380.user
CREATE TABLE IF NOT EXISTS `user` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `user_name` varchar(50) NOT NULL,
  `password` varchar(50) NOT NULL,
  `first_name` varchar(50) NOT NULL,
  `last_name` varchar(50) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

-- Dumping data for table csi5380.user: ~2 rows (approximately)
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT IGNORE INTO `user` (`id`, `user_name`, `password`, `first_name`, `last_name`) VALUES
	(4, 'kghosh', '12345', 'Kay', 'Ghosh');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;

-- Dumping structure for table csi5380.visitevent
CREATE TABLE IF NOT EXISTS `visitevent` (
  `day` varchar(8) NOT NULL,
  `bookid` varchar(20) NOT NULL,
  `eventtype` enum('VIEW','CART','PURCHASE') NOT NULL,
  KEY `bookid` (`bookid`),
  CONSTRAINT `visitevent_ibfk_1` FOREIGN KEY (`bookid`) REFERENCES `book` (`bookid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Dumping data for table csi5380.visitevent: ~9 rows (approximately)
/*!40000 ALTER TABLE `visitevent` DISABLE KEYS */;
INSERT IGNORE INTO `visitevent` (`day`, `bookid`, `eventtype`) VALUES
	('12202018', '1627794247', 'VIEW'),
	('12242018', '1627794247', 'CART'),
	('12252018', '1627794247', 'PURCHASE'),
	('10202018', '0132350882', 'VIEW'),
	('12242018', '0132350882', 'CART'),
	('12252018', '0132350882', 'PURCHASE'),
	('01202018', '1118008189', 'VIEW'),
	('01242018', '1118008189', 'CART'),
	('02252018', '1118008189', 'PURCHASE');
/*!40000 ALTER TABLE `visitevent` ENABLE KEYS */;

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
