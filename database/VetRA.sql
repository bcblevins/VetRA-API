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
drop table if exists "request" cascade;

CREATE TABLE "patient" (
  "patient_id" SERIAL PRIMARY KEY,
  "first_name" varchar(20) NOT NULL,
  "birthday" date,
  "species" varchar(20) NOT NULL,
  "sex" varchar(2),
  "owner_username" varchar(30) NOT NULL
);

CREATE TABLE "user" (
  "username" varchar(30) PRIMARY KEY,
  "password" varchar(300) NOT NULL,
  "first_name" varchar(20) NOT NULL,
  "last_name" varchar(20) NOT NULL
);

CREATE TABLE "role" (
  "username" varchar(30) NOT NULL,
  "role" varchar(250) NOT NULL
);

CREATE TABLE "test" (
  "test_id" SERIAL PRIMARY KEY,
  "name" varchar(50) NOT NULL,
  "time_stamp" timestamp NOT NULL,
  "patient_id" int NOT NULL,
  "doctor_username" varchar(30) NOT NULL
);

CREATE TABLE "result" (
  "result_id" SERIAL PRIMARY KEY,
  "test_id" int NOT NULL,
  "parameter_name" varchar(20) NOT NULL,
  "result_value" varchar(20) NOT NULL
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
  "quantity" numeric NOT NULL,
  "instructions" varchar(300) NOT NULL,
  "refills" int NOT NULL,
  "is_active" boolean DEFAULT true,
  "patient_id" int NOT NULL,
  "medication_name" varchar(20) NOT NULL,
  "doctor_username" varchar(30) NOT NULL
);

CREATE TABLE "message" (
  "message_id" SERIAL PRIMARY KEY,
  "subject" varchar(50) NOT NULL,
  "body" varchar(1000) NOT NULL,
  "from_username" varchar(30) NOT NULL,
  "to_username" varchar(30) NOT NULL
);

CREATE TABLE "message_test" (
  "message_id" int NOT NULL,
  "test_id" int NOT NULL,
  PRIMARY KEY ("message_id", "test_id")
);

CREATE TABLE "message_patient" (
  "message_id" int NOT NULL,
  "patient_id" int NOT NULL,
  PRIMARY KEY ("message_id", "patient_id")
);

CREATE TABLE "request" (
  "request_id" SERIAL PRIMARY KEY,
  "prescription_id" int NOT NULL,
  "status" varchar(20) DEFAULT 'PENDING',
  "request_date" timestamp DEFAULT (current_timestamp)
);

CREATE INDEX ON "role" ("username", "role");

ALTER TABLE "test" ADD FOREIGN KEY ("doctor_username") REFERENCES "user" ("username") ON DELETE CASCADE;

ALTER TABLE "patient" ADD FOREIGN KEY ("owner_username") REFERENCES "user" ("username") ON DELETE CASCADE;

ALTER TABLE "test" ADD FOREIGN KEY ("patient_id") REFERENCES "patient" ("patient_id") ON DELETE CASCADE;

ALTER TABLE "result" ADD FOREIGN KEY ("test_id") REFERENCES "test" ("test_id") ON DELETE CASCADE;

ALTER TABLE "result" ADD FOREIGN KEY ("parameter_name") REFERENCES "parameter" ("name") ON DELETE CASCADE;

ALTER TABLE "prescription" ADD FOREIGN KEY ("medication_name") REFERENCES "medication" ("name") ON DELETE CASCADE;

ALTER TABLE "prescription" ADD FOREIGN KEY ("doctor_username") REFERENCES "user" ("username") ON DELETE CASCADE;

ALTER TABLE "prescription" ADD FOREIGN KEY ("patient_id") REFERENCES "patient" ("patient_id") ON DELETE CASCADE;

ALTER TABLE "request" ADD FOREIGN KEY ("prescription_id") REFERENCES "prescription" ("prescription_id") ON DELETE CASCADE;

ALTER TABLE "message_patient" ADD FOREIGN KEY ("message_id") REFERENCES "message" ("message_id") ON DELETE CASCADE;

ALTER TABLE "message_patient" ADD FOREIGN KEY ("patient_id") REFERENCES "patient" ("patient_id") ON DELETE CASCADE;

ALTER TABLE "message" ADD FOREIGN KEY ("from_username") REFERENCES "user" ("username") ON DELETE CASCADE;

ALTER TABLE "message" ADD FOREIGN KEY ("to_username") REFERENCES "user" ("username") ON DELETE CASCADE;

ALTER TABLE "message_test" ADD FOREIGN KEY ("message_id") REFERENCES "message" ("message_id") ON DELETE CASCADE;

ALTER TABLE "message_test" ADD FOREIGN KEY ("test_id") REFERENCES "test" ("test_id") ON DELETE CASCADE;

ALTER TABLE "role" ADD FOREIGN KEY ("username") REFERENCES "user" ("username") ON DELETE CASCADE;


-- INSERTS
INSERT INTO medication VALUES ('Trazodone 50mg', 'tablets');
INSERT INTO medication VALUES ('Gabapentin 100mg', 'capsules');

INSERT INTO "user" VALUES ('bblevins96', '1234', 'Beau', 'Blevins');
INSERT INTO "user" VALUES ('cakelly4', '1234', 'Chris', 'Kelly');
INSERT INTO "user" VALUES ('admin', 'admin', 'admin', 'admin');

INSERT INTO message VALUES (1, 'What is up', 'I''m just bored, messaged you to entertain myself.', 'bblevins96', 'cakelly4');

INSERT INTO patient VALUES (1, 'Charlie', '2015-03-14', 'Canine', 'SF', 'bblevins96');
INSERT INTO patient VALUES (2, 'Sunny', '2016-02-20', 'Feline', 'CM', 'bblevins96');

INSERT INTO message_patient VALUES (1, 1);

INSERT INTO test VALUES (1, 'CBC', '2024-02-20 00:00:00', 1, 'cakelly4');

INSERT INTO message_test VALUES (1, 1);

INSERT INTO parameter VALUES ('WBC', 4, 15.5, '10^3/mcL', NULL, false);
INSERT INTO parameter VALUES ('RBC', 4.8, 9.3, '10^6/mcL', NULL, false);
INSERT INTO parameter VALUES ('HGB', 12.1, 20.3, 'g/dl', NULL, false);
INSERT INTO parameter VALUES ('HCT', 36, 60, '%', NULL, false);
INSERT INTO parameter VALUES ('MCV', 58, 79, 'fL', NULL, false);
INSERT INTO parameter VALUES ('PLT', 170, 400, '10^3/mcL', NULL, false);

INSERT INTO prescription VALUES (1, 10, 'Give 1/2 tablet by mouth 3 hours prior to thunderstorms to reduce anxiety.', 0, true, 1, 'Trazodone 50mg', 'cakelly4');
INSERT INTO prescription VALUES (2, 30, 'Give 1/2 to 1 capsule by mouth twice daily or as needed to reduce anxiety.', 0, true, 2, 'Gabapentin 100mg', 'cakelly4');

INSERT INTO request VALUES (1, 1, 'PENDING', '2024-05-14 14:23:34.448466');

INSERT INTO result VALUES (1, 1, 'WBC', '9.3');
INSERT INTO result VALUES (2, 1, 'RBC', '8.0');
INSERT INTO result VALUES (3, 1, 'HGB', '20.3');
INSERT INTO result VALUES (4, 1, 'HCT', '54.0');
INSERT INTO result VALUES (5, 1, 'MCV', '67.0');
INSERT INTO result VALUES (6, 1, 'PLT', '330.0');


INSERT INTO role VALUES ('bblevins96', 'OWNER');
INSERT INTO role VALUES ('cakelly4', 'DOCTOR');
INSERT INTO role VALUES ('admin', 'ADMIN');