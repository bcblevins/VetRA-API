# API Endpoints

### PATIENT
| Method | Path                 | Description                 | Role       |
|--------|----------------------|-----------------------------|------------|
| GET    | /patients            | get all patients            | ALL        |
| GET    | /patients/:patientId | get patient by patientId    | ALL        |
| POST   | /patients            | create a patient            | DOCTOR     |
| PUT    | /patients/:patientId | update patient by patientId | DOCTOR     |
| DELETE | /patients/:patientId | delete patient by patientId | ADMIN ONLY |
| GET    | /patients/all        | get all patients            | DOCTOR     |

<details>
  <summary>JSON Format(s)</summary>

### Patient
```json
{
"patientId": "integer",
"firstName":"string",
"birthday":"string",
"species":"string",
"sex":"string",
"ownerUsername":"string"
}
```

</details>

### PATIENT:MESSAGE
| Method | Path                          | Description                      | Role |
|--------|-------------------------------|----------------------------------|------|
| GET    | /patients/:patientId/messages | get all messages about a patient | ALL  |
| POST   | /patients/:patientId/messages | create a message about a patient | ALL  |

<details>
  <summary>JSON Format(s)</summary>

### Message
```json
{
   "messageId": "integer",
   "subject":"string",
   "body":"string",
   "fromUsername":"string",
   "toUsername":"string",
   "testId": "integer",
   "patientId": "integer"
}
```

</details>

### PATIENT:TEST
| Method | Path                               | Description                      | Role       |
|--------|------------------------------------|----------------------------------|------------|
| GET    | /patients/:patientId/tests         | get all tests of patient         | ALL        |
| GET    | /patients/:patientId/tests/:testId | get test by testId of patient    | ALL        |
| DELETE | /patients/:patientId/tests/:testId | delete test by testId of patient | ADMIN ONLY |

<details>
  <summary>JSON Format(s)</summary>

### Test
```json
{
  "testId": "integer",
  "name": "string",
  "patientID": "integer",
  "doctorUsername": "string",
  "timestamp": "string"
}

```

</details>

### PATIENT:TEST:RESULT
| Method | Path                                                 | Description               | Role       |
|--------|------------------------------------------------------|---------------------------|------------|
| GET    | /patients/:patientId/tests/:testId/results           | get all results of test   | ALL        |
| GET    | /patients/:patientId/tests/:testId/results/:resultId | get single result of test | ALL        |
| DELETE | /patients/:patientId/tests/:testId/results/:resultId | delete result by resultId | ADMIN ONLY |

<details>
  <summary>JSON Format(s)</summary>

### Result
```json
{
  "resultID": 1,
  "testID": 1,
  "resultValue": "9.3",
  "parameterName": "WBC",
  "rangeLow": "4",
  "rangeHigh": "15.5",
  "unit": "10^3/mcL"
}

```

</details>

### PATIENT:TEST:MESSAGE
| Method | Path                                        | Description                                    | Role |
|--------|---------------------------------------------|------------------------------------------------|------|
| GET    | /patients/:patientId/tests/:testId/messages | get all messages of test of patient            | ALL  |
| POST   | /patients/:patientId/tests/:testId/messages | create a message in context of test of patient | ALL  |

<details>
  <summary>JSON Format(s)</summary>

### Message
```json
{
    "messageId": "integer",
    "subject":"string",
    "body":"string",
    "fromUsername":"string",
    "toUsername":"string", 
    "testId": "integer",
    "patientId": "integer"
}
```

</details>

### PATIENT:PRESCRIPTION
| Method | Path                                               | Description                                      | Role   |
|--------|----------------------------------------------------|--------------------------------------------------|--------|
| GET    | /patients/:patientId/prescriptions                 | get all prescriptions of patient                 | ALL    |
| GET    | /patients/:patientId/prescriptions/:prescriptionId | get prescription by prescriptionId of patient    | ALL    |
| POST   | /patients/:patientId/prescriptions                 | create a prescription of patient                 | DOCTOR |
| PUT    | /patients/:patientId/prescriptions/:prescriptionId | update prescription by prescriptionId of patient | DOCTOR |
| DELETE | /patients/:patientId/prescriptions/:prescriptionId | delete prescription by prescriptionId of patient | DOCTOR |

<details>
  <summary>JSON Format(s)</summary>

### Prescription
```json
{
    "prescriptionId": "integer",
    "name": "string",
    "quantity": "integer",
    "unit": "string",
    "instructions": "string",
    "refills": "integer",
    "patientId": "integer",
    "doctorUsername": "string",
    "active": "boolean"
}

```

</details>

### PATIENT:PRESCRIPTION:REQUEST
| Method | Path                                                                   | Description                                                | Role |
|--------|------------------------------------------------------------------------|------------------------------------------------------------|------|
| GET    | /patients/:patientId/prescriptions/:prescriptionId/requests            | get all refill requests of prescription of patient         | ALL  |
| GET    | /patients/:patientId/prescriptions/:prescriptionId/requests/:requestId | get refill request by requestId of prescription of patient | ALL  |
| POST   | /patients/:patientId/prescriptions/:prescriptionId/requests            | create a refill request of prescription of patient         | ALL  |

<details>
  <summary>JSON Format(s)</summary>

### Request
```json
{
    "requestId": int,
    "prescriptionId": int,
    "status": "string",
    "requestDate": "string"
}

```

</details>

### REQUEST
| Method | Path                 | Description                        | Role       |
|--------|----------------------|------------------------------------|------------|
| GET    | /requests            | get all refill requests            | DOCTOR     |
| GET    | /requests/:requestId | get refill request by requestId    | DOCTOR     |
| POST   | /requests            | create a refill request            | DOCTOR     |
| PUT    | /requests/:requestId | update refill request by requestId | DOCTOR     |
| DELETE | /requests/:requestId | delete refill request by requestId | ADMIN ONLY |


<details>
  <summary>JSON Format(s)</summary>

### Request with Prescription information
```json
{
    "requestId": "integer",
    "prescriptionId": "integer",
    "patientId": "integer",
    "status": "string",
    "requestDate": "string",
    "name": "string",
    "quantity": "number",
    "instructions": "string",
    "refills": "integer",
    "doctorUsername": "string",
    "active": "boolean"
}


```

</details>

### MESSAGE
| Method | Path                     | Description                                 | Role       |
|--------|--------------------------|---------------------------------------------|------------|
| GET    | /messages                | get all messages sent by or to user         | ALL        |
| GET    | /messages/:messageId     | get message by messageId sent by or to user | ALL        |
| PUT    | /messages/all/:messageId | update message by messageId                 | DOCTOR     |
| DELETE | /messages/all/:messageId | delete message by messageId                 | ADMIN ONLY |
| GET    | /messages/all            | get all messages                            | ADMIN ONLY |
| GET    | /messages/all/:messageId | get message by messageId                    | ADMIN ONLY |

<details>
  <summary>JSON Format(s)</summary>

### Message
```json
{
    "messageId": "integer",
    "subject":"string",
    "body":"string",
    "fromUsername":"string",
    "toUsername":"string", 
    "testId": "integer",
    "patientId": "integer"
}
```

</details>

### USER
| Method | Path                         | Description                            | Role       |
|--------|------------------------------|----------------------------------------|------------|
| GET    | /users                       | get all users                          | DOCTOR     |
| GET    | /users/:username             | get user by username                   | DOCTOR     |
| POST   | /users                       | create a user                          | ADMIN ONLY |
| PUT    | /users/:username             | update user by username (not password) | ADMIN ONLY |
| DELETE | /users/:username             | delete user by username                | ADMIN ONLY |
| GET    | /users/:username/roles       | get roles of user by username          | DOCTOR     |
| POST   | /users/:username/roles       | add role to user by username           | ADMIN ONLY |
| DELETE | /users/:username/roles/:role | delete role from user by username      | ADMIN ONLY |
| PUT    | /password                    | change own password                    | ALL        |
| PUT    | /users/:username/password    | change password of user by username    | ADMIN ONLY |

<details>
  <summary>JSON Format(s)</summary>

### User
```json
{
    "username": "string",
    "password": "string",
    "firstName": "string",
    "lastName": "string",
    "email": "string"
}
```
### Password
```json
string
```

</details>