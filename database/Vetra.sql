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
  "is_qualitative" boolean
);

CREATE TABLE "medication" (
  "name" varchar(20) PRIMARY KEY
);

CREATE TABLE "prescription" (
  "prescription_id" SERIAL PRIMARY KEY,
  "quantity" varchar(10),
  "instructions" varchar(50),
  "isActive" boolean DEFAULT true,
  "patient_id" int,
  "medication_name" int,
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
