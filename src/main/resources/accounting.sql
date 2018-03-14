CREATE SCHEMA accounting;

CREATE TABLE accounting_category ( 
	accounting_category_id int  NOT NULL  ,
	accounting_category_name varchar(100)    ,
	accounting_category_description varchar(200)    ,
	CONSTRAINT pk_accounting_category_accounting_category_id PRIMARY KEY ( accounting_category_id )
 ) engine=InnoDB;

CREATE TABLE organization ( 
	organization_id      int  NOT NULL  ,
	organization_name    varchar(100)    ,
	organization_description varchar(200)    ,
	organization_address varchar(200)    ,
	CONSTRAINT pk_organization_organization_id PRIMARY KEY ( organization_id )
 ) engine=InnoDB;

CREATE TABLE accounting_structure ( 
	accounting_structure_id int  NOT NULL  ,
	organization_id      int    ,
	accounting_structure_name varchar(100)    ,
	accounting_structure_description varchar(200)    ,
	CONSTRAINT pk_accounting_structure_accounting_structure_id PRIMARY KEY ( accounting_structure_id ),
	CONSTRAINT fk_accounting_structure FOREIGN KEY ( organization_id ) REFERENCES organization( organization_id ) ON DELETE NO ACTION ON UPDATE NO ACTION
 ) engine=InnoDB;

CREATE INDEX idx_accounting_structure_organization_id ON accounting_structure ( organization_id );

CREATE TABLE accounting_structure_item ( 
	accounting_structure_item_id int  NOT NULL  ,
	accounting_category_id int    ,
	accounting_structure_id int    ,
	accounting_structure_item_name varchar(100)    ,
	accounting_structure_item_description varchar(200)    ,
	CONSTRAINT pk_accounting_structure_item_accounting_structure_item_id PRIMARY KEY ( accounting_structure_item_id ),
	CONSTRAINT fk_accounting_structure FOREIGN KEY ( accounting_structure_id ) REFERENCES accounting_structure( accounting_structure_id ) ON DELETE NO ACTION ON UPDATE NO ACTION,
	CONSTRAINT fk_accounting_category FOREIGN KEY ( accounting_category_id ) REFERENCES accounting_category( accounting_category_id ) ON DELETE NO ACTION ON UPDATE NO ACTION
 ) engine=InnoDB;

CREATE INDEX idx_accounting_structure_item_accounting_structure_id ON accounting_structure_item ( accounting_structure_id );

CREATE INDEX idx_accounting_structure_item_accounting_category_id ON accounting_structure_item ( accounting_category_id );

