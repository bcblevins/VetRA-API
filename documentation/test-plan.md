### PATIENT

| Method | Path                 | Test                                                                                                 |
|--------|----------------------|------------------------------------------------------------------------------------------------------|
| GET    | /patients            | Sending request to this path should return all patients owned by user                                |
| GET    | /patients/:patientId | Sending request to this path should return patient of indicated ID (if exists)                       |
| POST   | /patients            | Sending request with patient information in body should add patient to patient table.                |
| PUT    | /patients/:patientId | Sending request with patient information in body should update patient information to match request. |
| DELETE | /patients/:patientId | Sending request should delete patient of indicated ID from table                                     |
| GET    | /patients/all        | Sending request should return all patients in table                                                  |

### PATIENT:MESSAGE

| Method | Path                          | Test                                                                                                           |
|--------|-------------------------------|----------------------------------------------------------------------------------------------------------------|
| GET    | /patients/:patientId/messages | Sending request should return all messages about a patient of indicated ID                                     |
| POST   | /patients/:patientId/messages | Sending request with message information in the body should add message to table with patient ID matching path |

### PATIENT:TEST

| Method | Path                               | Test                                                                                                      |
|--------|------------------------------------|-----------------------------------------------------------------------------------------------------------|
| GET    | /patients/:patientId/tests         | Sending request should return all tests for patient of indicated ID                                       |
| GET    | /patients/:patientId/tests/:testId | Sending request should return test of indicated ID if it exists for indicated patient ID                  |
| DELETE | /patients/:patientId/tests/:testId | Sending request should delete test and results of indicated test ID if it exists for indicated patient ID |

### PATIENT:TEST:RESULT

| Method | Path                                                 | Test                                                                                                 |
|--------|------------------------------------------------------|------------------------------------------------------------------------------------------------------|
| GET    | /patients/:patientId/tests/:testId/results           | Sending request should return all results for indicated test ID if exists for indicated patient ID   |
| GET    | /patients/:patientId/tests/:testId/results/:resultId | Sending request should return single result for indicated test ID if exists for indicated patient ID |
| DELETE | /patients/:patientId/tests/:testId/results/:resultId | Sending request should delete result by indicated result ID                                          |

### PATIENT:TEST:MESSAGE

| Method | Path                                        | Test                                                                                                                         |
|--------|---------------------------------------------|------------------------------------------------------------------------------------------------------------------------------|
| GET    | /patients/:patientId/tests/:testId/messages | Sending request should return all messages for indicated test ID                                                             |
| POST   | /patients/:patientId/tests/:testId/messages | Sending request with message information with the body should add message to message table with matching patient and test ID |

### PATIENT:PRESCRIPTION

| Method | Path                                               | Test                                                                                                                                  |
|--------|----------------------------------------------------|---------------------------------------------------------------------------------------------------------------------------------------|
| GET    | /patients/:patientId/prescriptions                 | Sending request should return all prescriptions for indicated patient ID                                                              |
| GET    | /patients/:patientId/prescriptions/:prescriptionId | Sending request should return single prescription for indicated patient ID                                                            |
| POST   | /patients/:patientId/prescriptions                 | Sending request with prescription information in body should add prescription to prescription table                                   |
| PUT    | /patients/:patientId/prescriptions/:prescriptionId | Sending request with prescription information in body should update prescription of indicated ID to match the information in the body |
| DELETE | /patients/:patientId/prescriptions/:prescriptionId | Sending request should delete prescription of indicated ID from table                                                                 |

### PATIENT:PRESCRIPTION:REQUEST

| Method | Path                                                                   | Test                                                                                 |
|--------|------------------------------------------------------------------------|--------------------------------------------------------------------------------------|
| GET    | /patients/:patientId/prescriptions/:prescriptionId/requests            | Sending request should return all "request"s for a given prescription                |
| GET    | /patients/:patientId/prescriptions/:prescriptionId/requests/:requestId | Sending request should return a single "request" for a given request ID              |
| POST   | /patients/:patientId/prescriptions/:prescriptionId/requests            | Sending request with "request" information in the body should add "request" to table |

### REQUEST

| Method | Path                 | Test                                                                                                |
|--------|----------------------|-----------------------------------------------------------------------------------------------------|
| GET    | /requests            | Sending request should return all "requests"                                                        |
| GET    | /requests/:requestId | Sending request should return single "request" by ID                                                |
| POST   | /requests            | Sending request with "request" information in the body should add "request" to table                |
| PUT    | /requests/:requestId | Sending request with "request" information in the body should update "request" of given ID to match |
| DELETE | /requests/:requestId | Sending request should delete "request" of given ID from table                                      |

### MESSAGE

| Method | Path                     | Test                                                                                            |
|--------|--------------------------|-------------------------------------------------------------------------------------------------|
| GET    | /messages                | Sending request should return all messages send to or from user                                 |
| GET    | /messages/:messageId     | Sending request should return single message of given message ID if sent to or from user        |
| PUT    | /messages/all/:messageId | Sending request with message information in the body should update message of given ID to match |
| DELETE | /messages/all/:messageId | Sending request should delete message of given ID from table                                    |
| GET    | /messages/all            | Sending request should return all messages                                                      |
| GET    | /messages/all/:messageId | Sending request should return single message by ID                                              |

### USER

| Method | Path                         | Test                                                                                            |
|--------|------------------------------|-------------------------------------------------------------------------------------------------|
| GET    | /users                       | Sending request should return all users                                                         |
| GET    | /users/:username             | Sending request should return user by username                                                  |
| POST   | /users                       | Sending request with user information in the body should add a user to the table                |
| PUT    | /users/:username             | Sending request with user information in the body should update user by username (not password) |
| DELETE | /users/:username             | Sending request should delete user by username                                                  |
| GET    | /users/:username/roles       | Sending request should return roles of user by username                                         |
| POST   | /users/:username/roles       | Sending request should add role to role table by username                                       |
| DELETE | /users/:username/roles/:role | Sending request should delete role from role table by username                                  |
| PUT    | /password                    | Sending request should change users own password                                                |
| PUT    | /users/:username/password    | Sending request should change password of user by username                                      |

