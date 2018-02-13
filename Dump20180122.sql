-- MySQL dump 10.13  Distrib 5.7.17, for Win64 (x86_64)
--
-- Host: localhost    Database: varsity
-- ------------------------------------------------------
-- Server version	5.7.19-log

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
-- Table structure for table `d_calendar`
--

DROP TABLE IF EXISTS `d_calendar`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `d_calendar` (
  `DATE_KEY` int(11) NOT NULL,
  `DATE` date DEFAULT NULL,
  `DAY_NAME` varchar(10) DEFAULT NULL COMMENT 'Contains name of the day, Sunday, Monday ',
  `YEAR` char(4) DEFAULT NULL,
  `MONTH` varchar(2) DEFAULT NULL COMMENT 'Number of the Month 1 to 12',
  `MONTH_NAME` varchar(10) DEFAULT NULL,
  `QUARTER` char(2) DEFAULT NULL COMMENT '1,2,3,4',
  `WEEK` int(11) DEFAULT NULL,
  PRIMARY KEY (`DATE_KEY`),
  UNIQUE KEY `DATE_KEY` (`DATE_KEY`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `d_campus`
--

DROP TABLE IF EXISTS `d_campus`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `d_campus` (
  `CAMPUS_ID` int(11) NOT NULL,
  `CAMPUS_NAME` varchar(200) NOT NULL,
  PRIMARY KEY (`CAMPUS_ID`),
  UNIQUE KEY `CAMPUS_ID` (`CAMPUS_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `d_exam`
--

DROP TABLE IF EXISTS `d_exam`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `d_exam` (
  `EXAM_ID` int(11) NOT NULL,
  `EXAM_MODEL` varchar(100) DEFAULT NULL,
  `EXAM_TYPE` varchar(100) DEFAULT NULL,
  `EXAM_NAME` varchar(1000) DEFAULT NULL,
  `EXAM_DATE` date DEFAULT NULL,
  PRIMARY KEY (`EXAM_ID`),
  UNIQUE KEY `EXAM_ID` (`EXAM_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `d_section`
--

DROP TABLE IF EXISTS `d_section`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `d_section` (
  `SECTION_ID` int(11) NOT NULL,
  `SECTION_NAME` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`SECTION_ID`),
  UNIQUE KEY `SECTION_ID` (`SECTION_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `d_student`
--

DROP TABLE IF EXISTS `d_student`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `d_student` (
  `STUDENT_ID` int(11) NOT NULL,
  `STUDENT_FULL_NAME` varchar(200) NOT NULL,
  `STUDENT_SHORT_NAME` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`STUDENT_ID`),
  UNIQUE KEY `STUDENT_ID` (`STUDENT_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `d_subject`
--

DROP TABLE IF EXISTS `d_subject`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `d_subject` (
  `SUBJECT_ID` int(11) NOT NULL,
  `SUBJECT_NAME` varchar(30) DEFAULT NULL,
  PRIMARY KEY (`SUBJECT_ID`),
  UNIQUE KEY `SUBJECT_ID` (`SUBJECT_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `exam_result_stg`
--

DROP TABLE IF EXISTS `exam_result_stg`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `exam_result_stg` (
  `OMR_ID` int(11) DEFAULT NULL,
  `STUDENT_NAME` varchar(50) DEFAULT NULL,
  `MAT_120` int(11) DEFAULT NULL,
  `MATHS_RANK` int(11) DEFAULT NULL,
  `MATHS_PERCENTAGE` decimal(28,16) DEFAULT NULL,
  `PHY_120` int(11) DEFAULT NULL,
  `PHYSICS_RANK` int(11) DEFAULT NULL,
  `PHYSICS_PERCENTAGE` decimal(28,16) DEFAULT NULL,
  `CHE_120` int(11) DEFAULT NULL,
  `CHEMISTRY_RANK` int(11) DEFAULT NULL,
  `CHEMISTRY_PERCENTAGE` decimal(28,15) DEFAULT NULL,
  `Total_360` int(11) DEFAULT NULL,
  `STATE_Rank` int(11) DEFAULT NULL,
  `AIR_Rank` int(11) DEFAULT NULL,
  `TOTAL_PERCENTAGE` decimal(28,15) DEFAULT NULL,
  `CAMPUS` varchar(50) DEFAULT NULL,
  `DATE` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `f_exam_result`
--

DROP TABLE IF EXISTS `f_exam_result`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `f_exam_result` (
  `EXAM_RESULT_ID` int(11) NOT NULL AUTO_INCREMENT,
  `MAX_MATHS_MAKRS` int(100) DEFAULT NULL,
  `MAX_PHYSICS_MARKS` int(100) DEFAULT NULL,
  `MAX_CHEMISTRY_MARKS` int(100) DEFAULT NULL,
  `PHYSICS_MARKS` float(5,2) unsigned zerofill DEFAULT NULL,
  `CHEMISTRY_MARKS` float(5,2) unsigned zerofill DEFAULT NULL,
  `MATHS_MARKS` float(5,2) unsigned zerofill DEFAULT NULL,
  `MATHS_RANK` int(11) DEFAULT NULL,
  `ALL_INDIA_RANK` int(11) DEFAULT NULL,
  `PHYSICS_RANK` int(11) DEFAULT NULL,
  `CHEMISTRY_RANK` int(11) DEFAULT NULL,
  `STATE_RANK` int(11) DEFAULT NULL,
  `MATHS_PERCENTAGE` char(60) DEFAULT NULL,
  `PHYSICS_PERCENTAGE` char(60) DEFAULT NULL,
  `CHEMISTRY_PERCENTAGE` char(60) DEFAULT NULL,
  `TOTAL_PERCENTAGE` char(60) DEFAULT NULL,
  `TOTAL_MARKS` float(5,2) unsigned zerofill DEFAULT NULL,
  `CAMPUS_ID_FK` int(11) NOT NULL,
  `STUDENT_ID_FK` int(11) NOT NULL,
  `DATE_KEY_FK` int(11) NOT NULL,
  `SECTION_ID_FK` int(11) NOT NULL,
  `EXAM_ID_FK` int(11) NOT NULL,
  PRIMARY KEY (`EXAM_RESULT_ID`),
  KEY `Relationship3` (`CAMPUS_ID_FK`),
  KEY `Relationship4` (`STUDENT_ID_FK`),
  KEY `Relationship5` (`DATE_KEY_FK`),
  KEY `Relationship6` (`SECTION_ID_FK`),
  KEY `Relationship7` (`EXAM_ID_FK`),
  CONSTRAINT `Relationship3` FOREIGN KEY (`CAMPUS_ID_FK`) REFERENCES `d_campus` (`CAMPUS_ID`),
  CONSTRAINT `Relationship4` FOREIGN KEY (`STUDENT_ID_FK`) REFERENCES `d_student` (`STUDENT_ID`),
  CONSTRAINT `Relationship5` FOREIGN KEY (`DATE_KEY_FK`) REFERENCES `d_calendar` (`DATE_KEY`),
  CONSTRAINT `Relationship6` FOREIGN KEY (`SECTION_ID_FK`) REFERENCES `d_section` (`SECTION_ID`),
  CONSTRAINT `Relationship7` FOREIGN KEY (`EXAM_ID_FK`) REFERENCES `d_exam` (`EXAM_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=212 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `f_ques_ans`
--

DROP TABLE IF EXISTS `f_ques_ans`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `f_ques_ans` (
  `QUES_ANS_ID` int(11) NOT NULL AUTO_INCREMENT,
  `QUESTION_NUM` char(5) DEFAULT NULL,
  `ANSWER_RESULT` char(5) DEFAULT NULL,
  `SUBJECT_ID_FK` int(11) NOT NULL,
  `EXAM_ID_FK` int(11) NOT NULL,
  `STUDENT_ID_FK` int(11) NOT NULL,
  `CAMPUS_ID_FK` int(11) NOT NULL,
  `SECTION_ID_FK` int(11) NOT NULL,
  PRIMARY KEY (`QUES_ANS_ID`),
  KEY `Relationship8` (`SUBJECT_ID_FK`),
  KEY `Relationship9` (`EXAM_ID_FK`),
  KEY `Relationship10` (`STUDENT_ID_FK`),
  KEY `Relationship11` (`CAMPUS_ID_FK`),
  KEY `Relationship12` (`SECTION_ID_FK`),
  CONSTRAINT `Relationship10` FOREIGN KEY (`STUDENT_ID_FK`) REFERENCES `d_student` (`STUDENT_ID`),
  CONSTRAINT `Relationship11` FOREIGN KEY (`CAMPUS_ID_FK`) REFERENCES `d_campus` (`CAMPUS_ID`),
  CONSTRAINT `Relationship12` FOREIGN KEY (`SECTION_ID_FK`) REFERENCES `d_section` (`SECTION_ID`),
  CONSTRAINT `Relationship8` FOREIGN KEY (`SUBJECT_ID_FK`) REFERENCES `d_subject` (`SUBJECT_ID`),
  CONSTRAINT `Relationship9` FOREIGN KEY (`EXAM_ID_FK`) REFERENCES `d_exam` (`EXAM_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2018-01-22 10:38:36
