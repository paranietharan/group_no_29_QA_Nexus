CREATE DATABASE  IF NOT EXISTS `qa_training` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `qa_training`;
-- MySQL dump 10.13  Distrib 8.0.40, for Win64 (x86_64)
--
-- Host: localhost    Database: qa_training
-- ------------------------------------------------------
-- Server version	8.0.40

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `categories`
--

DROP TABLE IF EXISTS `categories`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `categories` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(10) NOT NULL,
  `parent_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKbh9wbkic0w5mhjpaqi6mkt4ie` (`name`,`parent_id`),
  KEY `FKsaok720gsu4u2wrgbk10b5n8d` (`parent_id`),
  CONSTRAINT `FKsaok720gsu4u2wrgbk10b5n8d` FOREIGN KEY (`parent_id`) REFERENCES `categories` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=58 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `categories`
--

LOCK TABLES `categories` WRITE;
/*!40000 ALTER TABLE `categories` DISABLE KEYS */;
INSERT INTO `categories` VALUES (39,'AB8535',NULL),(42,'Carnat2786',NULL),(35,'Carnat4681',NULL),(34,'Carnat5783',NULL),(37,'Carnations',NULL),(19,'Catego1265',NULL),(20,'Catego1300',NULL),(21,'Catego1359',NULL),(22,'Catego1385',NULL),(23,'Catego1403',NULL),(24,'Catego1425',NULL),(25,'Catego1446',NULL),(26,'Catego1469',NULL),(27,'Catego1493',NULL),(28,'Catego1517',NULL),(29,'Catego1539',NULL),(30,'Catego1559',NULL),(31,'Catego1581',NULL),(32,'Catego1608',NULL),(33,'Catego1630',NULL),(43,'Catego8551',NULL),(44,'Catego8602',NULL),(45,'Catego8627',NULL),(46,'Catego8646',NULL),(47,'Catego8661',NULL),(48,'Catego8679',NULL),(49,'Catego8695',NULL),(50,'Catego8712',NULL),(51,'Catego8729',NULL),(52,'Catego8743',NULL),(53,'Catego8765',NULL),(54,'Catego8784',NULL),(55,'Catego8804',NULL),(56,'Catego8821',NULL),(57,'Catego8838',NULL),(17,'Child11088',NULL),(18,'Child21107',NULL),(6,'Dahlia8736',NULL),(8,'DahliaNew',NULL),(1,'Indoor',NULL),(4,'new',2),(11,'Orchid',NULL),(2,'outdoor',NULL),(16,'Parent1049',NULL),(15,'Peony570',NULL),(7,'RedRo8963',NULL),(41,'Spring5914',NULL),(36,'Spring6894',35),(3,'sub',1),(5,'sub1',1),(13,'Sub1395',NULL),(14,'Sub2411',NULL),(10,'Sunflo9721',NULL),(40,'VeryLo8458',NULL);
/*!40000 ALTER TABLE `categories` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `inventory`
--

DROP TABLE IF EXISTS `inventory`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `inventory` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `note` varchar(255) DEFAULT NULL,
  `quantity` int NOT NULL,
  `type` enum('IN','OUT') NOT NULL,
  `plant_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKkpd5nc87b63jxt98yeuj6ryak` (`plant_id`),
  CONSTRAINT `FKkpd5nc87b63jxt98yeuj6ryak` FOREIGN KEY (`plant_id`) REFERENCES `plants` (`id`),
  CONSTRAINT `inventory_chk_1` CHECK ((`quantity` >= 1))
) ENGINE=InnoDB AUTO_INCREMENT=55 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `inventory`
--

LOCK TABLES `inventory` WRITE;
/*!40000 ALTER TABLE `inventory` DISABLE KEYS */;
INSERT INTO `inventory` VALUES (1,'2026-01-18 18:21:50.832024','Sale',1,'OUT',1),(2,'2026-01-19 11:18:19.547147','Sale',8,'OUT',2),(3,'2026-01-19 12:56:29.806268','Sale',2,'OUT',1),(4,'2026-02-04 11:28:43.990233','Sale',3,'OUT',3),(5,'2026-02-04 11:28:54.731431','Sale',1,'OUT',2),(6,'2026-02-04 22:57:17.528504','Sale',5,'OUT',1),(7,'2026-02-04 22:57:25.738822','Sale',1,'OUT',5),(8,'2026-02-04 22:57:34.255311','Sale',2,'OUT',4),(9,'2026-02-04 22:58:38.676421','Sale',2,'OUT',3),(10,'2026-02-04 22:58:45.651926','Sale',5,'OUT',7),(11,'2026-02-04 22:58:53.900166','Sale',3,'OUT',6),(12,'2026-02-04 22:59:04.450220','Sale',3,'OUT',1),(13,'2026-02-04 22:59:10.936980','Sale',4,'OUT',8),(14,'2026-02-05 14:29:07.874456','Sale',1,'OUT',1),(15,'2026-02-05 14:45:57.813323','Sale',1,'OUT',2),(16,'2026-02-05 14:47:02.642012','Sale',1,'OUT',1),(17,'2026-02-05 14:58:50.968255','Sale',1,'OUT',1),(18,'2026-02-05 18:14:48.742035','Sale',2,'OUT',1),(19,'2026-02-05 18:14:56.040755','Sale',2,'OUT',2),(20,'2026-02-05 18:15:03.434308','Sale',4,'OUT',3),(21,'2026-02-05 18:15:11.978557','Sale',12,'OUT',9),(22,'2026-02-05 18:15:19.772790','Sale',12,'OUT',4),(23,'2026-02-05 18:15:26.732793','Sale',4,'OUT',8),(24,'2026-02-05 18:15:34.840807','Sale',5,'OUT',6),(25,'2026-02-05 18:16:44.100383','Sale',10,'OUT',6),(26,'2026-02-05 18:16:52.936349','Sale',8,'OUT',5),(27,'2026-02-05 18:17:00.823730','Sale',7,'OUT',10),(28,'2026-02-05 18:17:07.699237','Sale',7,'OUT',11),(29,'2026-02-05 18:17:17.610582','Sale',10,'OUT',6),(30,'2026-02-05 18:17:23.855765','Sale',2,'OUT',1),(31,'2026-02-05 18:17:30.621758','Sale',8,'OUT',12),(32,'2026-02-05 20:39:53.815655','Sale',1,'OUT',1),(33,'2026-02-05 20:43:13.217495','Sale',1,'OUT',1),(34,'2026-02-05 23:25:10.020405','Sale',1,'OUT',1),(35,'2026-02-06 10:43:54.017719','Sale',1,'OUT',1),(36,'2026-02-06 10:43:55.318196','Sale',1,'OUT',1),(37,'2026-02-06 12:35:16.184970','Sale',1,'OUT',2),(38,'2026-02-06 13:26:55.365500','Sale',1,'OUT',2),(39,'2026-02-06 13:29:49.675618','Sale',1,'OUT',4),(40,'2026-02-06 13:38:13.541168','Sale',1,'OUT',2),(41,'2026-02-06 16:35:08.593118','Sale',1,'OUT',2),(42,'2026-02-06 17:32:04.741758','Sale',1,'OUT',2),(43,'2026-02-06 21:45:48.874676','Sale',1,'OUT',2),(44,'2026-02-06 22:07:45.816993','Sale',1,'OUT',2),(45,'2026-02-06 22:19:39.739022','Sale',1,'OUT',3),(46,'2026-02-06 22:50:22.405648','Sale',1,'OUT',3),(47,'2026-02-06 23:07:05.111876','Sale',1,'OUT',3),(48,'2026-02-07 01:13:38.263783','Sale',1,'OUT',3),(49,'2026-02-07 01:16:01.547305','Sale',1,'OUT',3),(50,'2026-02-07 01:19:23.559136','Sale',1,'OUT',3),(51,'2026-02-07 01:22:37.171583','Sale',1,'OUT',3),(52,'2026-02-07 01:24:28.959876','Sale',1,'OUT',3),(53,'2026-02-07 11:38:07.817884','Sale',1,'OUT',3),(54,'2026-02-07 12:14:49.390266','Sale',1,'OUT',3);
/*!40000 ALTER TABLE `inventory` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `plants`
--

DROP TABLE IF EXISTS `plants`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `plants` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(25) NOT NULL,
  `price` double NOT NULL,
  `quantity` int NOT NULL,
  `category_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKnles3upetjyjq9otvk8rsfc00` (`name`,`category_id`),
  KEY `FKgk96wou3hd3xfgqlisa1e0lyc` (`category_id`),
  CONSTRAINT `FKgk96wou3hd3xfgqlisa1e0lyc` FOREIGN KEY (`category_id`) REFERENCES `categories` (`id`),
  CONSTRAINT `plants_chk_1` CHECK ((`quantity` >= 0))
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `plants`
--

LOCK TABLES `plants` WRITE;
/*!40000 ALTER TABLE `plants` DISABLE KEYS */;
INSERT INTO `plants` VALUES (1,'anthurium',200,0,3),(2,'rose',2000,0,4),(3,'shoe',100,0,4),(4,'jasmine',200,6,4),(5,'apple',300,10,4),(6,'orange',250,20,4),(7,'mango',350,6,3),(8,'guava',450,6,4),(9,'tulip',450,21,4),(10,'rose-pink',220,11,3),(11,'banana',200,18,5),(12,'papaya',400,20,3);
/*!40000 ALTER TABLE `plants` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sales`
--

DROP TABLE IF EXISTS `sales`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sales` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `quantity` int NOT NULL,
  `sold_at` datetime(6) DEFAULT NULL,
  `total_price` double NOT NULL,
  `plant_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK704q9anh1jrbsfiqsett1xbge` (`plant_id`),
  CONSTRAINT `FK704q9anh1jrbsfiqsett1xbge` FOREIGN KEY (`plant_id`) REFERENCES `plants` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=55 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sales`
--

LOCK TABLES `sales` WRITE;
/*!40000 ALTER TABLE `sales` DISABLE KEYS */;
INSERT INTO `sales` VALUES (25,10,'2026-02-05 18:16:44.100383',2500,6),(26,8,'2026-02-05 18:16:52.936349',2400,5),(27,7,'2026-02-05 18:17:00.823730',1540,10),(35,1,'2026-02-06 10:43:54.017719',200,1),(37,1,'2026-02-06 12:35:16.183988',2000,2),(38,1,'2026-02-06 13:26:55.365500',2000,2),(47,1,'2026-02-06 23:07:05.111876',100,3),(49,1,'2026-02-07 01:16:01.547305',100,3),(50,1,'2026-02-07 01:19:23.559136',100,3),(54,1,'2026-02-07 12:14:49.389265',100,3);
/*!40000 ALTER TABLE `sales` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping events for database 'qa_training'
--

--
-- Dumping routines for database 'qa_training'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-02-07 12:38:56
