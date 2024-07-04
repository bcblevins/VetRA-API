--
-- PostgreSQL database dump
--

-- Dumped from database version 16.2
-- Dumped by pg_dump version 16.2

-- Started on 2024-07-04 08:43:35

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- TOC entry 4907 (class 0 OID 30255)
-- Dependencies: 223
-- Data for Name: medication; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.medication (name, unit) VALUES ('Trazodone 50mg', 'tablets');
INSERT INTO public.medication (name, unit) VALUES ('Gabapentin 100mg', 'capsules');


--
-- TOC entry 4899 (class 0 OID 30195)
-- Dependencies: 215
-- Data for Name: user; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public."user" (username, password, first_name, last_name, email) VALUES ('bblevins96', '$2a$10$rdrt3j7YkAaVTQJcGnPX.ORrpMZ3ZXUMZqhfx0jR68vLaqB2jvsH2', 'Beau', 'Blevins', 'notreal@test.com');
INSERT INTO public."user" (username, password, first_name, last_name, email) VALUES ('cakelly4', '$2a$10$rdrt3j7YkAaVTQJcGnPX.ORrpMZ3ZXUMZqhfx0jR68vLaqB2jvsH2', 'Chris', 'Kelly', 'itsmybossimnotgivingyouhisemail@shameonyou.com');
INSERT INTO public."user" (username, password, first_name, last_name, email) VALUES ('admin', '$2a$10$o5y4WbVoawMUwZiTnQINJOAm6QJyOE3dD2KYIE1kkze7O0m6PzqA.', 'admin', 'admin', 'company@info.com');


--
-- TOC entry 4901 (class 0 OID 30203)
-- Dependencies: 217
-- Data for Name: patient; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.patient (patient_id, first_name, birthday, species, sex, owner_username) VALUES (1, 'Charlie', '2015-03-14', 'Canine', 'SF', 'bblevins96');
INSERT INTO public.patient (patient_id, first_name, birthday, species, sex, owner_username) VALUES (2, 'Sunny', '2016-02-20', 'Feline', 'CM', 'bblevins96');
INSERT INTO public.patient (patient_id, first_name, birthday, species, sex, owner_username) VALUES (3, 'Arlo', '2020-02-20', 'Feline', 'CM', 'bblevins96');


--
-- TOC entry 4904 (class 0 OID 30225)
-- Dependencies: 220
-- Data for Name: test; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.test (test_id, name, time_stamp, patient_id, doctor_username) VALUES (1, 'CBC', '2024-05-24 00:00:00', 1, 'cakelly4');
INSERT INTO public.test (test_id, name, time_stamp, patient_id, doctor_username) VALUES (2, 'Fecal', '2024-07-03 21:05:41.764148', 2, 'cakelly4');
INSERT INTO public.test (test_id, name, time_stamp, patient_id, doctor_username) VALUES (3, 'Fecal', '2024-07-03 21:05:41.764148', 2, 'cakelly4');
INSERT INTO public.test (test_id, name, time_stamp, patient_id, doctor_username) VALUES (4, 'Fecal', '2024-07-03 21:05:41.764148', 1, 'cakelly4');
INSERT INTO public.test (test_id, name, time_stamp, patient_id, doctor_username) VALUES (5, 'CBC', '2024-07-03 21:05:41.764148', 2, 'cakelly4');
INSERT INTO public.test (test_id, name, time_stamp, patient_id, doctor_username) VALUES (6, 'Fecal', '2024-07-03 21:05:41.764148', 1, 'cakelly4');
INSERT INTO public.test (test_id, name, time_stamp, patient_id, doctor_username) VALUES (7, 'CBC', '2024-07-03 21:05:41.764148', 2, 'cakelly4');
INSERT INTO public.test (test_id, name, time_stamp, patient_id, doctor_username) VALUES (8, 'CBC', '2024-07-03 21:05:41.764148', 1, 'cakelly4');
INSERT INTO public.test (test_id, name, time_stamp, patient_id, doctor_username) VALUES (9, 'Fecal', '2024-07-03 21:05:41.764148', 2, 'cakelly4');


--
-- TOC entry 4911 (class 0 OID 30286)
-- Dependencies: 227
-- Data for Name: message; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.message (message_id, subject, body, from_username, to_username, test_id, patient_id) VALUES (1, 'Looks great!', 'Charlie''s labwork looks great, her white blood cell count is back in the normal range. How is she doing after her visit?', 'cakelly4', 'bblevins96', 1, 1);
INSERT INTO public.message (message_id, subject, body, from_username, to_username, test_id, patient_id) VALUES (2, 'Charlie learned to drive', 'Please help, Charlie has kidnapped me and is driving me to the treat store. I haven''t told her yet that that doesn''t exist.', 'cakelly4', 'cakelly4', 1, 1);
INSERT INTO public.message (message_id, subject, body, from_username, to_username, test_id, patient_id) VALUES (3, 'Recheck in 2 weeks', 'Charlie''s liver values are still elevated. We should recheck in 2 weeks to see if they are improving.', 'cakelly4', 'bblevins96', 3, 1);
INSERT INTO public.message (message_id, subject, body, from_username, to_username, test_id, patient_id) VALUES (4, 'Lab Results', 'The latest lab results for Charlie are in. Liver values have improved and the rest of her bloodwork looks great.', 'cakelly4', 'bblevins96', 4, 1);
INSERT INTO public.message (message_id, subject, body, from_username, to_username, test_id, patient_id) VALUES (5, 'Dietary Recommendations', 'Charlie''s weight is up a little bit. I recommend cutting back on her food by 1/4 cup per day. Let me know if you have any questions.', 'cakelly4', 'bblevins96', 5, 1);


--
-- TOC entry 4909 (class 0 OID 30261)
-- Dependencies: 225
-- Data for Name: prescription; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.prescription (prescription_id, quantity, instructions, refills, is_active, patient_id, medication_name, doctor_username) VALUES (1, 10, 'Give 1/2 tablet by mouth 3 hours prior to thunderstorms to reduce anxiety.', 0, true, 1, 'Trazodone 50mg', 'cakelly4');
INSERT INTO public.prescription (prescription_id, quantity, instructions, refills, is_active, patient_id, medication_name, doctor_username) VALUES (2, 30, 'Give 1/2 to 1 capsule by mouth twice daily or as needed to reduce anxiety.', 0, true, 2, 'Gabapentin 100mg', 'cakelly4');


--
-- TOC entry 4913 (class 0 OID 30315)
-- Dependencies: 229
-- Data for Name: request; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.request (request_id, prescription_id, status, request_date) VALUES (1, 1, 'APPPROVED', '2024-05-14 14:23:34.448466');


--
-- TOC entry 4906 (class 0 OID 30242)
-- Dependencies: 222
-- Data for Name: result; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.result (result_id, test_id, result_value, parameter_name, range_low, range_high, unit) VALUES (1, 1, '9.3', 'WBC', '4', '15.5', '10^3/mcL');
INSERT INTO public.result (result_id, test_id, result_value, parameter_name, range_low, range_high, unit) VALUES (2, 1, '8.0', 'RBC', '4.8', '9.3', '10^6/mcL');
INSERT INTO public.result (result_id, test_id, result_value, parameter_name, range_low, range_high, unit) VALUES (3, 1, '20.3', 'HGB', '12.1', '20.3', 'g/dl');
INSERT INTO public.result (result_id, test_id, result_value, parameter_name, range_low, range_high, unit) VALUES (4, 1, '54.0', 'HCT', '36', '60', '%');
INSERT INTO public.result (result_id, test_id, result_value, parameter_name, range_low, range_high, unit) VALUES (5, 1, '67.0', 'MCV', '58', '79', 'fL');
INSERT INTO public.result (result_id, test_id, result_value, parameter_name, range_low, range_high, unit) VALUES (6, 1, '330', 'PLT', '170', '400', '10^3/mcL');
INSERT INTO public.result (result_id, test_id, result_value, parameter_name, range_low, range_high, unit) VALUES (7, 2, 'Negative', 'Hookworms', NULL, NULL, NULL);
INSERT INTO public.result (result_id, test_id, result_value, parameter_name, range_low, range_high, unit) VALUES (8, 2, 'Negative', 'Roundworms', NULL, NULL, NULL);
INSERT INTO public.result (result_id, test_id, result_value, parameter_name, range_low, range_high, unit) VALUES (9, 2, 'Negative', 'Whipworms', NULL, NULL, NULL);
INSERT INTO public.result (result_id, test_id, result_value, parameter_name, range_low, range_high, unit) VALUES (10, 2, 'Negative', 'Tapeworms', NULL, NULL, NULL);
INSERT INTO public.result (result_id, test_id, result_value, parameter_name, range_low, range_high, unit) VALUES (11, 3, 'Negative', 'Hookworms', NULL, NULL, NULL);
INSERT INTO public.result (result_id, test_id, result_value, parameter_name, range_low, range_high, unit) VALUES (12, 3, 'Positive', 'Roundworms', NULL, NULL, NULL);
INSERT INTO public.result (result_id, test_id, result_value, parameter_name, range_low, range_high, unit) VALUES (13, 3, 'Negative', 'Whipworms', NULL, NULL, NULL);
INSERT INTO public.result (result_id, test_id, result_value, parameter_name, range_low, range_high, unit) VALUES (14, 3, 'Negative', 'Tapeworms', NULL, NULL, NULL);
INSERT INTO public.result (result_id, test_id, result_value, parameter_name, range_low, range_high, unit) VALUES (15, 4, 'Negative', 'Hookworms', NULL, NULL, NULL);
INSERT INTO public.result (result_id, test_id, result_value, parameter_name, range_low, range_high, unit) VALUES (16, 4, 'Negative', 'Roundworms', NULL, NULL, NULL);
INSERT INTO public.result (result_id, test_id, result_value, parameter_name, range_low, range_high, unit) VALUES (17, 4, 'Negative', 'Whipworms', NULL, NULL, NULL);
INSERT INTO public.result (result_id, test_id, result_value, parameter_name, range_low, range_high, unit) VALUES (18, 4, 'Negative', 'Tapeworms', NULL, NULL, NULL);
INSERT INTO public.result (result_id, test_id, result_value, parameter_name, range_low, range_high, unit) VALUES (19, 5, '13.1', 'WBC', '4.0', '15.5', '10^3/uL');
INSERT INTO public.result (result_id, test_id, result_value, parameter_name, range_low, range_high, unit) VALUES (20, 5, '5.57', 'RBC', '4.5', '8.25', '10^6/uL');
INSERT INTO public.result (result_id, test_id, result_value, parameter_name, range_low, range_high, unit) VALUES (21, 5, '13.0', 'Hemoglobin', '11.9', '18.9', 'g/dL');
INSERT INTO public.result (result_id, test_id, result_value, parameter_name, range_low, range_high, unit) VALUES (22, 5, '49', 'Hematocrit', '36', '60', '%');
INSERT INTO public.result (result_id, test_id, result_value, parameter_name, range_low, range_high, unit) VALUES (23, 5, '72', 'MCV', '58', '79', 'fL');
INSERT INTO public.result (result_id, test_id, result_value, parameter_name, range_low, range_high, unit) VALUES (24, 5, '431', 'Platelets', '170', '400', '10^3/uL');
INSERT INTO public.result (result_id, test_id, result_value, parameter_name, range_low, range_high, unit) VALUES (25, 6, 'Positive', 'Hookworms', NULL, NULL, NULL);
INSERT INTO public.result (result_id, test_id, result_value, parameter_name, range_low, range_high, unit) VALUES (26, 6, 'Negative', 'Roundworms', NULL, NULL, NULL);
INSERT INTO public.result (result_id, test_id, result_value, parameter_name, range_low, range_high, unit) VALUES (27, 6, 'Negative', 'Whipworms', NULL, NULL, NULL);
INSERT INTO public.result (result_id, test_id, result_value, parameter_name, range_low, range_high, unit) VALUES (28, 6, 'Negative', 'Tapeworms', NULL, NULL, NULL);
INSERT INTO public.result (result_id, test_id, result_value, parameter_name, range_low, range_high, unit) VALUES (29, 7, '11.2', 'WBC', '4.0', '15.5', '10^3/uL');
INSERT INTO public.result (result_id, test_id, result_value, parameter_name, range_low, range_high, unit) VALUES (30, 7, '5.33', 'RBC', '4.5', '8.25', '10^6/uL');
INSERT INTO public.result (result_id, test_id, result_value, parameter_name, range_low, range_high, unit) VALUES (31, 7, '14.0', 'Hemoglobin', '11.9', '18.9', 'g/dL');
INSERT INTO public.result (result_id, test_id, result_value, parameter_name, range_low, range_high, unit) VALUES (32, 7, '68', 'Hematocrit', '36', '60', '%');
INSERT INTO public.result (result_id, test_id, result_value, parameter_name, range_low, range_high, unit) VALUES (33, 7, '81', 'MCV', '58', '79', 'fL');
INSERT INTO public.result (result_id, test_id, result_value, parameter_name, range_low, range_high, unit) VALUES (34, 7, '709', 'Platelets', '170', '400', '10^3/uL');
INSERT INTO public.result (result_id, test_id, result_value, parameter_name, range_low, range_high, unit) VALUES (35, 8, '5.5', 'WBC', '4.0', '15.5', '10^3/uL');
INSERT INTO public.result (result_id, test_id, result_value, parameter_name, range_low, range_high, unit) VALUES (36, 8, '8.1', 'RBC', '4.5', '8.25', '10^6/uL');
INSERT INTO public.result (result_id, test_id, result_value, parameter_name, range_low, range_high, unit) VALUES (37, 8, '18.6', 'Hemoglobin', '11.9', '18.9', 'g/dL');
INSERT INTO public.result (result_id, test_id, result_value, parameter_name, range_low, range_high, unit) VALUES (38, 8, '27', 'Hematocrit', '36', '60', '%');
INSERT INTO public.result (result_id, test_id, result_value, parameter_name, range_low, range_high, unit) VALUES (39, 8, '65', 'MCV', '58', '79', 'fL');
INSERT INTO public.result (result_id, test_id, result_value, parameter_name, range_low, range_high, unit) VALUES (40, 8, '506', 'Platelets', '170', '400', '10^3/uL');
INSERT INTO public.result (result_id, test_id, result_value, parameter_name, range_low, range_high, unit) VALUES (41, 9, 'Negative', 'Hookworms', NULL, NULL, NULL);
INSERT INTO public.result (result_id, test_id, result_value, parameter_name, range_low, range_high, unit) VALUES (42, 9, 'Negative', 'Roundworms', NULL, NULL, NULL);
INSERT INTO public.result (result_id, test_id, result_value, parameter_name, range_low, range_high, unit) VALUES (43, 9, 'Positive', 'Whipworms', NULL, NULL, NULL);
INSERT INTO public.result (result_id, test_id, result_value, parameter_name, range_low, range_high, unit) VALUES (44, 9, 'Negative', 'Tapeworms', NULL, NULL, NULL);


--
-- TOC entry 4902 (class 0 OID 30214)
-- Dependencies: 218
-- Data for Name: role; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.role (username, role) VALUES ('bblevins96', 'OWNER');
INSERT INTO public.role (username, role) VALUES ('cakelly4', 'DOCTOR');
INSERT INTO public.role (username, role) VALUES ('admin', 'ADMIN');


--
-- TOC entry 4919 (class 0 OID 0)
-- Dependencies: 226
-- Name: message_message_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.message_message_id_seq', 5, true);


--
-- TOC entry 4920 (class 0 OID 0)
-- Dependencies: 216
-- Name: patient_patient_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.patient_patient_id_seq', 4, true);


--
-- TOC entry 4921 (class 0 OID 0)
-- Dependencies: 224
-- Name: prescription_prescription_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.prescription_prescription_id_seq', 2, true);


--
-- TOC entry 4922 (class 0 OID 0)
-- Dependencies: 228
-- Name: request_request_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.request_request_id_seq', 1, true);


--
-- TOC entry 4923 (class 0 OID 0)
-- Dependencies: 221
-- Name: result_result_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.result_result_id_seq', 44, true);


--
-- TOC entry 4924 (class 0 OID 0)
-- Dependencies: 219
-- Name: test_test_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.test_test_id_seq', 9, true);


-- Completed on 2024-07-04 08:43:35

--
-- PostgreSQL database dump complete
--

