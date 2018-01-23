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
		},
		{
			"d_calendar": {
				"DATE_KEY": "int",
				"DATE": "date",
				"DAY_NAME": "String",
				"YEAR": "String",
				"MONTH": "String",
				"MONTH_NAME": "String",
				"QUARTER": "String",
				"WEEK": "String"
			}
		},
		{
			"d_campus": {
				"CAMPUS_ID": "int",
				"CAMPUS_NAME": "String"
			}
		},
		{
			"d_exam": {
				"EXAM_ID": "int",
				"EXAM_MODEL": "String",
				"EXAM_TYPE": "String",
				"EXAM_NAME": "String",
				"EXAM_DATE":"date"
			}
		},
		{
			"d_section": {
				"SECTION_ID": "int",
				"SECTION_NAME": "int"
			}
		},
		{
			"d_student": {
				"STUDENT_ID": "int",
				"STUDENT_FULL_NAME": "String",
				"STUDENT_SHORT_NAME": "String"
			}
		},
		{
			"d_subject": {
				"SUBJECT_ID": "int",
				"SUBJECT_NAME": "String",
				"STUDENT_SHORT_NAME": "String"
			}
		},
		{
			"exam_result_stg": {
				"OMR_ID": "int",
				"NAME OF THE STUDENT": "String",
				"MAT_120": "String",
				"MATHS_RANK": "String",
				"MATHS_PERCENTAGE": "String",
			}
		},
		{
			"f_exam_result": {
				"EXAM_RESULT_ID": "int",
				"MAX_MATHS_MAKRS": "String",
				"MAX_PHYSICS_MARKS": "String",
				"MAX_CHEMISTRY_MARKS": "String",
				"PHYSICS_MARKS": "String",
				"CHEMISTRY_MARKS": "String",
				"MATHS_MARKS": "String",
				"MATHS_RANK": "String",
				"ALL_INDIA_RANK": "String",
				"PHYSICS_RANK": "String",
				"CHEMISTRY_RANK": "String",
				"STATE_RANK": "String",
				"CAMPUS_ID_FK":"int",
				"STUDENT_ID_FK":"int",
				"DATE_KEY_FK":"int",
				"SECTION_ID_FK":"int",
				"EXAM_ID_FK":"int"
			}
		},
		{
			"f_ques_ans": {
				"QUES_ANS_ID": "int",
				"QUESTION_NUM": "String",
				"ANSWER_RESULT": "String",
				"SUBJECT_ID_FK": "int",
				"STUDENT_ID_FK": "int",
				"CAMPUS_ID_FK": "int",
				"SECTION_ID_FK": "int",
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
