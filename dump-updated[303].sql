CREATE DATABASE  IF NOT EXISTS `tsm_v2` /*!40100 DEFAULT CHARACTER SET latin1 */;
USE `tsm_v2`;
-- MySQL dump 10.13  Distrib 5.7.17, for Win64 (x86_64)
--
-- Host: 192.168.32.111    Database: tsm_v2
-- ------------------------------------------------------
-- Server version	5.6.22

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
-- Table structure for table `osi_attachments`
--

DROP TABLE IF EXISTS `osi_attachments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `osi_attachments` (
  `attachment_id` int(11) NOT NULL AUTO_INCREMENT,
  `original_file_name` varchar(100) DEFAULT NULL,
  `duplicate_file_name` varchar(100) DEFAULT NULL,
  `file_type` varchar(100) DEFAULT NULL,
  `file_content` varchar(100) DEFAULT NULL,
  `attachment_type` varchar(50) DEFAULT NULL,
  `object_type` varchar(50) DEFAULT NULL,
  `object_id` int(11) DEFAULT NULL,
  `created_by` int(11) DEFAULT NULL,
  `creation_date` timestamp NULL DEFAULT NULL,
  `last_updated_by` int(11) DEFAULT NULL,
  `last_update_date` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`attachment_id`),
  KEY `IX_Relationship24` (`attachment_id`)
) ENGINE=InnoDB AUTO_INCREMENT=82 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `osi_calendar`
--

DROP TABLE IF EXISTS `osi_calendar`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `osi_calendar` (
  `CALENDAR_ID` int(11) NOT NULL AUTO_INCREMENT,
  `ORG_ID` int(11) NOT NULL COMMENT '\n\nBelongs to which organization',
  `PERIOD_NAME` varchar(45) NOT NULL COMMENT '\n\nUnique Name',
  `PERIOD_NUM` int(11) NOT NULL COMMENT '\n\n',
  `PERIOD_START_DATE` datetime NOT NULL,
  `PERIOD_END_DATE` datetime NOT NULL,
  `YEAR_START_DATE` datetime NOT NULL,
  `QUARTER_START_DATE` varchar(45) NOT NULL,
  `PERIOD_TYPE` varchar(45) DEFAULT NULL COMMENT 'WEEKLYMONTHLYQUARTERLY',
  `PERIOD_YEAR` int(11) NOT NULL COMMENT '\n\n\n\n2017, 2018',
  `QUARTER_NUM` int(11) NOT NULL COMMENT '\n\n\n1, 2, 3, 4',
  `LONG_PERIOD_NAME` varchar(512) NOT NULL,
  `CREATED_BY` int(11) NOT NULL,
  `CREATION_DATE` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `LAST_UPDATED_BY` int(11) NOT NULL,
  `LAST_UPDATE_DATE` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`PERIOD_NAME`,`PERIOD_NUM`),
  UNIQUE KEY `OSI_CALENDAR_U1` (`CALENDAR_ID`),
  KEY `OSI_CALENDAR_N1` (`PERIOD_NAME`,`PERIOD_NUM`),
  KEY `OSI_CALENDAR_N2` (`PERIOD_NUM`),
  KEY `OSI_CALENDAR_N3` (`PERIOD_START_DATE`,`PERIOD_END_DATE`),
  KEY `OSI_CALENDAR_F1_idx` (`ORG_ID`),
  CONSTRAINT `OSI_CALENDAR_F1` FOREIGN KEY (`ORG_ID`) REFERENCES `osi_organizations` (`org_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=201854 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;


--
-- Table structure for table `osi_countries`
--

DROP TABLE IF EXISTS `osi_countries`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `osi_countries` (
  `country_id` int(11) NOT NULL AUTO_INCREMENT,
  `country_name` varchar(500) NOT NULL,
  `country_code` varchar(45) NOT NULL,
  `region_id` int(11) DEFAULT NULL,
  `created_by` int(11) NOT NULL,
  `creation_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `last_updated_by` int(11) NOT NULL,
  `last_update_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `country_calling_code` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`country_name`),
  UNIQUE KEY `osi_countries_u1` (`country_id`),
  UNIQUE KEY `osi_countries_u2` (`country_code`),
  KEY `osi_countries_n1` (`region_id`)
) ENGINE=InnoDB AUTO_INCREMENT=455 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `osi_currencies`
--

DROP TABLE IF EXISTS `osi_currencies`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `osi_currencies` (
  `currency_id` int(11) NOT NULL AUTO_INCREMENT,
  `currency_name` varchar(100) NOT NULL,
  `currency_code` varchar(20) NOT NULL,
  `country` int(11) NOT NULL,
  `active` int(11) DEFAULT '1',
  `created_by` int(11) NOT NULL,
  `creation_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `last_updated_by` int(11) NOT NULL,
  `last_update_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`currency_code`),
  UNIQUE KEY `osi_currencies_u1` (`currency_id`),
  UNIQUE KEY `osi_currencies_u2` (`currency_name`),
  KEY `osi_currencies_n1` (`country`),
  CONSTRAINT `osi_currencies_f1` FOREIGN KEY (`country`) REFERENCES `osi_countries` (`country_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=34 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `osi_customer`
--

DROP TABLE IF EXISTS `osi_customer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `osi_customer` (
  `CUSTOMER_ID` int(11) NOT NULL AUTO_INCREMENT,
  `CUSTOMER_NAME` varchar(100) NOT NULL COMMENT '\n\nStock Ticker\nUpper Case\nNo Spaces',
  `CUSTOMER_LONG_NAME` varchar(1024) NOT NULL COMMENT '\n\n\nCamel Case',
  `CUSTOMER_CONTACT_ID` int(11) DEFAULT NULL COMMENT '\n\n\nConnected to OSI_Contacts Table',
  `ACCOUNT_MANAGER_ID` int(11) DEFAULT NULL COMMENT '\n\n\nConnected to OSI_Employee Table',
  `ACCOUNT_SALESREP_ID` int(11) DEFAULT NULL COMMENT '\n\nConnected to OSI_Employee Table',
  `CUST_TIMEZONE` varchar(45) DEFAULT NULL,
  `CUST_REGION` varchar(45) DEFAULT NULL,
  `CUST_LOGO` int(11) DEFAULT NULL,
  `CUST_URL` varchar(45) DEFAULT NULL,
  `CREATED_BY` int(11) NOT NULL,
  `CREATION_DATE` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `UPDATED_BY` int(11) DEFAULT NULL,
  `UPDATE_DATE` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `CUSTOMER_SOURCED_FROM` varchar(45) NOT NULL COMMENT '\n\n\nDELIVERY\nINSIDE_SALES\nSALES\nINTERNAL_EMPLOYEES',
  PRIMARY KEY (`CUSTOMER_ID`),
  UNIQUE KEY `OSI_CUSTOMER_U1` (`CUSTOMER_ID`),
  UNIQUE KEY `CUSTOMER_NAME` (`CUSTOMER_NAME`),
  KEY `OSI_CUSTOMER_N1` (`CUSTOMER_CONTACT_ID`),
  KEY `OSI_CUSTOMER_N2` (`ACCOUNT_MANAGER_ID`),
  KEY `OSI_CUSTOMER_N3` (`ACCOUNT_SALESREP_ID`),
  KEY `osicustomer_fk1` (`CUST_LOGO`),
  CONSTRAINT `osicustomer_fk1` FOREIGN KEY (`CUST_LOGO`) REFERENCES `osi_attachments` (`attachment_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1010 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;


--
-- Table structure for table `osi_organizations`
--

DROP TABLE IF EXISTS `osi_organizations`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `osi_organizations` (
  `ORG_ID` int(11) NOT NULL AUTO_INCREMENT,
  `ORG_NAME` varchar(100) NOT NULL,
  `ORG_SHORT_NAME` varchar(20) NOT NULL,
  `WEBSITE` varchar(20) DEFAULT NULL,
  `FAX_NUMBER` varchar(100) DEFAULT NULL,
  `PHONE_NUMBER` varchar(20) DEFAULT NULL,
  `PARENT_ORG_ID` int(11) DEFAULT NULL,
  `EMAIL_ADDRRESS` varchar(50) DEFAULT NULL,
  `CONTACT_PERSON_ID` int(11) DEFAULT NULL,
  `BASE_CURRENCY_ID` int(11) DEFAULT NULL,
  `REPORTING_CURRENCY_ID` int(11) DEFAULT NULL,
  `ACTIVE` int(11) NOT NULL DEFAULT '1',
  `FAX_CODE` varchar(45) DEFAULT NULL,
  `OVERHEAD_PCT` int(11) NOT NULL DEFAULT '40' COMMENT '\n\n\n40',
  `TOTAL_HRS_PER_YEAR` int(11) NOT NULL DEFAULT '2088',
  `COST_CALC` varchar(45) NOT NULL DEFAULT 'FLAT' COMMENT '\n\nFLAT\nCTCOH',
  `INTER_ORG_EMP_COST_OVERHEAD_PCT` int(11) NOT NULL DEFAULT '30',
  `COUNTRY_CODE` varchar(45) DEFAULT NULL,
  `CREATED_BY` int(11) NOT NULL DEFAULT '1',
  `CREATION_DATE` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `LAST_UPDATED_BY` int(11) NOT NULL,
  `LAST_UPDATE_DATE` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`ORG_NAME`),
  UNIQUE KEY `OSI_ORGA IZATIONS_U1` (`ORG_ID`),
  UNIQUE KEY `OSI_ORGA IZATIONS_U2` (`ORG_SHORT_NAME`),
  KEY `OSI_ORGA IZATIONS_N1` (`BASE_CURRENCY_ID`),
  KEY `OSI_ORGA IZATIONS_N2` (`REPORTING_CURRENCY_ID`),
  KEY `OSI_ORGA IZATIONS_N3` (`PARENT_ORG_ID`),
  CONSTRAINT `OSI_ORGA IZATIONS_F1` FOREIGN KEY (`BASE_CURRENCY_ID`) REFERENCES `osi_currencies` (`currency_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `OSI_ORGA IZATIONS_F2` FOREIGN KEY (`REPORTING_CURRENCY_ID`) REFERENCES `osi_currencies` (`currency_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `OSI_ORGA IZATIONS_F3` FOREIGN KEY (`PARENT_ORG_ID`) REFERENCES `osi_organizations` (`ORG_ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;


--
-- Table structure for table `osi_projects`
--

DROP TABLE IF EXISTS `osi_projects`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `osi_projects` (
  `PROJECT_ID` int(11) NOT NULL AUTO_INCREMENT,
  `ORG_ID` int(11) NOT NULL,
  `PROJECT_NAME` varchar(500) NOT NULL,
  `PROJECT_LONG_NAME` varchar(1024) DEFAULT NULL,
  `PROJECT_DESCRIPTION` varchar(2000) DEFAULT NULL,
  `CUSTOMER_ID` int(11) DEFAULT NULL,
  `PROJECT_STATUS` int(11) NOT NULL,
  `PROJECT_TYPE` int(11) NOT NULL,
  `PROJECT_START_DATE` datetime DEFAULT NULL,
  `PROJECT_COMPLETION_DATE` datetime DEFAULT NULL,
  `PROJECT_MANAGER_ID` int(11) DEFAULT NULL,
  `SKILL_ID` varchar(1024) NOT NULL COMMENT 'comma separate skill ids',
  `PAYMENT_TERMS_ID` int(11) NOT NULL,
  `LATE_FEE_TERMS_ID` int(11) NOT NULL,
  `TAX_RATE_ID` int(11) NOT NULL,
  `INVOICE_TEMPLATE_TYPE` int(11) DEFAULT NULL COMMENT 'Invoice Template Type i.e Organisation type, Customer type',
  `INVOICE_TEMPLATE_ID` int(11) DEFAULT NULL,
  `CUSTOMER_INVOICE_NOTES` varchar(2048) DEFAULT NULL,
  `REVIEW_NOTES` varchar(2048) DEFAULT NULL COMMENT 'project review notes for PMO',
  `IS_TSAUTO_APPROVE` int(11) NOT NULL DEFAULT '0' COMMENT 'Time Sheet Auto Approval ;1-Auto Approval, 0-Manual Approval',
  `IS_BILLABLE` int(11) NOT NULL DEFAULT '1',
  `IS_EXPENSE_BILLABLE` int(11) NOT NULL DEFAULT '0' COMMENT '0-Can not be used for Billable expenses, 1- can be used for Billable expenses',
  `IS_INTERNAL` int(11) NOT NULL DEFAULT '0',
  `IS_VISIBLE_TO_EVERYONE` int(11) NOT NULL DEFAULT '0',
  `PROJECT_CURRENCY` int(11) NOT NULL,
  `CREATED_BY` int(11) NOT NULL,
  `CREATION_DATE` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `LAST_UPDATED_BY` int(11) DEFAULT NULL,
  `LAST_UPDATE_DATE` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`PROJECT_ID`),
  UNIQUE KEY `OSI_PROJECTS_U1` (`PROJECT_ID`),
  UNIQUE KEY `PROJECT_NAME` (`PROJECT_NAME`),
  KEY `OSI_PROJECTS_N1` (`CUSTOMER_ID`),
  KEY `OSI_PROJECTS_N2` (`PROJECT_MANAGER_ID`),
  KEY `OSI_PROJECTS_N3` (`PROJECT_CURRENCY`),
  KEY `OSI_PROJECTS_F3_idx` (`ORG_ID`),
  KEY `PROJECT_STATUS` (`PROJECT_STATUS`),
  KEY `PROJECT_TYPE` (`PROJECT_TYPE`),
  KEY `PROJECT_START_DATE` (`PROJECT_START_DATE`),
  KEY `PROJECT_COMPLETION_DATE` (`PROJECT_COMPLETION_DATE`),
  CONSTRAINT `OSI_PROJECTS_F1` FOREIGN KEY (`CUSTOMER_ID`) REFERENCES `osi_customer` (`CUSTOMER_ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `OSI_PROJECTS_F2` FOREIGN KEY (`PROJECT_CURRENCY`) REFERENCES `osi_currencies` (`currency_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `OSI_PROJECTS_F3` FOREIGN KEY (`ORG_ID`) REFERENCES `osi_organizations` (`org_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=1401 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;


--
-- Table structure for table `osi_sow_activities`
--

DROP TABLE IF EXISTS `osi_sow_activities`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `osi_sow_activities` (
  `SOW_ACTIVITY_ID` int(11) NOT NULL AUTO_INCREMENT,
  `SOW_ID` int(11) NOT NULL,
  `PROJECT_ID` int(11) NOT NULL,
  `ACTIVITY_NAME` varchar(1024) NOT NULL,
  `JOB_CODE_ID` int(11) NOT NULL,
  `HOURS` int(11) NOT NULL,
  `CREATED_BY` int(11) NOT NULL,
  `CREATION_DATE` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `LAST_UPDATED_BY` int(11) DEFAULT NULL,
  `LAST_UPDATE_DATE` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`SOW_ACTIVITY_ID`),
  KEY `osisowactivities_fk1` (`SOW_ID`),
  KEY `osisowactivities_fk2` (`JOB_CODE_ID`),
  KEY `osisowactivities_fk3` (`PROJECT_ID`),
  CONSTRAINT `osisowactivities_fk1` FOREIGN KEY (`SOW_ID`) REFERENCES `osi_sow` (`SOW_ID`),
  CONSTRAINT `osisowactivities_fk2` FOREIGN KEY (`JOB_CODE_ID`) REFERENCES `osi_job_codes` (`job_code_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `osisowactivities_fk3` FOREIGN KEY (`PROJECT_ID`) REFERENCES `osi_projects` (`PROJECT_ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=83 DEFAULT CHARSET=latin1 COMMENT='SOW Activities';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `osi_sow_milestones`
--

DROP TABLE IF EXISTS `osi_sow_milestones`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `osi_sow_milestones` (
  `MILESTONE_ID` int(11) NOT NULL AUTO_INCREMENT,
  `SOW_ID` int(11) NOT NULL,
  `MILESTONE_TYPE_ID` int(11) NOT NULL,
  `MILESTONE_NAME` varchar(45) NOT NULL,
  `MILESTONE_AMOUNT` decimal(20,2) NOT NULL,
  `EXCLUDE_PTO_HRS` int(11) DEFAULT '0',
  `DATE_FOR_INVOICING` varchar(45) NOT NULL,
  `INVOICE_DATE` varchar(45) DEFAULT NULL,
  `IS_INVOICED` int(11) DEFAULT '0' COMMENT '0-invoice not generated, 1-invoice generated',
  `INVOICE_ID` int(11) DEFAULT NULL,
  `IS_MILESTONE_COMPLETED` int(11) NOT NULL DEFAULT '0' COMMENT '0--not completed and 1 - completed',
  `CREATED_BY` int(11) NOT NULL,
  `CREATION_DATE` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `LAST_UPDATED_BY` int(11) DEFAULT NULL,
  `LAST_UPDATE_DATE` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`MILESTONE_ID`),
  UNIQUE KEY `OSI_SOW_MILESTONES_U1` (`MILESTONE_ID`),
  KEY `OSI_SOW_MILESTONES_N1` (`SOW_ID`),
  KEY `OSI_SOW_MILESTONES_N2` (`MILESTONE_TYPE_ID`),
  KEY `osisowmilestones_fk3` (`INVOICE_ID`),
  KEY `IS_INVOICED` (`IS_INVOICED`),
  KEY `DATE_FOR_INVOICING` (`DATE_FOR_INVOICING`),
  CONSTRAINT `OSI_SOW_MILESTONES_F1` FOREIGN KEY (`SOW_ID`) REFERENCES `osi_sow` (`SOW_ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `OSI_SOW_MILESTONES_F2` FOREIGN KEY (`MILESTONE_TYPE_ID`) REFERENCES `osi_milestone_types` (`MILESTONE_TYPE_ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `osisowmilestones_fk3` FOREIGN KEY (`INVOICE_ID`) REFERENCES `osi_invoices` (`INVOICE_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=61 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;


--
-- Table structure for table `osi_tasks`
--

DROP TABLE IF EXISTS `osi_tasks`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `osi_tasks` (
  `TASK_ID` int(11) NOT NULL AUTO_INCREMENT,
  `PROJECT_ID` int(11) NOT NULL,
  `TASK_NAME` varchar(45) NOT NULL,
  `TASK_TYPE` int(11) NOT NULL,
  `TASK_START_DATE` datetime NOT NULL,
  `TASK_END_DATE` datetime NOT NULL,
  `ESTIMATED_HOURS` int(11) DEFAULT NULL,
  `ACTUAL_HOURS` int(11) DEFAULT NULL,
  `PREDECESSOR_TASK` int(11) DEFAULT NULL,
  `IS_TASK_COMPLETED` int(11) DEFAULT NULL COMMENT '0-not completed, 1-completed',
  `TASK_COMPLETE_PCT` int(11) DEFAULT NULL,
  `PARENT_TASK` int(11) DEFAULT NULL,
  `SOW_ACTIVITY_ID` int(11) DEFAULT NULL,
  `MILESTONE_ID` int(11) DEFAULT NULL,
  `CREATED_BY` int(11) NOT NULL,
  `CREATION_DATE` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `LAST_UPDATED_BY` int(11) DEFAULT NULL,
  `LAST_UPDATE_DATE` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`TASK_ID`),
  UNIQUE KEY `OSI_TASKS_U1` (`TASK_ID`),
  KEY `OSI_TASKS_N1` (`PROJECT_ID`),
  KEY `OSI_TASKS_N2` (`PREDECESSOR_TASK`),
  KEY `OSI_TASKS_N3` (`MILESTONE_ID`),
  KEY `ositasks_fk4` (`SOW_ACTIVITY_ID`),
  KEY `ositasks_fk5` (`PARENT_TASK`),
  CONSTRAINT `OSI_TASKS_F1` FOREIGN KEY (`PROJECT_ID`) REFERENCES `osi_projects` (`PROJECT_ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `OSI_TASKS_F2` FOREIGN KEY (`PREDECESSOR_TASK`) REFERENCES `osi_tasks` (`TASK_ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `OSI_TASKS_F3` FOREIGN KEY (`MILESTONE_ID`) REFERENCES `osi_sow_milestones` (`MILESTONE_ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `ositasks_fk4` FOREIGN KEY (`SOW_ACTIVITY_ID`) REFERENCES `osi_sow_activities` (`SOW_ACTIVITY_ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `ositasks_fk5` FOREIGN KEY (`PARENT_TASK`) REFERENCES `osi_tasks` (`TASK_ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=117 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `osi_timesheet_entry`
--

DROP TABLE IF EXISTS `osi_timesheet_entry`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `osi_timesheet_entry` (
  `TIME_SHEET_ID` int(11) NOT NULL AUTO_INCREMENT COMMENT 'TIME_SHEET_ID	int(11)	AUTO INCREMENT. Unique ID for the time entry.',
  `ORG_ID` int(11) NOT NULL COMMENT 'Organization Id',
  `EMPLOYEE_ID` int(11) NOT NULL COMMENT 'Employee Id',
  `CALENDAR_ID` int(11) NOT NULL COMMENT 'Calendar Id',
  `SPENT_DATE` datetime NOT NULL COMMENT 'Date of the time entry.',
  `HOURS` double NOT NULL DEFAULT '0' COMMENT 'Number of (decimal time) hours tracked in this time entry.',
  `NOTES` varchar(2048) DEFAULT NULL COMMENT 'Notes attached to the time entry.',
  `PROJECT_ID` int(11) NOT NULL COMMENT 'Project Id',
  `TASK_ID` int(11) NOT NULL COMMENT 'Task Id',
  `UNIT_COST_ORG` decimal(10,2) DEFAULT NULL COMMENT 'The cost rate for the time entry.',
  `UNIT_COST_SOW_CUR` decimal(10,2) DEFAULT NULL COMMENT 'The cost currency for the time entry.',
  `STATUS` varchar(100) DEFAULT 'DRAFT' COMMENT 'DRAFT,SUBMITTED, APPROVED, REJECTED, LOCKED',
  `COMMENTS` varchar(2048) DEFAULT NULL COMMENT 'Approve or Reject or Locked Status reason',
  `IS_INVOICED` int(11) NOT NULL DEFAULT '0' COMMENT 'Whether or not the time entry has been marked as invoiced.',
  `INVOICE_ID` varchar(100) DEFAULT NULL COMMENT 'Once the time entry has been invoiced, this field will include the associated invoice’s id and number.',
  `CREATED_BY` int(11) NOT NULL COMMENT 'Created  user id',
  `CREATION_DATE` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Date and time the time entry was created.',
  `LAST_UPDATED_BY` int(11) DEFAULT NULL COMMENT 'Last updated user id',
  `LAST_UPDATE_DATE` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT 'Date and time the time entry was last updated.',
  PRIMARY KEY (`TIME_SHEET_ID`),
  UNIQUE KEY `OSI_TIMESHEET_ENTRY_U1` (`TIME_SHEET_ID`),
  UNIQUE KEY `TIME_SHEET_ID` (`TIME_SHEET_ID`),
  KEY `OSI_TIMESHEET_ENTRY_N1` (`EMPLOYEE_ID`,`ORG_ID`),
  KEY `OSI_TIMESHEET_ENTRY_N2` (`SPENT_DATE`),
  KEY `OSI_TIMESHEET_ENTRY_N3` (`PROJECT_ID`,`TASK_ID`),
  KEY `OSI_TIMESHEET_ENTRY_N4` (`TASK_ID`),
  KEY `OSI_TIMESHEET_ENTRY_N5` (`LAST_UPDATED_BY`),
  KEY `OSI_TIMESHEET_ENTRY_N6` (`CREATED_BY`),
  KEY `OSI_TIMESHEET_ENTRY_F3_idx` (`ORG_ID`),
  KEY `OSI_TIMESHEET_ENTRY_F4` (`CALENDAR_ID`),
  KEY `STATUS` (`STATUS`),
  CONSTRAINT `OSI_TIMESHEET_ENTRY_F1` FOREIGN KEY (`PROJECT_ID`) REFERENCES `osi_projects` (`PROJECT_ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `OSI_TIMESHEET_ENTRY_F2` FOREIGN KEY (`TASK_ID`) REFERENCES `osi_tasks` (`TASK_ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `OSI_TIMESHEET_ENTRY_F3` FOREIGN KEY (`ORG_ID`) REFERENCES `osi_organizations` (`org_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `OSI_TIMESHEET_ENTRY_F4` FOREIGN KEY (`CALENDAR_ID`) REFERENCES `osi_calendar` (`CALENDAR_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=1556 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `osi_timesheet_entry_history`
--

DROP TABLE IF EXISTS `osi_timesheet_entry_history`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `osi_timesheet_entry_history` (
  `TIME_SHEET_HISTORY_ID` int(11) NOT NULL AUTO_INCREMENT,
  `TIME_SHEET_ID` int(11) NOT NULL COMMENT 'TIME_SHEET_ID	int(11)	AUTO INCREMENT. Unique ID for the time entry.',
  `ORG_ID` int(11) NOT NULL COMMENT 'Organization Id',
  `EMPLOYEE_ID` int(11) NOT NULL COMMENT 'Employee Id',
  `CALENDAR_ID` int(11) NOT NULL COMMENT 'Calendar Id',
  `SPENT_DATE` datetime NOT NULL COMMENT 'Date of the time entry.',
  `HOURS` double NOT NULL COMMENT 'Number of (decimal time) hours tracked in this time entry.',
  `NOTES` varchar(2048) DEFAULT NULL COMMENT 'Notes attached to the time entry.',
  `PROJECT_ID` int(11) NOT NULL COMMENT 'Project Id',
  `TASK_ID` int(11) NOT NULL COMMENT 'Task Id',
  `UNIT_COST_ORG` decimal(10,2) DEFAULT NULL COMMENT 'The cost rate for the time entry.',
  `UNIT_COST_SOW_CUR` decimal(10,2) DEFAULT NULL COMMENT 'The cost currency for the time entry.',
  `STATUS` varchar(100) NOT NULL COMMENT 'SUBMITTED, APPROVED, REJECTED, LOCKED',
  `COMMENTS` varchar(2048) DEFAULT NULL COMMENT 'Approve or Reject or Locked Status reason',
  `IS_INVOICED` int(11) NOT NULL DEFAULT '0' COMMENT 'Whether or not the time entry has been marked as invoiced.',
  `INVOICE_ID` varchar(100) DEFAULT NULL COMMENT 'Once the time entry has been invoiced, this field will include the associated invoice’s id and number.',
  `CREATED_BY` int(11) NOT NULL COMMENT 'Created  user id',
  `CREATION_DATE` datetime NOT NULL COMMENT 'Date and time the time entry was created.',
  `LAST_UPDATED_BY` int(11) DEFAULT NULL COMMENT 'Last updated user id',
  `LAST_UPDATE_DATE` datetime DEFAULT NULL COMMENT 'Date and time the time entry was last updated.',
  `MODIFIED_BY` int(11) DEFAULT NULL,
  `MODIFIED_DATE` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`TIME_SHEET_HISTORY_ID`),
  KEY `OSI_TIMESHEET_ENTRY_N1` (`EMPLOYEE_ID`,`ORG_ID`),
  KEY `OSI_TIMESHEET_ENTRY_N2` (`SPENT_DATE`),
  KEY `OSI_TIMESHEET_ENTRY_N3` (`PROJECT_ID`,`TASK_ID`),
  KEY `OSI_TIMESHEET_ENTRY_N4` (`TASK_ID`),
  KEY `OSI_TIMESHEET_ENTRY_N5` (`LAST_UPDATED_BY`),
  KEY `OSI_TIMESHEET_ENTRY_N6` (`CREATED_BY`),
  KEY `OSI_TIMESHEET_ENTRY_F3_idx` (`ORG_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=994 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;


-- Dump completed on 2018-03-30 15:13:36
