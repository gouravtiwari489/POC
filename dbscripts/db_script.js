conn = new Mongo("localhost:27017");
db = conn.getDB("datagenerator");
db.dropDatabase();
db.createCollection("domain");
print("domain Collection Created!");
db.domain.insert(
{
	"_id": "HRMS",
	"tables": [{
			"Employee": {
				"emp_id": "int",
				"con_id": "int",
				"dept_id": "int",
				"Name": "String",
				"age": "int",
				"dob": "date",
				"adress": "String",
				"status": "String"
			}
		},
		{
			"Contract": {
				"con_id": "int",
				"status": "String",
				"con_sdate": "int"
			}
		},
		{
			"Department": {
				"dept_id": "int",
				"dept_name": "String",
				"location": "int"
			}
		},
		{
			"Salary": {
				"sal_id": "int",
				"sal_amount": "int",
				"sal_deduction": "int",
				"sal_bonus": "int",
				"emp_d": "int"
			}
		},
		{
			"Project": {
				"proj_id": "int",
				"dept_no": "int",
				"proj_title": "int"
			}
		}

]});

db.domain.insert(
{
	"_id": "SupplyChain",
	"tables": [
	 {
		  "Users": {
			"patient_id": "int",
			"name": "String",
			"gender": "String",
			"address": "String",
			"pdetails": "String",
			"date_dmitted": "date",
			"date_discharged": "date"
		}
	  },
	  {
		  "rooms": {
			"room_id": "int",
			"room_type": "String",
			"period": "int"
     }
	 },
	 {
		 "Record": {
			"record_no": "int",
			"appoint_id": "int",
			"patient_id": "int",
			"description": "String"
		}
	 }

]});

db.domain.insert(
{
	"_id": "Finance",
	"tables": [
	 {
		  "account": {
			"account_id": "int",
			"acc_name": "String",
			"acc_type": "String"
			}
	  },
	  {
		  "Expenses": {
			"expense_id": "int",
			"expense_type": "String",
			"expense_date": "int"
     }
	 }

]});

