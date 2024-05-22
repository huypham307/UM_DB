-- MySQL dump 10.13  Distrib 8.3.0, for macos14.2 (arm64)
--
-- Host: localhost    Database: QuackstagramDB
-- ------------------------------------------------------
-- Server version	8.3.0

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `follows`
--

DROP TABLE IF EXISTS `follows`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `follows` (
  `follower_id` int DEFAULT NULL,
  `followee_id` int DEFAULT NULL,
  UNIQUE KEY `unique_follower_followee` (`follower_id`,`followee_id`),
  KEY `followee_id` (`followee_id`),
  CONSTRAINT `follows_ibfk_1` FOREIGN KEY (`follower_id`) REFERENCES `users` (`user_id`),
  CONSTRAINT `follows_ibfk_2` FOREIGN KEY (`followee_id`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `follows`
--

LOCK TABLES `follows` WRITE;
/*!40000 ALTER TABLE `follows` DISABLE KEYS */;
INSERT INTO `follows` VALUES (1,2),(1,3),(1,4),(2,1),(2,3),(3,2),(4,1),(4,3),(5,2),(5,4),(6,4),(14,4),(18,2),(18,3),(19,2),(19,4),(67,2),(67,3),(67,4);
/*!40000 ALTER TABLE `follows` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `image_data`
--

DROP TABLE IF EXISTS `image_data`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `image_data` (
  `image_id` varchar(32) NOT NULL,
  `user_id` int DEFAULT NULL,
  `image_bio` text,
  `post_time` datetime DEFAULT NULL,
  `file_path` varchar(128) DEFAULT NULL,
  PRIMARY KEY (`image_id`),
  UNIQUE KEY `image_id` (`image_id`),
  KEY `idx_user_id` (`user_id`),
  CONSTRAINT `image_data_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `image_data`
--

LOCK TABLES `image_data` WRITE;
/*!40000 ALTER TABLE `image_data` DISABLE KEYS */;
INSERT INTO `image_data` VALUES ('kidprobambo123_1',18,'Enter a caption','2024-03-03 15:42:35','src/main/java/img/uploaded/kidprobambo123_1.jpg'),('kidprobambo123_2',18,'Enter a caption','2024-03-03 15:42:42','src/main/java/img/uploaded/kidprobambo123_2.JPG'),('kidprobambo123_3',18,'Enter a captionáiudhasiudhasdhiuahsdiahdiahsdiuhasdiuhasiudhaiusdhiaushdiuahsdiahsdiuashdiuioasdiuahsdiuhasasdasdasd','2024-03-04 13:21:46','src/main/java/img/uploaded/kidprobambo123_3.jpg'),('Lorin_1',1,'In the cookie jar my hand was not.','2023-12-17 19:07:43','src/main/java/img/uploaded/Lorin_1.png'),('Lorin_2',1,'Meditate I must.','2023-12-17 19:09:35','src/main/java/img/uploaded/Lorin_2.png'),('Lorin_4',1,'Enter a caption','2024-05-16 11:33:08','src/main/java/img/uploaded/Lorin_4.png'),('Mystar_1',4,'Cookies gone?','2023-12-17 19:26:50','src/main/java/img/uploaded/Mystar_1.png'),('Mystar_2',4,'In my soup a fly is.','2023-12-17 19:27:24','src/main/java/img/uploaded/Mystar_2.png'),('Xylo_1',2,'My tea strong as Force is.','2023-12-17 19:22:40','src/main/java/img/uploaded/Xylo_1.png'),('Xylo_2',2,'Jedi mind trick failed.','2023-12-17 19:23:14','src/main/java/img/uploaded/Xylo_2.png'),('Xylo_3',2,'Enter a caption','2024-03-05 15:34:13','src/main/java/img/uploaded/Xylo_3.jpeg'),('Xylo_4',2,'Enter a caption','2024-03-06 09:30:00','src/main/java/img/uploaded/Xylo_4.jpeg'),('Zara_1',3,'Lost my map I have. Oops.','2023-12-17 19:24:31','src/main/java/img/uploaded/Zara_1.png'),('Zara_2',3,'Yoga with Yoda','2023-12-17 19:25:03','src/main/java/img/uploaded/Zara_2.png');
/*!40000 ALTER TABLE `image_data` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `posts`
--

DROP TABLE IF EXISTS `posts`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `posts` (
  `post_id` int NOT NULL AUTO_INCREMENT,
  `liker_id` int NOT NULL,
  `image_id` varchar(32) NOT NULL,
  `time` datetime DEFAULT NULL,
  PRIMARY KEY (`post_id`),
  UNIQUE KEY `unique_liker_image` (`liker_id`,`image_id`),
  KEY `image_id` (`image_id`),
  KEY `idx_user_id` (`liker_id`),
  CONSTRAINT `posts_ibfk_1` FOREIGN KEY (`liker_id`) REFERENCES `users` (`user_id`),
  CONSTRAINT `posts_ibfk_3` FOREIGN KEY (`image_id`) REFERENCES `image_data` (`image_id`)
) ENGINE=InnoDB AUTO_INCREMENT=87 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `posts`
--

LOCK TABLES `posts` WRITE;
/*!40000 ALTER TABLE `posts` DISABLE KEYS */;
INSERT INTO `posts` VALUES (62,1,'Mystar_1','2024-05-11 20:01:19'),(63,1,'Mystar_2','2024-05-11 20:02:10'),(64,1,'Zara_1','2024-05-12 15:23:51'),(65,1,'Xylo_1','2024-05-12 17:00:17'),(66,1,'Xylo_2','2024-05-12 17:00:18'),(68,1,'Xylo_3','2024-05-12 17:00:21'),(69,4,'Lorin_1','2024-05-12 18:06:33'),(70,4,'Lorin_2','2024-05-12 18:06:35'),(71,1,'Xylo_4','2024-05-16 10:58:45'),(75,1,'Zara_2','2024-05-16 10:58:49'),(76,67,'Mystar_1','2024-05-21 21:13:43'),(77,67,'Mystar_2','2024-05-21 21:13:44'),(79,67,'Zara_1','2024-05-21 21:13:53'),(80,67,'Zara_2','2024-05-21 21:13:55'),(81,67,'Xylo_1','2024-05-21 21:14:08'),(82,67,'Xylo_2','2024-05-21 21:14:09'),(83,67,'Xylo_3','2024-05-21 21:14:11'),(84,67,'Xylo_4','2024-05-21 21:14:12');
/*!40000 ALTER TABLE `posts` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sessions`
--

DROP TABLE IF EXISTS `sessions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sessions` (
  `session_id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NOT NULL,
  PRIMARY KEY (`session_id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `sessions_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=127 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sessions`
--

LOCK TABLES `sessions` WRITE;
/*!40000 ALTER TABLE `sessions` DISABLE KEYS */;
INSERT INTO `sessions` VALUES (19,1),(20,1),(21,1),(22,1),(23,1),(24,1),(25,1),(26,1),(27,1),(28,1),(29,1),(30,1),(31,1),(32,1),(33,1),(34,1),(35,1),(36,1),(37,1),(38,1),(39,1),(40,1),(41,1),(42,1),(43,1),(44,1),(45,1),(46,1),(47,1),(48,1),(49,1),(50,1),(51,1),(52,1),(53,1),(54,1),(55,1),(56,1),(57,1),(58,1),(59,1),(60,1),(61,1),(62,1),(63,1),(64,1),(65,1),(66,1),(67,1),(68,1),(69,1),(70,1),(71,1),(72,1),(73,1),(74,1),(75,1),(76,1),(77,1),(78,1),(79,1),(80,1),(81,1),(82,1),(83,1),(84,1),(85,1),(86,1),(87,1),(88,1),(89,1),(90,1),(91,1),(92,1),(93,1),(94,1),(95,1),(96,1),(97,1),(98,1),(99,1),(100,1),(101,1),(102,1),(103,1),(104,1),(105,1),(106,1),(107,1),(108,1),(109,1),(110,1),(111,1),(112,1),(113,1),(114,1),(116,1),(117,1),(118,1),(119,1),(120,1),(122,1),(123,1),(126,1),(1,3),(5,3),(115,4),(121,4),(2,20),(3,20),(4,20),(6,20),(7,20),(8,20),(9,20),(13,20),(10,25),(14,25),(15,25),(16,25),(17,25),(18,25),(11,27),(12,27),(124,67),(125,67);
/*!40000 ALTER TABLE `sessions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_posts`
--

DROP TABLE IF EXISTS `user_posts`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_posts` (
  `user_id` int NOT NULL,
  `post_count` int DEFAULT '0',
  PRIMARY KEY (`user_id`),
  CONSTRAINT `user_posts_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_posts`
--

LOCK TABLES `user_posts` WRITE;
/*!40000 ALTER TABLE `user_posts` DISABLE KEYS */;
/*!40000 ALTER TABLE `user_posts` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Temporary view structure for view `user_session`
--

DROP TABLE IF EXISTS `user_session`;
/*!50001 DROP VIEW IF EXISTS `user_session`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `user_session` AS SELECT 
 1 AS `user_id`,
 1 AS `COUNT(session_id)`*/;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `user_id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(128) NOT NULL,
  `password` varchar(255) NOT NULL,
  `bio` text,
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=109 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'Lorin','Š4ÇŠ4Ç','Fierce warrior, not solo'),(2,'Xylo','ʛMư˽{ƤʩOƗ','Humanoid robot much like the rest'),(3,'Zara','ʛMư˽{ƤʩOƗ','Xylo and I are not the same!'),(4,'Mystar','Š4ÇŠ4Ç','Bio'),(5,'quack1','Š4ÇŠ4ÇŠ4Ç','This is my Bio'),(6,'quacksta','Š4ÇŠ4ÇŠ4Ç','Bio'),(7,'quack2','Š4ÇŠ4ÇŠ4Ç','Bio'),(8,'quack3','Š4ÇŠ4ÇŠ4Ç','Bio'),(9,'quack6','ŧ5Ëŧ5Ëŧ5Ë','Bio'),(10,'quack7','Š4ÇŠ4ÇŠ4Ç','Bio'),(11,'User111','Š4ÇŠ4ÇŠ4Ç','Bio1'),(12,'Username112','ˍų̙gǘȌ￾ƞȜXĺ','Bio12'),(13,'Username11111','ˍų̙gǘȌ￾ƞř1ÅȚXĹ','Bio12111'),(14,'hello123','˥sƗ̣ƹ̀fǅǁ3÷ȞXĻ','Bio123'),(15,'hadjfha','˥sƗʧAƹ','Bio1'),(16,'Username123','ˍų̙gǘȌ￾ƞǷ}Ë','Bi123'),(17,'Username7886','Š4ÇŠ4Ç','Bio'),(18,'quackson71','darwizzyking13',NULL),(19,'quack10','Š4ÇŠ4ÇŠ4Ç','Bio'),(20,'quack11','Š4ÇŠ4Ç',NULL),(21,'quack98','Š4ÇŠ4Ç','I am quack'),(22,'quack22','Š4ÇŠ4Ç','I am quack 22'),(23,'quack87','Š4ÇŠ4Ç','I am quack 87'),(24,'quack88','Š4ÇŠ4Ç','asdasd'),(25,'quackstar11','Š4ÇŠ4Ç','I am a star '),(26,'quackstar22','Š4ÇŠ4Ç','Bio'),(27,'quackstar28282','Š4ÇŠ4Ç','Bio'),(28,'Loran','Š4ÇŠ4Ç','Bio'),(29,'quack42','Š4ÇŠ4ÇŠ4Ç','Bio'),(30,'quack43','Š4ÇŠ4ÇŠ4Ç','Bio'),(31,'quack44','Š4ÇŠ4ÇŠ4Ç','Bio'),(32,'quack45','Š4ÇŠ4ÇŠ4Ç','Bio'),(33,'quack46','Š4ÇŠ4ÇŠ4Ç','Bio'),(34,'quack47','Š4ÇŠ4ÇŠ4Ç','Bio'),(35,'quack48','Š4ÇŠ4ÇŠ4Ç','Bio'),(36,'quack49','Š4ÇŠ4ÇŠ4Ç','Bio'),(37,'quack50','Š4ÇŠ4ÇŠ4Ç','Bio'),(38,'quack51','Š4ÇŠ4ÇŠ4Ç','Bio'),(39,'quack52','Š4ÇŠ4ÇŠ4Ç','Bio'),(40,'quack53','Š4ÇŠ4ÇŠ4Ç','Bio'),(41,'quack54','Š4ÇŠ4ÇŠ4Ç','Bio'),(42,'quack55','Š4ÇŠ4ÇŠ4Ç','Bio'),(43,'quack56','Š4ÇŠ4ÇŠ4Ç','Bio'),(44,'quack57','Š4ÇŠ4ÇŠ4Ç','Bio'),(45,'quack58','Š4ÇŠ4ÇŠ4Ç','Bio'),(46,'quack59','Š4ÇŠ4ÇŠ4Ç','Bio'),(47,'quack60','Š4ÇŠ4ÇŠ4Ç','Bio'),(48,'quack61','Š4ÇŠ4ÇŠ4Ç','Bio'),(49,'quack62','Š4ÇŠ4ÇŠ4Ç','Bio'),(50,'quack63','Š4ÇŠ4ÇŠ4Ç','Bio'),(51,'quack64','Š4ÇŠ4ÇŠ4Ç','Bio'),(52,'quack65','Š4ÇŠ4ÇŠ4Ç','Bio'),(53,'quack66','Š4ÇŠ4ÇŠ4Ç','Bio'),(54,'quack67','Š4ÇŠ4ÇŠ4Ç','Bio'),(55,'quack68','Š4ÇŠ4ÇŠ4Ç','Bio'),(56,'quack69','Š4ÇŠ4ÇŠ4Ç','Bio'),(57,'quack70','Š4ÇŠ4ÇŠ4Ç','Bio'),(58,'quack71','Š4ÇŠ4ÇŠ4Ç','Bio'),(59,'quack72','Š4ÇŠ4ÇŠ4Ç','Bio'),(60,'quack73','Š4ÇŠ4ÇŠ4Ç','Bio'),(61,'quack74','Š4ÇŠ4ÇŠ4Ç','Bio'),(62,'quack75','Š4ÇŠ4ÇŠ4Ç','Bio'),(63,'quack76','Š4ÇŠ4ÇŠ4Ç','Bio'),(64,'quack77','Š4ÇŠ4ÇŠ4Ç','Bio'),(65,'quack78','Š4ÇŠ4ÇŠ4Ç','Bio'),(66,'quack79','Š4ÇŠ4ÇŠ4Ç','Bio'),(67,'quack80','Š4ÇŠ4ÇŠ4Ç','Bio'),(68,'quack81','Š4ÇŠ4ÇŠ4Ç','Bio'),(69,'quack82','Š4ÇŠ4ÇŠ4Ç','Bio'),(70,'quack83','Š4ÇŠ4ÇŠ4Ç','Bio'),(71,'quack84','Š4ÇŠ4ÇŠ4Ç','Bio'),(72,'quack85','Š4ÇŠ4ÇŠ4Ç','Bio'),(73,'quack86','Š4ÇŠ4ÇŠ4Ç','Bio'),(76,'quack89','Š4ÇŠ4ÇŠ4Ç','Bio'),(77,'quack90','Š4ÇŠ4ÇŠ4Ç','Bio'),(78,'quack91','Š4ÇŠ4ÇŠ4Ç','Bio'),(79,'quack92','Š4ÇŠ4ÇŠ4Ç','Bio'),(80,'quack93','Š4ÇŠ4ÇŠ4Ç','Bio'),(81,'quack94','Š4ÇŠ4ÇŠ4Ç','Bio'),(82,'quack95','Š4ÇŠ4ÇŠ4Ç','Bio'),(83,'quack96','Š4ÇŠ4ÇŠ4Ç','Bio'),(84,'quack97','Š4ÇŠ4ÇŠ4Ç','Bio'),(86,'quack99','Š4ÇŠ4ÇŠ4Ç','Bio'),(87,'quack100','Š4ÇŠ4ÇŠ4Ç','Bio'),(88,'quack101','Š4ÇŠ4ÇŠ4Ç','Bio'),(89,'quack102','Š4ÇŠ4ÇŠ4Ç','Bio'),(90,'quack103','Š4ÇŠ4ÇŠ4Ç','Bio'),(91,'quack104','Š4ÇŠ4ÇŠ4Ç','Bio'),(92,'quack105','Š4ÇŠ4ÇŠ4Ç','Bio'),(93,'quack106','Š4ÇŠ4ÇŠ4Ç','Bio'),(94,'quack107','Š4ÇŠ4ÇŠ4Ç','Bio'),(95,'quack108','Š4ÇŠ4ÇŠ4Ç','Bio'),(96,'quack109','Š4ÇŠ4ÇŠ4Ç','Bio'),(97,'quack110','Š4ÇŠ4ÇŠ4Ç','Bio'),(98,'quack111','Š4ÇŠ4ÇŠ4Ç','Bio'),(99,'quack112','Š4ÇŠ4ÇŠ4Ç','Bio'),(100,'quack113','Š4ÇŠ4ÇŠ4Ç','Bio'),(101,'quack114','Š4ÇŠ4ÇŠ4Ç','Bio'),(102,'quack115','Š4ÇŠ4ÇŠ4Ç','Bio'),(103,'quack116','Š4ÇŠ4ÇŠ4Ç','Bio'),(104,'quack117','Š4ÇŠ4ÇŠ4Ç','Bio'),(105,'quack118','Š4ÇŠ4ÇŠ4Ç','Bio'),(106,'quack119','Š4ÇŠ4ÇŠ4Ç','Bio'),(107,'quack120','Š4ÇŠ4ÇŠ4Ç','Bio'),(108,'quack121','Š4ÇŠ4ÇŠ4Ç','Bio');
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping routines for database 'QuackstagramDB'
--

--
-- Final view structure for view `user_session`
--

/*!50001 DROP VIEW IF EXISTS `user_session`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_0900_ai_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`steve`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `user_session` AS select `s`.`user_id` AS `user_id`,count(`s`.`session_id`) AS `COUNT(session_id)` from `sessions` `s` group by `s`.`user_id` */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-05-22 13:09:59
