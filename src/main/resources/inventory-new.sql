CREATE SCHEMA inventory;

CREATE TABLE employees ( 
	employee_id          int  NOT NULL  ,
	employee_name        varchar(100)    ,
	employee_dob         date    ,
	employee_address     varchar(200)    ,
	CONSTRAINT unq_employees_employee_id UNIQUE ( employee_id ) ,
	CONSTRAINT pk_employees_employee_id PRIMARY KEY ( employee_id )
 ) engine=InnoDB;

CREATE TABLE inventory_item_type ( 
	inventory_item_type_code int  NOT NULL  ,
	inventory_item_type_description varchar(500)    ,
	CONSTRAINT pk_inventory_item_type_inventory_item_type_code PRIMARY KEY ( inventory_item_type_code )
 ) engine=InnoDB;

CREATE TABLE inventory_items ( 
	inventory_item_id    int  NOT NULL  ,
	inventory_item_type_code int    ,
	item_description     varchar(500)    ,
	number_in_stock      int    ,
	rental_or_sale_or_both varchar(100)    ,
	rental_daily_rate    decimal(10,2)    ,
	sale_price           decimal(10,2)    ,
	CONSTRAINT pk_inventory_items_inventory_item_id PRIMARY KEY ( inventory_item_id ),
	CONSTRAINT fk_inventory_items FOREIGN KEY ( inventory_item_type_code ) REFERENCES inventory_item_type( inventory_item_type_code ) ON DELETE NO ACTION ON UPDATE NO ACTION
 ) engine=InnoDB;

CREATE INDEX idx_inventory_items_inventory_item_type_code ON inventory_items ( inventory_item_type_code );

CREATE TABLE maintenance_skills ( 
	skill_id             int  NOT NULL  ,
	skill_short_name     varchar(100)    ,
	skill_dsecription    varchar(500)    ,
	CONSTRAINT unq_maintenance_skills_skill_id UNIQUE ( skill_id ) ,
	CONSTRAINT pk_maintenance_skills_skill_id PRIMARY KEY ( skill_id )
 ) engine=InnoDB;

CREATE TABLE employee_skills ( 
	employee_id          int    ,
	skill_id             int    ,
	CONSTRAINT fk_employee_skills_employees FOREIGN KEY ( employee_id ) REFERENCES employees( employee_id ) ON DELETE NO ACTION ON UPDATE NO ACTION,
	CONSTRAINT fk_employee_skills FOREIGN KEY ( skill_id ) REFERENCES maintenance_skills( skill_id ) ON DELETE NO ACTION ON UPDATE NO ACTION
 ) engine=InnoDB;

CREATE INDEX idx_employee_skills_employee_id ON employee_skills ( employee_id );

CREATE INDEX idx_employee_skills_skill_id ON employee_skills ( skill_id );

CREATE TABLE item_maintenance_work ( 
	item_maintenance_work_id int  NOT NULL  ,
	inventory_item_id    int    ,
	maintenance_schedule_id int    ,
	work_done_by_employee_id int    ,
	maintenance_date     date    ,
	maintenance_comments varchar(500)    ,
	other_maintenance_details varchar(500)    ,
	CONSTRAINT pk_item_maintenance_work_item_maintenance_work_id PRIMARY KEY ( item_maintenance_work_id )
 ) engine=InnoDB;

CREATE INDEX idx_item_maintenance_work_inventory_item_id ON item_maintenance_work ( inventory_item_id );

CREATE INDEX idx_item_maintenance_work_maintenance_schedule_id ON item_maintenance_work ( maintenance_schedule_id );

CREATE INDEX idx_item_maintenance_work_work_done_by_employee_id ON item_maintenance_work ( work_done_by_employee_id );

CREATE TABLE maintenance_schedule ( 
	maintenance_schedule_id int  NOT NULL  ,
	inventory_item_type_code int    ,
	next_maintenance_schedule_id int    ,
	maintenance_schedule_details varchar(500)    ,
	CONSTRAINT pk_maintenance_schedule_maintenance_schedule_id PRIMARY KEY ( maintenance_schedule_id )
 ) engine=InnoDB;

CREATE INDEX idx_maintenance_schedule_inventory_item_type_code ON maintenance_schedule ( inventory_item_type_code );

CREATE INDEX idx_maintenance_schedule_next_maintenance_schedule_id ON maintenance_schedule ( next_maintenance_schedule_id );

CREATE TABLE maintenance_skills_required ( 
	maintenance_schedule_id int  NOT NULL  ,
	skill_id             int  NOT NULL  ,
	CONSTRAINT pk_maintenance_skills_required_maintenance_schedule_id PRIMARY KEY ( maintenance_schedule_id )
 ) engine=InnoDB;

CREATE INDEX idx_maintenance_skills_required_skill_id ON maintenance_skills_required ( skill_id );

ALTER TABLE item_maintenance_work ADD CONSTRAINT fk_item_maintenance_work FOREIGN KEY ( inventory_item_id ) REFERENCES inventory_items( inventory_item_id ) ON DELETE NO ACTION ON UPDATE NO ACTION;

ALTER TABLE item_maintenance_work ADD CONSTRAINT fk_maintenance_schedule FOREIGN KEY ( maintenance_schedule_id ) REFERENCES maintenance_schedule( maintenance_schedule_id ) ON DELETE NO ACTION ON UPDATE NO ACTION;

ALTER TABLE item_maintenance_work ADD CONSTRAINT fk_employees FOREIGN KEY ( work_done_by_employee_id ) REFERENCES employees( employee_id ) ON DELETE NO ACTION ON UPDATE NO ACTION;

ALTER TABLE maintenance_schedule ADD CONSTRAINT fk_inventory_item_type FOREIGN KEY ( inventory_item_type_code ) REFERENCES inventory_item_type( inventory_item_type_code ) ON DELETE NO ACTION ON UPDATE NO ACTION;

ALTER TABLE maintenance_schedule ADD CONSTRAINT fk_maintenance_schedule FOREIGN KEY ( next_maintenance_schedule_id ) REFERENCES maintenance_skills_required( maintenance_schedule_id ) ON DELETE NO ACTION ON UPDATE NO ACTION;

ALTER TABLE maintenance_skills_required ADD CONSTRAINT fk_maintenance_schedule FOREIGN KEY ( maintenance_schedule_id ) REFERENCES maintenance_schedule( maintenance_schedule_id ) ON DELETE NO ACTION ON UPDATE NO ACTION;

ALTER TABLE maintenance_skills_required ADD CONSTRAINT fk_maintenance_skills FOREIGN KEY ( skill_id ) REFERENCES maintenance_skills( skill_id ) ON DELETE NO ACTION ON UPDATE NO ACTION;

