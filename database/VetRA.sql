drop table if exists prescription cascade;
drop table if exists "result" cascade;
drop table if exists test cascade;
drop table if exists medication cascade;
drop table if exists patient cascade;
drop table if exists "user" cascade;
drop table if exists "role" cascade;
drop table if exists "user_vms" cascade;
drop table if exists "patient_vms" cascade;
drop table if exists "message" cascade;
drop table if exists "request" cascade;
drop table if exists "meta";

CREATE TABLE "user" (
  "username" varchar(30) PRIMARY KEY,
  "password" varchar(300) NOT NULL,
  "first_name" varchar(50) NOT NULL,
  "last_name" varchar(50) NOT NULL,
  "email" varchar(350)
);

CREATE TABLE "role" (
  "username" varchar(30) NOT NULL REFERENCES "user" ("username") ON DELETE CASCADE,
  "role" varchar(250) NOT NULL,
  PRIMARY KEY ("username", "role")
);

CREATE TABLE "user_vms" (
  "username" varchar(30) NOT NULL REFERENCES "user" ("username") ON DELETE CASCADE,
  "vms_name" varchar(30) NOT NULL,
  "vms_id" varchar(300) NOT NULL,
  PRIMARY KEY ("username", "vms_name")
);

CREATE TABLE "patient" (
  "patient_id" SERIAL PRIMARY KEY,
  "first_name" varchar(20) NOT NULL,
  "birthday" date,
  "species" varchar(20) NOT NULL,
  "sex" varchar(2),
  "owner_username" varchar(30) NOT NULL REFERENCES "user" ("username") ON DELETE CASCADE
);

CREATE TABLE "patient_vms" (
  "patient_id" int NOT NULL REFERENCES "patient" ("patient_id") ON DELETE CASCADE,
  "vms_name" varchar(30) NOT NULL,
  "vms_id" varchar(300) NOT NULL,
  PRIMARY KEY ("patient_id", "vms_name")
);

CREATE TABLE "test" (
  "test_id" SERIAL PRIMARY KEY,
  "name" varchar(50) NOT NULL,
  "time_stamp" timestamp NOT NULL,
  "patient_id" int NOT NULL REFERENCES "patient" ("patient_id") ON DELETE CASCADE,
  "doctor_username" varchar(30) NOT NULL REFERENCES "user" ("username") ON DELETE CASCADE
);

CREATE TABLE "result" (
	"result_id" SERIAL PRIMARY KEY,
	"test_id" int NOT NULL REFERENCES test (test_id) ON DELETE CASCADE,
	"result_value" varchar(250) NOT NULL,
	"parameter_name" varchar(250),
	"range_low" varchar(250),
	"range_high" varchar(250),
	"unit" varchar(250)
);

CREATE TABLE "medication" (
  "name" varchar(50) PRIMARY KEY,
  "unit" varchar(50) NOT NULL
);

CREATE TABLE "prescription" (
  "prescription_id" SERIAL PRIMARY KEY,
  "quantity" numeric NOT NULL,
  "instructions" varchar(300) NOT NULL,
  "refills" int NOT NULL,
  "is_active" boolean DEFAULT true,
  "patient_id" int NOT NULL REFERENCES "patient" ("patient_id"),
  "medication_name" varchar(20) NOT NULL REFERENCES "medication" ("name") ON DELETE CASCADE,
  "doctor_username" varchar(30) NOT NULL REFERENCES "user" ("username") ON DELETE CASCADE
);

CREATE TABLE "message" (
  "message_id" SERIAL PRIMARY KEY,
  "body" varchar(1000) NOT NULL,
  "time_stamp" timestamp DEFAULT (current_timestamp),
  "from_username" varchar(30) NOT NULL REFERENCES "user" ("username") ON DELETE CASCADE,
  "to_username" varchar(30) NOT NULL REFERENCES "user" ("username") ON DELETE CASCADE,
  "test_id" int REFERENCES "test" ("test_id") ON DELETE CASCADE,
  "prescription_id" int REFERENCES "prescription" ("prescription_id") ON DELETE CASCADE,
  "patient_id" int NOT NULL REFERENCES "patient" ("patient_id") ON DELETE CASCADE
);


CREATE TABLE "request" (
  "request_id" SERIAL PRIMARY KEY,
  "prescription_id" int NOT NULL REFERENCES "prescription" ("prescription_id") ON DELETE CASCADE,
  "status" varchar(20) DEFAULT 'PENDING',
  "request_date" timestamp DEFAULT (current_timestamp)
);

CREATE TABLE "meta" (
  "action" varchar(300) PRIMARY KEY,
  "performed_at" timestamp DEFAULT (current_timestamp)
);


-- INSERTS
INSERT INTO medication (name, unit) VALUES
    ('Trazodone 50mg', 'tablets'),
    ('Gabapentin 100mg', 'capsules');

INSERT INTO "user" (username, password, first_name, last_name, email) VALUES
    ('bblevins96', '$2a$10$rdrt3j7YkAaVTQJcGnPX.ORrpMZ3ZXUMZqhfx0jR68vLaqB2jvsH2', 'Beau', 'Blevins', 'notreal@test.com'),
    ('cakelly4', '$2a$10$rdrt3j7YkAaVTQJcGnPX.ORrpMZ3ZXUMZqhfx0jR68vLaqB2jvsH2', 'Chris', 'Kelly', 'itsmybossimnotgivingyouhisemail@shameonyou.com'),
    ('admin', '$2a$10$o5y4WbVoawMUwZiTnQINJOAm6QJyOE3dD2KYIE1kkze7O0m6PzqA.', 'admin', 'admin', 'company@info.com');

INSERT INTO patient (first_name, birthday, species, sex, owner_username) VALUES
    ('Charlie', '2015-03-14', 'Canine', 'SF', 'bblevins96'),
    ('Sunny', '2016-02-20', 'Feline', 'CM', 'bblevins96'),
	('Arlo', '2020-02-20', 'Feline', 'CM', 'bblevins96');



INSERT INTO test (name, time_stamp, patient_id, doctor_username) VALUES
    ('CBC', '2024-05-24 00:00:00', 1, 'cakelly4'),
	('Fecal',	'2024-07-04 11:09:34.905699',	2,	'cakelly4'),
	('CBC',	'2024-07-04 11:10:30.847789',	1,	'cakelly4'),
	('Fecal',	'2024-07-04 11:10:30.847789',	1,	'cakelly4'),
	('Fecal',	'2024-07-04 11:10:30.847789',	1,	'cakelly4'),
	('Fecal',	'2024-07-04 11:10:30.847789',	2,	'cakelly4');

INSERT INTO "message" (body, from_username, to_username, test_id, patient_id) VALUES
    ('Charlie''s labwork looks great, her white blood cell count is back in the normal range. How is she doing after her visit?', 'cakelly4', 'bblevins96', 1, 1),
	('Please help, Charlie has kidnapped me and is driving me to the treat store. I haven''t told her yet that that doesn''t exist.', 'cakelly4', 'cakelly4', null, 1),
	('The latest lab results for Charlie are in. Liver values have improved and the rest of her bloodwork looks great.', 'cakelly4', 'bblevins96', null, 1),
	('Charlie''s weight is up a little bit. I recommend cutting back on her food by 1/4 cup per day. Let me know if you have any questions.', 'cakelly4', 'bblevins96', null, 1);

INSERT INTO "result" (test_id, result_value, parameter_name, range_low, range_high, unit) VALUES
    (1, '9.3', 'WBC', '4', '15.5', '10^3/mcL'),
    (1, '8.0', 'RBC', '4.8', '9.3', '10^6/mcL'),
    (1, '20.3', 'HGB', '12.1', '20.3', 'g/dl'),
    (1, '54.0', 'HCT', '36', '60', '%'),
    (1, '67.0', 'MCV', '58', '79', 'fL'),
    (1, '330', 'PLT', '170', '400', '10^3/mcL'),
	(2,	'Negative',	'Hookworms',	null,	null,	null),
	(2,	'Negative',	'Roundworms',	null,	null,	null),
	(2,	'Positive',	'Whipworms',	null,	null,	null),
	(2,	'Positive',	'Tapeworms',	null,	null,	null),
	(3,	'14.8',	'WBC',	'4.0',	'15.5',	'10^3/uL'),
	(3,	'5.27',	'RBC',	'4.5',	'8.25',	'10^6/uL'),
	(3,	'18.s4',	'Hemoglobin',	'11.9',	'18.9', 'g/dL'),
	(3,	'60',	    'Hematocrit',	'36',	'60',	'%'),
	(3,	'63',	    'MCV',	'58',	'79',	'fL'),
	(3,	'212',	'Platelets',	'170',	'400',	'10^3/uL'),
	(4,	'Positive',	'Hookworms',	null,	null,	null),
	(4,	'Negative',	'Roundworms',	null,	null,	null),
	(4,	'Negative',	'Whipworms',	null,	null,	null),
	(4,	'Negative',	'Tapeworms',	null,	null,	null),
	(5,	'Positive',	'Hookworms',	null,	null,	null),
	(5,	'Negative',	'Roundworms',	null,	null,	null),
	(5,	'Negative',	'Whipworms',	null,	null,	null),
	(5,	'Negative',	'Tapeworms',	null,	null,	null),
	(6,	'Negative',	'Hookworms',	null,	null,	null),
	(6,	'Negative',	'Roundworms',	null,	null,	null),
	(6,	'Positive',	'Whipworms',	null,	null,	null),
	(6,	'Negative',	'Tapeworms',	null,	null,	null);

INSERT INTO prescription (quantity, instructions, refills, is_active, patient_id, medication_name, doctor_username) VALUES
    (10, 'Give 1/2 tablet by mouth 3 hours prior to thunderstorms to reduce anxiety.', 0, true, 1, 'Trazodone 50mg', 'cakelly4'),
    (30, 'Give 1/2 to 1 capsule by mouth twice daily or as needed to reduce anxiety.', 0, true, 2, 'Gabapentin 100mg', 'cakelly4');


INSERT INTO role VALUES
    ('bblevins96', 'OWNER'),
    ('cakelly4', 'DOCTOR'),
    ('admin', 'ADMIN');

INSERT INTO "meta" (action, performed_at) VALUES
    ('ezyvet patients updated', '1970-01-01 00:00:00'),
	('ezyvet tests updated', '1970-01-01 00:00:00');

	
	
	