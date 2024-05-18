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

CREATE TABLE "user" (
  "username" varchar(30) PRIMARY KEY,
  "password" varchar(300) NOT NULL,
  "first_name" varchar(50) NOT NULL,
  "last_name" varchar(50) NOT NULL,
  "email" varchar(350)
);

CREATE TABLE "patient" (
  "patient_id" SERIAL PRIMARY KEY,
  "first_name" varchar(20) NOT NULL,
  "birthday" date,
  "species" varchar(20) NOT NULL,
  "sex" varchar(2),
  "owner_username" varchar(30) NOT NULL REFERENCES "user" ("username")
);

CREATE TABLE "role" (
  "username" varchar(30) NOT NULL REFERENCES "user" ("username"),
  "role" varchar(250) NOT NULL,
  PRIMARY KEY ("username", "role")
);

CREATE TABLE "test" (
  "test_id" SERIAL PRIMARY KEY,
  "name" varchar(50) NOT NULL,
  "time_stamp" timestamp NOT NULL,
  "patient_id" int NOT NULL REFERENCES "patient" ("patient_id"),
  "doctor_username" varchar(30) NOT NULL REFERENCES "user" ("username")
);

CREATE TABLE "parameter" (
  "name" varchar(20) PRIMARY KEY,
  "range_low" numeric,
  "range_high" numeric,
  "unit" varchar(20),
  "qualitative_normal" varchar(20),
  "is_qualitative" boolean DEFAULT false
);

CREATE TABLE "result" (
  "result_id" SERIAL PRIMARY KEY,
  "test_id" int NOT NULL REFERENCES "test" ("test_id"),
  "parameter_name" varchar(20) NOT NULL REFERENCES "parameter" ("name"),
  "result_value" varchar(20) NOT NULL
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
  "medication_name" varchar(20) NOT NULL REFERENCES "medication" ("name"),
  "doctor_username" varchar(30) NOT NULL REFERENCES "user" ("username")
);

CREATE TABLE "message" (
  "message_id" SERIAL PRIMARY KEY,
  "subject" varchar(50) NOT NULL,
  "body" varchar(1000) NOT NULL,
  "from_username" varchar(30) NOT NULL REFERENCES "user" ("username"),
  "to_username" varchar(30) NOT NULL REFERENCES "user" ("username")
);

CREATE TABLE "message_test" (
  "message_id" int NOT NULL REFERENCES "message" ("message_id"),
  "test_id" int NOT NULL REFERENCES "test" ("test_id"),
  PRIMARY KEY ("message_id", "test_id")
);

CREATE TABLE "message_patient" (
  "message_id" int NOT NULL REFERENCES "message" ("message_id"),
  "patient_id" int NOT NULL REFERENCES "patient" ("patient_id"),
  PRIMARY KEY ("message_id", "patient_id")
);

CREATE TABLE "request" (
  "request_id" SERIAL PRIMARY KEY,
  "prescription_id" int NOT NULL REFERENCES "prescription" ("prescription_id"),
  "status" varchar(20) DEFAULT 'PENDING',
  "request_date" timestamp DEFAULT (current_timestamp)
);


--ALTER TABLE "test" ADD FOREIGN KEY ("doctor_username") REFERENCES "user" ("username");

--ALTER TABLE "patient" ADD FOREIGN KEY ("owner_username") REFERENCES "user" ("username");

--ALTER TABLE "test" ADD FOREIGN KEY ("patient_id") REFERENCES "patient" ("patient_id");

--ALTER TABLE "result" ADD FOREIGN KEY ("test_id") REFERENCES "test" ("test_id");

--ALTER TABLE "result" ADD FOREIGN KEY ("parameter_name") REFERENCES "parameter" ("name");

--ALTER TABLE "prescription" ADD FOREIGN KEY ("medication_name") REFERENCES "medication" ("name");

--ALTER TABLE "prescription" ADD FOREIGN KEY ("doctor_username") REFERENCES "user" ("username");

--ALTER TABLE "prescription" ADD FOREIGN KEY ("patient_id") REFERENCES "patient" ("patient_id");

--ALTER TABLE "request" ADD FOREIGN KEY ("prescription_id") REFERENCES "prescription" ("prescription_id");

--ALTER TABLE "message_patient" ADD FOREIGN KEY ("message_id") REFERENCES "message" ("message_id");

--ALTER TABLE "message_patient" ADD FOREIGN KEY ("patient_id") REFERENCES "patient" ("patient_id");

--ALTER TABLE "message" ADD FOREIGN KEY ("from_username") REFERENCES "user" ("username");

--ALTER TABLE "message" ADD FOREIGN KEY ("to_username") REFERENCES "user" ("username");

--ALTER TABLE "message_test" ADD FOREIGN KEY ("message_id") REFERENCES "message" ("message_id");

--ALTER TABLE "message_test" ADD FOREIGN KEY ("test_id") REFERENCES "test" ("test_id");

--ALTER TABLE "role" ADD FOREIGN KEY ("username") REFERENCES "user" ("username");


-- INSERTS
INSERT INTO medication (name, unit) VALUES
    ('Trazodone 50mg', 'tablets'),
    ('Gabapentin 100mg', 'capsules');

INSERT INTO "user" (username, password, first_name, last_name, email) VALUES
    ('bblevins96', '$2a$10$rdrt3j7YkAaVTQJcGnPX.ORrpMZ3ZXUMZqhfx0jR68vLaqB2jvsH2', 'Beau', 'Blevins', 'notreal@test.com'),
    ('cakelly4', '$2a$10$rdrt3j7YkAaVTQJcGnPX.ORrpMZ3ZXUMZqhfx0jR68vLaqB2jvsH2', 'Chris', 'Kelly', 'itsmybossimnotgivingyouhisemail@shameonyou.com'),
    ('admin', '$2a$10$o5y4WbVoawMUwZiTnQINJOAm6QJyOE3dD2KYIE1kkze7O0m6PzqA.', 'admin', 'admin', 'company@info.com');

INSERT INTO message (subject, body, from_username, to_username) VALUES
    ('What is up', 'I''m just bored, messaged you to entertain myself.', 'bblevins96', 'cakelly4');

INSERT INTO patient (first_name, birthday, species, sex, owner_username) VALUES
    ('Charlie', '2015-03-14', 'Canine', 'SF', 'bblevins96'),
    ('Sunny', '2016-02-20', 'Feline', 'CM', 'bblevins96');

INSERT INTO message_patient (message_id, patient_id) VALUES (1, 1);

INSERT INTO test (name, time_stamp, patient_id, doctor_username) VALUES
    ('CBC', '2024-02-20 00:00:00', 1, 'cakelly4');

INSERT INTO message_test (message_id, test_id) VALUES (1, 1);

INSERT INTO parameter (name, range_low, range_high, unit, qualitative_normal, is_qualitative) VALUES
    ('WBC', 4, 15.5, '10^3/mcL', NULL, false),
    ('RBC', 4.8, 9.3, '10^6/mcL', NULL, false),
    ('HGB', 12.1, 20.3, 'g/dl', NULL, false),
    ('HCT', 36, 60, '%', NULL, false),
    ('MCV', 58, 79, 'fL', NULL, false),
    ('PLT', 170, 400, '10^3/mcL', NULL, false);

INSERT INTO prescription (quantity, instructions, refills, is_active, patient_id, medication_name, doctor_username) VALUES
    (10, 'Give 1/2 tablet by mouth 3 hours prior to thunderstorms to reduce anxiety.', 0, true, 1, 'Trazodone 50mg', 'cakelly4'),
    (30, 'Give 1/2 to 1 capsule by mouth twice daily or as needed to reduce anxiety.', 0, true, 2, 'Gabapentin 100mg', 'cakelly4');


INSERT INTO request (prescription_id, status, request_date) VALUES
    (1, 'PENDING', '2024-05-14 14:23:34.448466');

INSERT INTO result VALUES
    (1, 1, 'WBC', '9.3'),
    (2, 1, 'RBC', '8.0'),
    (3, 1, 'HGB', '20.3'),
    (4, 1, 'HCT', '54.0'),
    (5, 1, 'MCV', '67.0'),
    (6, 1, 'PLT', '330.0');

INSERT INTO role VALUES
    ('bblevins96', 'OWNER'),
    ('cakelly4', 'DOCTOR'),
    ('admin', 'ADMIN');