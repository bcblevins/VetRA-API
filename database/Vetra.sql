drop table if exists prescription;
drop table if exists result;
drop table if exists parameter;
drop table if exists test;
drop table if exists medication;
drop table if exists patient;
drop table if exists person;

CREATE TABLE "patient" (
  "patient_id" SERIAL PRIMARY KEY,
  "chart_number" varchar(8) UNIQUE,
  "first_name" varchar(20),
  "last_name" varchar(20),
  "birthday" date,
  "species" varchar(20),
  "sex" varchar(2),
  "owner_id" int
);

CREATE TABLE "person" (
  "person_id" SERIAL PRIMARY KEY,
  "first_name" varchar(20),
  "last_name" varchar(20),
  "is_doctor" boolean
);

CREATE TABLE "test" (
  "test_id" SERIAL PRIMARY KEY,
  "name" varchar(50),
  "time_stamp" timestamp,
  "doctor_notes" varchar(500),
  "patient_id" int,
  "doctor_id" int
);

CREATE TABLE "result" (
  "result_id" SERIAL PRIMARY KEY,
  "test_id" int,
  "parameter_name" varchar(20),
  "result_value" varchar(20)
);

CREATE TABLE "parameter" (
  "name" varchar(20) PRIMARY KEY,
  "range_low" numeric,
  "range_high" numeric,
  "unit" varchar(20),
  "qualitative_normal" varchar(20),
  "is_qualitative" boolean default false
);

CREATE TABLE "medication" (
  "name" varchar(50) PRIMARY KEY,
  "unit" varchar(50) not null
);

CREATE TABLE "prescription" (
  "prescription_id" SERIAL PRIMARY KEY,
  "quantity" numeric,
  "instructions" varchar(300),
  "is_active" boolean DEFAULT true,
  "patient_id" int,
  "medication_name" varchar(20),
  "doctor_id" int
);

ALTER TABLE "test" ADD FOREIGN KEY ("doctor_id") REFERENCES "person" ("person_id");

ALTER TABLE "patient" ADD FOREIGN KEY ("owner_id") REFERENCES "person" ("person_id");

ALTER TABLE "test" ADD FOREIGN KEY ("patient_id") REFERENCES "patient" ("patient_id");

ALTER TABLE "result" ADD FOREIGN KEY ("test_id") REFERENCES "test" ("test_id");

ALTER TABLE "result" ADD FOREIGN KEY ("parameter_name") REFERENCES "parameter" ("name");

ALTER TABLE "prescription" ADD FOREIGN KEY ("medication_name") REFERENCES "medication" ("name");

ALTER TABLE "prescription" ADD FOREIGN KEY ("doctor_id") REFERENCES "person" ("person_id");

ALTER TABLE "prescription" ADD FOREIGN KEY ("patient_id") REFERENCES "patient" ("patient_id");

INSERT INTO parameter (name, range_low, range_high, unit) values
	('WBC', 4, 15.5, '10^3/mcL'),
	('RBC', 4.8, 9.3, '10^6/mcL'),
	('HGB', 12.1, 20.3, 'g/dl'),
	('HCT', 36, 60, '%'),
	('MCV', 58, 79, 'fL'),
	('PLT', 170, 400, '10^3/mcL');
	
insert into person (first_name, last_name, is_doctor) values
('Beau', 'Blevins', false),
('Chris', 'Kelly', true);

insert into patient (chart_number, first_name, last_name, birthday, species, sex, owner_id) values 
('000000', 'Charlie', 'Blevins', '2015-03-14', 'Canine', 'SF', 1),
('000001', 'Sunny', 'Blevins', '2016-02-20', 'Feline', 'CM', 1);

insert into test (name, time_stamp, doctor_notes, patient_id, doctor_id)
values ('CBC', '2024-02-20', 'Charlie''s labwork looks great!', 1, 2);

insert into result (test_id, parameter_name, result_value) values 	
	(1, 'WBC', 9.3),
	(1, 'RBC', 8.0),
	(1, 'HGB', 20.3),
	(1, 'HCT', 54.0),
	(1, 'MCV', 67.0),
	(1, 'PLT', 330.0);
	
insert into medication (name, unit) values 
	('Trazodone 50mg', 'tablets'),
	('Gabapentin 100mg', 'capsules');
	
insert into prescription (quantity, instructions, is_active, patient_id, medication_name, doctor_id) values
	(10, 'Give 1/2 tablet by mouth 3 hours prior to thunderstorms to reduce anxiety.', true, 1, 'Trazodone 50mg', 2),
	(30, 'Give 1/2 to 1 capsule by mouth twice daily or as needed to reduce anxiety.', true, 2, 'Gabapentin 100mg', 2);
	