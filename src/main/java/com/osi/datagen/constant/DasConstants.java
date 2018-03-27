package com.osi.datagen.constant;

public interface DasConstants {

  String COMMA_SEPRATOR = ",";
  String NEWLINE_CHARACTER = "\n";
  String ALREADY_LOGGEDIN_EXCEPTION_MESSAGE = "Warning! You already login somewhere";
  String DATE_DATA_GENERATOR_BEGIN_TIME = "1900-01-01 00:00:00";
  String DATE_TIME_DATA_GENERATOR_OFFSET = "2012-01-01 00:00:00";
  String DATE_TIME_DATA_GENERATOR_END = "2013-01-01 00:00:00";
  double DOUBLE_DATA_GENERATOR_BEGIN = 1000d;
  double DOUBLE_DATA_GENERATOR_END = 9999d;
  String DECIMAL_FORMAT = "0.00";
  int LONG_DATA_GENERATOR_BEGIN = 10000;
  int INT_DATA_GENERATOR_BEGIN = 1000;
  int INT_DATA_GENERATOR_END = 9999;
  String ALPHA_NUMERIC_STRING = "abcdefghijklmnopqrstuvwxyz";
  String DATASETS_CHARACTERSET = "utf-8";
  String DATASETS_COMMONS = "Commons";
  int MILLIS_IN_DAY = 24 * 60 * 60 * 1000;
  String XML_ROOT_ELEMENT = "root";
  String XML_ROW_ELEMENT = "row";
  String DEFAULT_EXCEPTION_MESSAGE = "Please contact your administrator";
  String DATABASE_CONNECTION_EXCEPTION_MESSAGE = "Unable to connect to database";


	String HRMS = "HRMS";
	String SUPPLYCHAIN = "SupplyChain";
	String MANUFACTURING = "Manufacturing";
	String CRM = "CRM";
	String RETAIL = "Retail";
	String PURCHASE = "Purchase";
	String LOGISTICS = "Logistics";
	String FINANCIAL = "Financial";
	String HRMSCATEGORY = "HRMS-dataset.txt";
	String SUPPLYCHAINCATEGORY = "SupplyChain-dataset.txt";
	String MANUFACTURINGCATEGORY = "Manufacturing-Categories.txt";

	String DATASETS_PATH = "datasets\\";

	String OUTPUT_FILE_NAME = "DAS";

	String SQL_AND = "AND";
	String SQL_OR = "OR";
	String SQL_IN = "IN";
	String SQL_BETWEEN = "BETWEEN";
	String SQL_NOT_IN = "NOT IN";
	String SQL_LIKE = "LIKE";
	String ADMIN = "admin";
	String DEFAULT_USER = "user";
	String DEFAULT_PASSWORD = "user";
}
