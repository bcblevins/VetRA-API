### PATIENT
| Method | Path                 | Expected Outcome |
|--------|----------------------|------------------|
| GET    | /patients            |                  |
| GET    | /patients/:patientId |                  |
| POST   | /patients            |                  |
| PUT    | /patients/:patientId |                  |
| DELETE | /patients/:patientId |                  |
| GET    | /patients/all        |                  |


### PATIENT:MESSAGE
| Method | Path                          | Expected Outcome |
|--------|-------------------------------|------------------|
| GET    | /patients/:patientId/messages |                  |
| POST   | /patients/:patientId/messages |                  |


### PATIENT:TEST
| Method | Path                               | Expected Outcome |
|--------|------------------------------------|------------------|
| GET    | /patients/:patientId/tests         |                  |
| GET    | /patients/:patientId/tests/:testId |                  |
| DELETE | /patients/:patientId/tests/:testId |                  |


### PATIENT:TEST:MESSAGE
| Method | Path                                        | Expected Outcome |
|--------|---------------------------------------------|------------------|
| GET    | /patients/:patientId/tests/:testId/messages |                  |
| POST   | /patients/:patientId/tests/:testId/messages |                  |


### PATIENT:PRESCRIPTION
| Method | Path                                               | Expected Outcome |
|--------|----------------------------------------------------|------------------|
| GET    | /patients/:patientId/prescriptions                 |                  |
| GET    | /patients/:patientId/prescriptions/:prescriptionId |                  |
| POST   | /patients/:patientId/prescriptions                 |                  |
| PUT    | /patients/:patientId/prescriptions/:prescriptionId |                  |
| DELETE | /patients/:patientId/prescriptions/:prescriptionId |                  |


### PATIENT:PRESCRIPTION:REQUEST
| Method | Path                                                                   | Expected Outcome |
|--------|------------------------------------------------------------------------|------------------|
| GET    | /patients/:patientId/prescriptions/:prescriptionId/requests            |                  |
| GET    | /patients/:patientId/prescriptions/:prescriptionId/requests/:requestId |                  |
| POST   | /patients/:patientId/prescriptions/:prescriptionId/requests            |                  |



### REQUEST
| Method | Path                 | Expected Outcome |
|--------|----------------------|------------------|
| GET    | /requests            |                  |
| GET    | /requests/:requestId |                  |
| POST   | /requests            |                  |
| PUT    | /requests/:requestId |                  |
| DELETE | /requests/:requestId |                  |




### MESSAGE
| Method | Path                    | Expected Outcome |
|--------|-------------------------|------------------|
| GET    | /messages               |                  |
| GET    | /messages/:messageId    |                  |
| PUT    | /messages/:messageId    |                  |
| DELETE | /messages/:messageId    |                  |
| GET    | /allMessages            |                  |
| GET    | /allMessages/:messageId |                  |



### USER
| Method | Path                      | Expected Outcome |
|--------|---------------------------|------------------|
| GET    | /users                    |                  |
| GET    | /users/:username          |                  |
| POST   | /users                    |                  |
| PUT    | /users/:username          |                  |
| DELETE | /users/:username          |                  |
| GET    | /users/:username/roles    |                  |
| POST   | /users/:username/roles    |                  |
| DELETE | /users/:username/roles    |                  |
| PUT    | /password                 |                  |
| PUT    | /users/:username/password |                  |

