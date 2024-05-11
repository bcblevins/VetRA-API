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
  "doctor_id" int
);

CREATE TABLE "message" (
  "message_id" serial PRIMARY KEY,
  "subject" varchar(50),
  "body" varchar(1000),
  "person_id" int
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
  "status" varchar(20) DEFAULT 'PENDING' CHECK (status IN ('PENDING', 'APPROVED', 'DENIED'))
  "request_date" timestamp default current_timestamp,
);

ALTER TABLE "test" ADD FOREIGN KEY ("doctor_id") REFERENCES "person" ("person_id");

ALTER TABLE "patient" ADD FOREIGN KEY ("owner_id") REFERENCES "person" ("person_id");

ALTER TABLE "test" ADD FOREIGN KEY ("patient_id") REFERENCES "patient" ("patient_id");

ALTER TABLE "result" ADD FOREIGN KEY ("test_id") REFERENCES "test" ("test_id");

ALTER TABLE "result" ADD FOREIGN KEY ("parameter_name") REFERENCES "parameter" ("name");

ALTER TABLE "prescription" ADD FOREIGN KEY ("medication_name") REFERENCES "medication" ("name");

ALTER TABLE "prescription" ADD FOREIGN KEY ("doctor_id") REFERENCES "person" ("person_id");

ALTER TABLE "prescription" ADD FOREIGN KEY ("patient_id") REFERENCES "patient" ("patient_id");

ALTER TABLE "message_prescription" ADD FOREIGN KEY ("message_id") REFERENCES "message" ("message_id");

ALTER TABLE "message_prescription" ADD FOREIGN KEY ("prescription_id") REFERENCES "prescription" ("prescription_id");

ALTER TABLE "message" ADD FOREIGN KEY ("patient_id") REFERENCES "patient" ("patient_id");
