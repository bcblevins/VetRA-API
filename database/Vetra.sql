CREATE TABLE "patient" (
  "patient_id" serial PRIMARY KEY,
  "chart_number" varchar(8) UNIQUE,
  "first_name" varchar(20),
  "last_name" varchar(20),
  "birthday" date,
  "species" varchar(20),
  "sex" varchar(2),
  "owner_id" int
);

CREATE TABLE "person" (
  "person_id" serial PRIMARY KEY,
  "first_name" varchar(20),
  "last_name" varchar(20),
  "is_doctor" boolean
);

CREATE TABLE "test" (
  "test_id" serial PRIMARY KEY,
  "patient_id" int,
  "time_stamp" timestamp
);

CREATE TABLE "result" (
  "result_id" serial PRIMARY KEY,
  "test_id" int,
  "parameter_id" int,
  "result_value" varchar(20)
);

CREATE TABLE "parameter" (
  "parameter_id" serial PRIMARY KEY,
  "name" varchar(20),
  "range_low" numeric,
  "range_high" numeric,
  "unit" varchar(20),
  "normal_result" varchar(20),
  "is_qualitative" boolean
);

CREATE TABLE "medication" (
  "medication_id" serial PRIMARY KEY,
  "name" varchar(20) UNIQUE
);

CREATE TABLE "prescription" (
  "prescription_id" serial PRIMARY KEY,
  "quantity" varchar(10),
  "instructions" varchar(50),
  "patient_id" int,
  "medication_id" int,
  "doctor_id" int
);

ALTER TABLE "patient" ADD FOREIGN KEY ("owner_id") REFERENCES "person" ("person_id");

ALTER TABLE "test" ADD FOREIGN KEY ("patient_id") REFERENCES "patient" ("patient_id");

ALTER TABLE "result" ADD FOREIGN KEY ("test_id") REFERENCES "test" ("test_id");

ALTER TABLE "result" ADD FOREIGN KEY ("parameter_id") REFERENCES "parameter" ("parameter_id");

ALTER TABLE "prescription" ADD FOREIGN KEY ("medication_id") REFERENCES "medication" ("medication_id");

ALTER TABLE "prescription" ADD FOREIGN KEY ("doctor_id") REFERENCES "person" ("person_id");

ALTER TABLE "prescription" ADD FOREIGN KEY ("patient_id") REFERENCES "patient" ("patient_id");
