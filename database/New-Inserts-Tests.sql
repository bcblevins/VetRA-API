--
-- PostgreSQL database dump
--

-- Dumped from database version 12.8
-- Dumped by pg_dump version 12.8

-- Started on 2024-07-04 11:27:06

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
-- TOC entry 2930 (class 0 OID 35526)
-- Dependencies: 212
-- Data for Name: medication; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.medication (name, unit) FROM stdin;
Trazodone 50mg	tablets
Gabapentin 100mg	capsules
\.


--
-- TOC entry 2920 (class 0 OID 35441)
-- Dependencies: 202
-- Data for Name: user; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public."user" (username, password, first_name, last_name, email) FROM stdin;
bblevins96	$2a$10$rdrt3j7YkAaVTQJcGnPX.ORrpMZ3ZXUMZqhfx0jR68vLaqB2jvsH2	Beau	Blevins	notreal@test.com
cakelly4	$2a$10$rdrt3j7YkAaVTQJcGnPX.ORrpMZ3ZXUMZqhfx0jR68vLaqB2jvsH2	Chris	Kelly	itsmybossimnotgivingyouhisemail@shameonyou.com
admin	$2a$10$o5y4WbVoawMUwZiTnQINJOAm6QJyOE3dD2KYIE1kkze7O0m6PzqA.	admin	admin	company@info.com
\.


--
-- TOC entry 2924 (class 0 OID 35471)
-- Dependencies: 206
-- Data for Name: patient; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.patient (patient_id, first_name, birthday, species, sex, owner_username) FROM stdin;
1	Charlie	2015-03-14	Canine	SF	bblevins96
2	Sunny	2016-02-20	Feline	CM	bblevins96
3	Arlo	2020-02-20	Feline	CM	bblevins96
\.


--
-- TOC entry 2927 (class 0 OID 35494)
-- Dependencies: 209
-- Data for Name: test; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.test (test_id, name, time_stamp, patient_id, doctor_username) FROM stdin;
1	CBC	2024-05-24 00:00:00	1	cakelly4
2	Fecal	2024-07-04 11:09:34.905699	2	cakelly4
3	CBC	2024-07-04 11:10:30.847789	1	cakelly4
4	Fecal	2024-07-04 11:10:30.847789	1	cakelly4
5	Fecal	2024-07-04 11:10:30.847789	1	cakelly4
6	Fecal	2024-07-04 11:10:30.847789	2	cakelly4
\.


--
-- TOC entry 2934 (class 0 OID 35560)
-- Dependencies: 216
-- Data for Name: message; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.message (message_id, subject, body, from_username, to_username, test_id, patient_id) FROM stdin;
1	Looks great!	Charlie's labwork looks great, her white blood cell count is back in the normal range. How is she doing after her visit?	cakelly4	bblevins96	1	1
2	Charlie learned to drive	Please help, Charlie has kidnapped me and is driving me to the treat store. I haven't told her yet that that doesn't exist.	cakelly4	cakelly4	\N	1
3	Recheck in 2 weeks	Charlie's liver values are still elevated. We should recheck in 2 weeks to see if they are improving.	cakelly4	bblevins96	1	1
4	Lab Results	The latest lab results for Charlie are in. Liver values have improved and the rest of her bloodwork looks great.	cakelly4	bblevins96	\N	1
5	Dietary Recommendations	Charlie's weight is up a little bit. I recommend cutting back on her food by 1/4 cup per day. Let me know if you have any questions.	cakelly4	bblevins96	\N	1
\.


--
-- TOC entry 2937 (class 0 OID 35604)
-- Dependencies: 219
-- Data for Name: meta; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.meta (action, performed_at) FROM stdin;
ezyvet patients updated	1970-01-01 00:00:00
ezyvet tests updated	1970-01-01 00:00:00
\.


--
-- TOC entry 2925 (class 0 OID 35482)
-- Dependencies: 207
-- Data for Name: patient_vms; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.patient_vms (patient_id, vms_name, vms_id) FROM stdin;
\.


--
-- TOC entry 2932 (class 0 OID 35533)
-- Dependencies: 214
-- Data for Name: prescription; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.prescription (prescription_id, quantity, instructions, refills, is_active, patient_id, medication_name, doctor_username) FROM stdin;
1	10	Give 1/2 tablet by mouth 3 hours prior to thunderstorms to reduce anxiety.	0	t	1	Trazodone 50mg	cakelly4
2	30	Give 1/2 to 1 capsule by mouth twice daily or as needed to reduce anxiety.	0	t	2	Gabapentin 100mg	cakelly4
\.


--
-- TOC entry 2936 (class 0 OID 35591)
-- Dependencies: 218
-- Data for Name: request; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.request (request_id, prescription_id, status, request_date) FROM stdin;
\.


--
-- TOC entry 2929 (class 0 OID 35512)
-- Dependencies: 211
-- Data for Name: result; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.result (result_id, test_id, result_value, parameter_name, range_low, range_high, unit) FROM stdin;
1	1	9.3	WBC	4	15.5	10^3/mcL
2	1	8.0	RBC	4.8	9.3	10^6/mcL
3	1	20.3	HGB	12.1	20.3	g/dl
4	1	54.0	HCT	36	60	%
5	1	67.0	MCV	58	79	fL
6	1	330	PLT	170	400	10^3/mcL
7	2	Negative	Hookworms	\N	\N	\N
8	2	Negative	Roundworms	\N	\N	\N
9	2	Positive	Whipworms	\N	\N	\N
10	2	Positive	Tapeworms	\N	\N	\N
11	3	14.8	WBC	4.0	15.5	10^3/uL
12	3	5.27	RBC	4.5	8.25	10^6/uL
13	3	18.4	Hemoglobin	11.9	18.9	g/dL
14	3	60	Hematocrit	36	60	%
15	3	63	MCV	58	79	fL
16	3	212	Platelets	170	400	10^3/uL
17	4	Positive	Hookworms	\N	\N	\N
18	4	Negative	Roundworms	\N	\N	\N
19	4	Negative	Whipworms	\N	\N	\N
20	4	Negative	Tapeworms	\N	\N	\N
21	5	Positive	Hookworms	\N	\N	\N
22	5	Negative	Roundworms	\N	\N	\N
23	5	Negative	Whipworms	\N	\N	\N
24	5	Negative	Tapeworms	\N	\N	\N
25	6	Negative	Hookworms	\N	\N	\N
26	6	Negative	Roundworms	\N	\N	\N
27	6	Positive	Whipworms	\N	\N	\N
28	6	Negative	Tapeworms	\N	\N	\N
\.


--
-- TOC entry 2921 (class 0 OID 35449)
-- Dependencies: 203
-- Data for Name: role; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.role (username, role) FROM stdin;
bblevins96	OWNER
cakelly4	DOCTOR
admin	ADMIN
\.


--
-- TOC entry 2922 (class 0 OID 35459)
-- Dependencies: 204
-- Data for Name: user_vms; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.user_vms (username, vms_name, vms_id) FROM stdin;
\.


--
-- TOC entry 2943 (class 0 OID 0)
-- Dependencies: 215
-- Name: message_message_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.message_message_id_seq', 5, true);


--
-- TOC entry 2944 (class 0 OID 0)
-- Dependencies: 205
-- Name: patient_patient_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.patient_patient_id_seq', 3, true);


--
-- TOC entry 2945 (class 0 OID 0)
-- Dependencies: 213
-- Name: prescription_prescription_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.prescription_prescription_id_seq', 2, true);


--
-- TOC entry 2946 (class 0 OID 0)
-- Dependencies: 217
-- Name: request_request_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.request_request_id_seq', 1, false);


--
-- TOC entry 2947 (class 0 OID 0)
-- Dependencies: 210
-- Name: result_result_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.result_result_id_seq', 28, true);


--
-- TOC entry 2948 (class 0 OID 0)
-- Dependencies: 208
-- Name: test_test_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.test_test_id_seq', 6, true);


-- Completed on 2024-07-04 11:27:06

--
-- PostgreSQL database dump complete
--

