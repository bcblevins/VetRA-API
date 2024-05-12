drop table if exists prescription cascade;
drop table if exists result cascade;
drop table if exists parameter cascade;
drop table if exists test cascade;
drop table if exists medication cascade;
drop table if exists patient cascade;
drop table if exists "user" cascade;
drop table if exists "role" cascade;
drop table if exists "message" cascade;
drop table if exists "message_test" cascade;
drop table if exists "message_patient" cascade;
drop table if exists "refill_request" cascade;



CREATE TABLE "patient" (
  "patient_id" SERIAL PRIMARY KEY,
  "chart_number" varchar(8) UNIQUE,
  "first_name" varchar(20),
  "last_name" varchar(20),
  "birthday" date,
  "species" varchar(20),
  "sex" varchar(2),
  "username" varchar(30)
);

CREATE TABLE "user" (
  "username" varchar(30) PRIMARY KEY,
  "password" varchar(300),
  "first_name" varchar(20),
  "last_name" varchar(20)
);

CREATE TABLE "role" (
  "username" varchar(30),
  "role" varchar(250)
);

CREATE TABLE "test" (
  "test_id" SERIAL PRIMARY KEY,
  "name" varchar(50),
  "time_stamp" timestamp,
  "doctor_notes" varchar(500),
  "patient_id" int,
  "doctor_username" varchar(30)
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
  "is_qualitative" boolean DEFAULT false
);

CREATE TABLE "medication" (
  "name" varchar(50) PRIMARY KEY,
  "unit" varchar(50) NOT NULL
);

CREATE TABLE "prescription" (
  "prescription_id" SERIAL PRIMARY KEY,
  "quantity" numeric,
  "instructions" varchar(300),
  "refills" int,
  "is_active" boolean DEFAULT true,
  "patient_id" int,
  "medication_name" varchar(20),
  "doctor_username" varchar(30)
);

CREATE TABLE "message" (
  "message_id" SERIAL PRIMARY KEY,
  "subject" varchar(50),
  "body" varchar(1000),
  "username" varchar(30)
);

CREATE TABLE "message_test" (
  "message_id" int,
  "test_id" int,
  PRIMARY KEY ("message_id", "test_id")
);

CREATE TABLE "message_patient" (
  "message_id" int,
  "patient_id" int,
  PRIMARY KEY ("message_id", "patient_id")
);

CREATE TABLE "refill_request" (
  "refill_request_id" SERIAL PRIMARY KEY,
  "prescription_id" int,
  "status" varchar(20) DEFAULT 'PENDING',
  "request_date" timestamp DEFAULT (current_timestamp)
);

ALTER TABLE "test" ADD FOREIGN KEY ("doctor_username") REFERENCES "user" ("username") on delete cascade;

ALTER TABLE "patient" ADD FOREIGN KEY ("username") REFERENCES "user" ("username") on delete cascade;

ALTER TABLE "test" ADD FOREIGN KEY ("patient_id") REFERENCES "patient" ("patient_id") on delete cascade;

ALTER TABLE "result" ADD FOREIGN KEY ("test_id") REFERENCES "test" ("test_id") on delete cascade;

ALTER TABLE "result" ADD FOREIGN KEY ("parameter_name") REFERENCES "parameter" ("name") on delete cascade;

ALTER TABLE "prescription" ADD FOREIGN KEY ("medication_name") REFERENCES "medication" ("name") on delete cascade;

ALTER TABLE "prescription" ADD FOREIGN KEY ("doctor_username") REFERENCES "user" ("username") on delete cascade;

ALTER TABLE "prescription" ADD FOREIGN KEY ("patient_id") REFERENCES "patient" ("patient_id") on delete cascade;

ALTER TABLE "refill_request" ADD FOREIGN KEY ("prescription_id") REFERENCES "prescription" ("prescription_id") on delete cascade;

ALTER TABLE "message_patient" ADD FOREIGN KEY ("message_id") REFERENCES "message" ("message_id") on delete cascade;

ALTER TABLE "message_patient" ADD FOREIGN KEY ("patient_id") REFERENCES "patient" ("patient_id") on delete cascade;

ALTER TABLE "message" ADD FOREIGN KEY ("username") REFERENCES "user" ("username") on delete cascade;

ALTER TABLE "message_test" ADD FOREIGN KEY ("message_id") REFERENCES "message" ("message_id") on delete cascade;

ALTER TABLE "message_test" ADD FOREIGN KEY ("test_id") REFERENCES "test" ("test_id") on delete cascade;

ALTER TABLE "roles" ADD FOREIGN KEY ("username") REFERENCES "user" ("username") on delete cascade;

INSERT INTO parameter (name, range_low, range_high, unit) values
	('WBC', 4, 15.5, '10^3/mcL'),
	('RBC', 4.8, 9.3, '10^6/mcL'),
	('HGB', 12.1, 20.3, 'g/dl'),
	('HCT', 36, 60, '%'),
	('MCV', 58, 79, 'fL'),
	('PLT', 170, 400, '10^3/mcL');

insert into "user" (username, password, first_name, last_name) values
('bblevins96', '1234', 'Beau', 'Blevins'),
('cakelly4', '1234', 'Chris', 'Kelly');

insert into patient (chart_number, first_name, last_name, birthday, species, sex, username) values
('000000', 'Charlie', 'Blevins', '2015-03-14', 'Canine', 'SF', 'bblevins96'),
('000001', 'Sunny', 'Blevins', '2016-02-20', 'Feline', 'CM', 'bblevins96');

insert into test (name, time_stamp, doctor_notes, patient_id, doctor_username)
values ('CBC', '2024-02-20', 'Charlie''s labwork looks great!', 1, 'cakelly4');

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

insert into prescription (quantity, instructions, is_active, patient_id, medication_name, doctor_username) values
	(10, 'Give 1/2 tablet by mouth 3 hours prior to thunderstorms to reduce anxiety.', true, 1, 'Trazodone 50mg', 'cakelly4'),
	(30, 'Give 1/2 to 1 capsule by mouth twice daily or as needed to reduce anxiety.', true, 2, 'Gabapentin 100mg', 'cakelly4');

