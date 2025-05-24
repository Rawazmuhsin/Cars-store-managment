CREATE DATABASE  IF NOT EXISTS `car_store_management` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `car_store_management`;
-- MySQL dump 10.13  Distrib 8.0.41, for Win64 (x86_64)
--
-- Host: localhost    Database: car_store_management
-- ------------------------------------------------------
-- Server version	8.0.41

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
-- Table structure for table `audit_logs`
--

DROP TABLE IF EXISTS `audit_logs`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `audit_logs` (
  `log_id` int NOT NULL AUTO_INCREMENT,
  `staff_id` int DEFAULT NULL,
  `action_type` enum('login','logout','create','update','delete','view','export') NOT NULL,
  `table_affected` varchar(50) DEFAULT NULL,
  `record_id` int DEFAULT NULL,
  `action_details` text,
  `timestamp` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`log_id`),
  KEY `staff_id` (`staff_id`),
  KEY `idx_audit_timestamp` (`timestamp`),
  CONSTRAINT `audit_logs_ibfk_1` FOREIGN KEY (`staff_id`) REFERENCES `staff` (`staff_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `audit_logs`
--

LOCK TABLES `audit_logs` WRITE;
/*!40000 ALTER TABLE `audit_logs` DISABLE KEYS */;
/*!40000 ALTER TABLE `audit_logs` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `car_models`
--

DROP TABLE IF EXISTS `car_models`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `car_models` (
  `model_id` int NOT NULL AUTO_INCREMENT,
  `manufacturer_id` int NOT NULL,
  `model_name` varchar(100) NOT NULL,
  `year_introduced` year DEFAULT NULL,
  `category` enum('sedan','suv','truck','sports','hatchback','van','luxury','electric') NOT NULL,
  PRIMARY KEY (`model_id`),
  UNIQUE KEY `manufacturer_id` (`manufacturer_id`,`model_name`,`year_introduced`),
  CONSTRAINT `car_models_ibfk_1` FOREIGN KEY (`manufacturer_id`) REFERENCES `manufacturers` (`manufacturer_id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `car_models`
--

LOCK TABLES `car_models` WRITE;
/*!40000 ALTER TABLE `car_models` DISABLE KEYS */;
INSERT INTO `car_models` VALUES (1,1,'Camry',2023,'sedan'),(2,1,'RAV4',2023,'suv'),(3,2,'Civic',2023,'sedan'),(4,3,'Mustang',2023,'sports'),(5,4,'X5',2023,'suv');
/*!40000 ALTER TABLE `car_models` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cars`
--

DROP TABLE IF EXISTS `cars`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cars` (
  `car_id` int NOT NULL AUTO_INCREMENT,
  `model_id` int NOT NULL,
  `vin` varchar(17) NOT NULL,
  `color` varchar(50) NOT NULL,
  `manufacture_year` year NOT NULL,
  `mileage` int DEFAULT '0',
  `price` decimal(12,2) NOT NULL,
  `cost` decimal(12,2) NOT NULL,
  `status` enum('available','sold','reserved','maintenance') DEFAULT 'available',
  `date_added` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `location` varchar(100) DEFAULT NULL,
  `image_url` varchar(255) DEFAULT NULL,
  `added_by` int DEFAULT NULL,
  `notes` text,
  PRIMARY KEY (`car_id`),
  UNIQUE KEY `vin` (`vin`),
  KEY `model_id` (`model_id`),
  KEY `added_by` (`added_by`),
  KEY `idx_cars_status` (`status`),
  KEY `idx_cars_date_added` (`date_added`),
  CONSTRAINT `cars_ibfk_1` FOREIGN KEY (`model_id`) REFERENCES `car_models` (`model_id`),
  CONSTRAINT `cars_ibfk_2` FOREIGN KEY (`added_by`) REFERENCES `staff` (`staff_id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cars`
--

LOCK TABLES `cars` WRITE;
/*!40000 ALTER TABLE `cars` DISABLE KEYS */;
INSERT INTO `cars` VALUES (1,1,'ABC123456789DEF12','Silver',2023,0,28500.00,26000.00,'available','2025-05-21 11:50:35',NULL,NULL,1,NULL),(2,2,'DEF123456789ABC12','White',2023,0,32000.00,29000.00,'available','2025-05-21 11:50:35',NULL,NULL,1,NULL),(3,3,'GHI123456789JKL12','Black',2023,0,25000.00,22500.00,'available','2025-05-21 11:50:35',NULL,NULL,2,NULL),(4,4,'MNO123456789PQR12','Red',2023,0,55000.00,48000.00,'sold','2025-05-21 11:50:35',NULL,NULL,1,NULL),(5,5,'STU123456789VWX12','Blue',2023,0,68000.00,60000.00,'available','2025-05-21 11:50:35',NULL,NULL,2,NULL);
/*!40000 ALTER TABLE `cars` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `coming_soon_cars`
--

DROP TABLE IF EXISTS `coming_soon_cars`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `coming_soon_cars` (
  `coming_soon_id` int NOT NULL AUTO_INCREMENT,
  `model_id` int NOT NULL,
  `color` varchar(50) DEFAULT NULL,
  `manufacture_year` year NOT NULL,
  `expected_price` decimal(12,2) DEFAULT NULL,
  `expected_arrival_date` date NOT NULL,
  `status` enum('ordered','in_production','in_transit') DEFAULT 'ordered',
  `ordered_by` int DEFAULT NULL,
  `notes` text,
  PRIMARY KEY (`coming_soon_id`),
  KEY `model_id` (`model_id`),
  KEY `ordered_by` (`ordered_by`),
  KEY `idx_coming_soon_arrival` (`expected_arrival_date`),
  KEY `idx_coming_soon_status` (`status`),
  CONSTRAINT `coming_soon_cars_ibfk_1` FOREIGN KEY (`model_id`) REFERENCES `car_models` (`model_id`),
  CONSTRAINT `coming_soon_cars_ibfk_2` FOREIGN KEY (`ordered_by`) REFERENCES `staff` (`staff_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `coming_soon_cars`
--

LOCK TABLES `coming_soon_cars` WRITE;
/*!40000 ALTER TABLE `coming_soon_cars` DISABLE KEYS */;
INSERT INTO `coming_soon_cars` VALUES (1,1,'Blue',2024,29500.00,'2025-06-15','ordered',1,NULL),(2,2,'Black',2024,33500.00,'2025-06-20','in_production',1,NULL),(3,3,'White',2024,26000.00,'2025-07-01','ordered',2,NULL),(4,4,'Yellow',2024,57000.00,'2025-07-10','in_transit',1,NULL);
/*!40000 ALTER TABLE `coming_soon_cars` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `economic_reports`
--

DROP TABLE IF EXISTS `economic_reports`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `economic_reports` (
  `report_id` int NOT NULL AUTO_INCREMENT,
  `report_date` date NOT NULL,
  `report_type` enum('daily','weekly','monthly','quarterly','annual') NOT NULL,
  `start_period` date NOT NULL,
  `end_period` date NOT NULL,
  `total_sales` decimal(12,2) NOT NULL,
  `total_expenses` decimal(12,2) NOT NULL,
  `profit` decimal(12,2) NOT NULL,
  `cars_sold` int NOT NULL,
  `average_sale_price` decimal(12,2) NOT NULL,
  `generated_by` int NOT NULL,
  PRIMARY KEY (`report_id`),
  KEY `generated_by` (`generated_by`),
  CONSTRAINT `economic_reports_ibfk_1` FOREIGN KEY (`generated_by`) REFERENCES `staff` (`staff_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `economic_reports`
--

LOCK TABLES `economic_reports` WRITE;
/*!40000 ALTER TABLE `economic_reports` DISABLE KEYS */;
/*!40000 ALTER TABLE `economic_reports` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `expenses`
--

DROP TABLE IF EXISTS `expenses`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `expenses` (
  `expense_id` int NOT NULL AUTO_INCREMENT,
  `expense_date` date NOT NULL,
  `expense_type` enum('purchase','repair','marketing','utilities','rent','salary','insurance','tax','other') NOT NULL,
  `amount` decimal(10,2) NOT NULL,
  `car_id` int DEFAULT NULL,
  `description` text NOT NULL,
  `recorded_by` int NOT NULL,
  PRIMARY KEY (`expense_id`),
  KEY `car_id` (`car_id`),
  KEY `recorded_by` (`recorded_by`),
  KEY `idx_expenses_date` (`expense_date`),
  CONSTRAINT `expenses_ibfk_1` FOREIGN KEY (`car_id`) REFERENCES `cars` (`car_id`),
  CONSTRAINT `expenses_ibfk_2` FOREIGN KEY (`recorded_by`) REFERENCES `staff` (`staff_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `expenses`
--

LOCK TABLES `expenses` WRITE;
/*!40000 ALTER TABLE `expenses` DISABLE KEYS */;
INSERT INTO `expenses` VALUES (1,'2025-05-21','purchase',145000.00,NULL,'Purchase of 5 new vehicles',1),(2,'2025-05-21','marketing',2500.00,NULL,'Monthly marketing expenses',2),(3,'2025-05-21','utilities',1200.00,NULL,'Monthly utilities',1);
/*!40000 ALTER TABLE `expenses` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `manufacturers`
--

DROP TABLE IF EXISTS `manufacturers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `manufacturers` (
  `manufacturer_id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  `country` varchar(50) DEFAULT NULL,
  `website` varchar(255) DEFAULT NULL,
  `contact_info` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`manufacturer_id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `manufacturers`
--

LOCK TABLES `manufacturers` WRITE;
/*!40000 ALTER TABLE `manufacturers` DISABLE KEYS */;
INSERT INTO `manufacturers` VALUES (1,'Toyota','Japan',NULL,NULL),(2,'Honda','Japan',NULL,NULL),(3,'Ford','USA',NULL,NULL),(4,'BMW','Germany',NULL,NULL),(5,'Mercedes-Benz','Germany',NULL,NULL);
/*!40000 ALTER TABLE `manufacturers` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sales`
--

DROP TABLE IF EXISTS `sales`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sales` (
  `sale_id` int NOT NULL AUTO_INCREMENT,
  `car_id` int NOT NULL,
  `buyer_name` varchar(100) NOT NULL,
  `buyer_contact` varchar(100) NOT NULL,
  `sale_date` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `sale_price` decimal(12,2) NOT NULL,
  `tax_amount` decimal(10,2) NOT NULL,
  `total_amount` decimal(12,2) NOT NULL,
  `payment_method` enum('cash','credit_card','bank_transfer','financing') NOT NULL,
  `payment_status` enum('pending','completed') NOT NULL DEFAULT 'completed',
  `handled_by` int NOT NULL,
  `sale_notes` text,
  PRIMARY KEY (`sale_id`),
  KEY `car_id` (`car_id`),
  KEY `handled_by` (`handled_by`),
  KEY `idx_sales_date` (`sale_date`),
  CONSTRAINT `sales_ibfk_1` FOREIGN KEY (`car_id`) REFERENCES `cars` (`car_id`),
  CONSTRAINT `sales_ibfk_2` FOREIGN KEY (`handled_by`) REFERENCES `staff` (`staff_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sales`
--

LOCK TABLES `sales` WRITE;
/*!40000 ALTER TABLE `sales` DISABLE KEYS */;
INSERT INTO `sales` VALUES (1,4,'John Doe','john.doe@example.com','2025-05-21 11:50:35',55000.00,5500.00,60500.00,'financing','completed',1,NULL);
/*!40000 ALTER TABLE `sales` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `specifications`
--

DROP TABLE IF EXISTS `specifications`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `specifications` (
  `spec_id` int NOT NULL AUTO_INCREMENT,
  `model_id` int NOT NULL,
  `engine_type` varchar(50) DEFAULT NULL,
  `transmission` enum('automatic','manual','semi-automatic') DEFAULT NULL,
  `fuel_type` enum('gasoline','diesel','electric','hybrid','plugin_hybrid') DEFAULT NULL,
  `horsepower` int DEFAULT NULL,
  `seats` int DEFAULT NULL,
  `fuel_economy` decimal(5,2) DEFAULT NULL,
  `features` text,
  PRIMARY KEY (`spec_id`),
  KEY `model_id` (`model_id`),
  CONSTRAINT `specifications_ibfk_1` FOREIGN KEY (`model_id`) REFERENCES `car_models` (`model_id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `specifications`
--

LOCK TABLES `specifications` WRITE;
/*!40000 ALTER TABLE `specifications` DISABLE KEYS */;
INSERT INTO `specifications` VALUES (1,1,'2.5L 4-Cylinder','automatic','gasoline',203,5,32.50,NULL),(2,2,'2.5L 4-Cylinder','automatic','gasoline',203,5,30.20,NULL),(3,3,'2.0L 4-Cylinder','automatic','gasoline',158,5,33.80,NULL),(4,4,'5.0L V8','manual','gasoline',450,4,22.60,NULL),(5,5,'3.0L 6-Cylinder','automatic','gasoline',335,5,25.50,NULL);
/*!40000 ALTER TABLE `specifications` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `staff`
--

DROP TABLE IF EXISTS `staff`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `staff` (
  `staff_id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(50) NOT NULL,
  `password` varchar(255) NOT NULL,
  `full_name` varchar(100) NOT NULL,
  `email` varchar(100) NOT NULL,
  `phone` varchar(20) DEFAULT NULL,
  `role` enum('admin','staff') NOT NULL DEFAULT 'staff',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `last_login` timestamp NULL DEFAULT NULL,
  `status` enum('active','inactive') DEFAULT 'active',
  PRIMARY KEY (`staff_id`),
  UNIQUE KEY `username` (`username`),
  UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `staff`
--

LOCK TABLES `staff` WRITE;
/*!40000 ALTER TABLE `staff` DISABLE KEYS */;
INSERT INTO `staff` VALUES (1,'admin','$2y$10$4NNxCnZtfZD9G3ZCZLQpOuPQ0baR0cxQoNURPcfj3iQlZla5DNyuC','Admin User','admin@carstore.com',NULL,'admin','2025-05-21 11:50:35',NULL,'active'),(2,'staff1','$2y$10$4NNxCnZtfZD9G3ZCZLQpOuPQ0baR0cxQoNURPcfj3iQlZla5DNyuC','Staff One','staff1@carstore.com',NULL,'staff','2025-05-21 11:50:35',NULL,'active');
/*!40000 ALTER TABLE `staff` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Temporary view structure for view `v_car_status_summary`
--

DROP TABLE IF EXISTS `v_car_status_summary`;
/*!50001 DROP VIEW IF EXISTS `v_car_status_summary`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `v_car_status_summary` AS SELECT 
 1 AS `available_cars`,
 1 AS `sold_cars`,
 1 AS `total_cars`*/;
SET character_set_client = @saved_cs_client;

--
-- Temporary view structure for view `v_coming_soon_summary`
--

DROP TABLE IF EXISTS `v_coming_soon_summary`;
/*!50001 DROP VIEW IF EXISTS `v_coming_soon_summary`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `v_coming_soon_summary` AS SELECT 
 1 AS `total_coming_soon`,
 1 AS `ordered`,
 1 AS `in_production`,
 1 AS `in_transit`*/;
SET character_set_client = @saved_cs_client;

--
-- Temporary view structure for view `v_economic_summary`
--

DROP TABLE IF EXISTS `v_economic_summary`;
/*!50001 DROP VIEW IF EXISTS `v_economic_summary`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `v_economic_summary` AS SELECT 
 1 AS `total_revenue`,
 1 AS `estimated_profit`*/;
SET character_set_client = @saved_cs_client;

--
-- Temporary view structure for view `v_new_cars_summary`
--

DROP TABLE IF EXISTS `v_new_cars_summary`;
/*!50001 DROP VIEW IF EXISTS `v_new_cars_summary`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `v_new_cars_summary` AS SELECT 
 1 AS `total_new_cars`*/;
SET character_set_client = @saved_cs_client;

--
-- Final view structure for view `v_car_status_summary`
--

/*!50001 DROP VIEW IF EXISTS `v_car_status_summary`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_0900_ai_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `v_car_status_summary` AS select count((case when (`cars`.`status` = 'available') then 1 end)) AS `available_cars`,count((case when (`cars`.`status` = 'sold') then 1 end)) AS `sold_cars`,count(0) AS `total_cars` from `cars` */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `v_coming_soon_summary`
--

/*!50001 DROP VIEW IF EXISTS `v_coming_soon_summary`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_0900_ai_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `v_coming_soon_summary` AS select count(0) AS `total_coming_soon`,count((case when (`coming_soon_cars`.`status` = 'ordered') then 1 end)) AS `ordered`,count((case when (`coming_soon_cars`.`status` = 'in_production') then 1 end)) AS `in_production`,count((case when (`coming_soon_cars`.`status` = 'in_transit') then 1 end)) AS `in_transit` from `coming_soon_cars` */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `v_economic_summary`
--

/*!50001 DROP VIEW IF EXISTS `v_economic_summary`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_0900_ai_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `v_economic_summary` AS select coalesce(sum(`s`.`total_amount`),0) AS `total_revenue`,coalesce(sum((`s`.`total_amount` - `c`.`cost`)),0) AS `estimated_profit` from (`sales` `s` join `cars` `c` on((`s`.`car_id` = `c`.`car_id`))) where (`s`.`sale_date` >= (now() - interval 30 day)) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `v_new_cars_summary`
--

/*!50001 DROP VIEW IF EXISTS `v_new_cars_summary`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_0900_ai_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `v_new_cars_summary` AS select count(0) AS `total_new_cars` from `cars` where (`cars`.`date_added` >= (now() - interval 30 day)) */;
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

-- Dump completed on 2025-05-21 21:55:16
