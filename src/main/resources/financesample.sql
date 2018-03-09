CREATE DATABASE `finance` 

USE `finance`;
CREATE TABLE 'company' (
    'id'     int NOT NULL PRIMARY KEY,
    'name'   varchar(255) NOT NULL
);

-- "Balance sheet", "IFRS Income statement", etc
CREATE TABLE statement (
    'id'     int NOT NULL PRIMARY KEY,
    'name'   varchar(255) NOT NULL
);

--- "Tangible assets", "Outstanding stock", etc
CREATE TABLE statementRow (
    'id'    int NOT NULL PRIMARY KEY,
    'statementId' int NOT NULL,
    'rowOrder'  int NOT NULL,
    'rowTitle'  varchar(255) NOT NULL,
    'rowDescription' varchar(max) NULL,
    'rowProperties' varchar(max) NULL,
    FOREIGN KEY ('statementId') REFERENCES statement ('id')
);

--- The facts
CREATE TABLE 'statementFact' (
    'companyId'      int NOT NULL,
    'statementRowId' int NOT NULL,
    'statementdate'  date NOT NULL,
    'amount'         numeric NULL,
    PRIMARY KEY ('statementdate', 'statementRow'),
    FOREIGN KEY ('companyId') REFERENCES company ('id'),
    FOREIGN KEY ('statementRowId') REFERENCES statementRow ('id')
);