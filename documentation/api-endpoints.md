### PATIENT
| Method | Path                 | Description                 | Role       | Codes |
|--------|----------------------|-----------------------------|------------|-------|
| GET    | /patients            | get all patients            | ALL        |       |
| GET    | /patients/:patientId | get patient by patientId    | ALL        |       |
| POST   | /patients            | create a patient            | DOCTOR     |       |
| PUT    | /patients/:patientId | update patient by patientId | DOCTOR     |       |
| DELETE | /patients/:patientId | delete patient by patientId | ADMIN ONLY |       |
| GET    | /patients/all        | get all patients            | DOCTOR     |       |

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
| Method | Path                          | Description                      | Role | Codes |
|--------|-------------------------------|----------------------------------|------|-------|
| GET    | /patients/:patientId/messages | get all messages about a patient | ALL  |       |
| POST   | /patients/:patientId/messages | create a message about a patient | ALL  |       |

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
| Method | Path                               | Description                      | Role       | Codes |
|--------|------------------------------------|----------------------------------|------------|-------|
| GET    | /patients/:patientId/tests         | get all tests of patient         | ALL        |       |
| GET    | /patients/:patientId/tests/:testId | get test by testId of patient    | ALL        |       |
| DELETE | /patients/:patientId/tests/:testId | delete test by testId of patient | ADMIN ONLY |       |

<details>
  <summary>JSON Format(s)</summary>

### Test
```json
{
  "testId": "integer",
  "name": "string",
  "patientID": "integer",
  "doctorUsername": "string",
  "results": [
    {
      "resultID": "integer",
      "testID": "integer",
      "name": "string",
      "resultValue": "string",
      "rangeLow": "string",
      "rangeHigh": "string",
      "unit": "string",
      "qualitativeNormal": "null or string",
      "qualitative": "boolean"
    },
    ...
  ],
  "timestamp": "string"
}

```

</details>

### PATIENT:TEST:MESSAGE
| Method | Path                                        | Description                                    | Role | Codes |
|--------|---------------------------------------------|------------------------------------------------|------|-------|
| GET    | /patients/:patientId/tests/:testId/messages | get all messages of test of patient            | ALL  |       |
| POST   | /patients/:patientId/tests/:testId/messages | create a message in context of test of patient | ALL  |       |

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
| Method | Path                                               | Description                                      | Role   | Codes |
|--------|----------------------------------------------------|--------------------------------------------------|--------|-------|
| GET    | /patients/:patientId/prescriptions                 | get all prescriptions of patient                 | ALL    |       |
| GET    | /patients/:patientId/prescriptions/:prescriptionId | get prescription by prescriptionId of patient    | ALL    |       |
| POST   | /patients/:patientId/prescriptions                 | create a prescription of patient                 | DOCTOR |       |
| PUT    | /patients/:patientId/prescriptions/:prescriptionId | update prescription by prescriptionId of patient | DOCTOR |       |
| DELETE | /patients/:patientId/prescriptions/:prescriptionId | delete prescription by prescriptionId of patient | DOCTOR |       |

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
| Method | Path                                                                   | Description                                                | Role | Codes |
|--------|------------------------------------------------------------------------|------------------------------------------------------------|------|-------|
| GET    | /patients/:patientId/prescriptions/:prescriptionId/requests            | get all refill requests of prescription of patient         | ALL  |       |
| GET    | /patients/:patientId/prescriptions/:prescriptionId/requests/:requestId | get refill request by requestId of prescription of patient | ALL  |       |
| POST   | /patients/:patientId/prescriptions/:prescriptionId/requests            | create a refill request of prescription of patient         | ALL  |       |

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
| Method | Path                 | Description                        | Role       | Codes |
|--------|----------------------|------------------------------------|------------|-------|
| GET    | /requests            | get all refill requests            | DOCTOR     |       |
| GET    | /requests/:requestId | get refill request by requestId    | DOCTOR     |       |
| POST   | /requests            | create a refill request            | DOCTOR     |       |
| PUT    | /requests/:requestId | update refill request by requestId | DOCTOR     |       |
| DELETE | /requests/:requestId | delete refill request by requestId | ADMIN ONLY |       |


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
| Method | Path                    | Description                 | Role       | Codes |
|--------|-------------------------|-----------------------------|------------|-------|
| GET    | /messages               | get all messages            | ALL        |       |
| GET    | /messages/:messageId    | get message by messageId    | ALL        |       |
| PUT    | /messages/:messageId    | update message by messageId | DOCTOR     |       |
| DELETE | /messages/:messageId    | delete message by messageId | ADMIN ONLY |       |
| GET    | /allMessages            | get all messages            | ADMIN ONLY |       |
| GET    | /allMessages/:messageId | get message by messageId    | ADMIN ONLY |       |

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
| Method | Path                      | Description                            | Role       | Codes |
|--------|---------------------------|----------------------------------------|------------|-------|
| GET    | /users                    | get all users                          | DOCTOR     |       |
| GET    | /users/:username          | get user by username                   | DOCTOR     |       |
| POST   | /users                    | create a user                          | ADMIN ONLY |       |
| PUT    | /users/:username          | update user by username (not password) | ADMIN ONLY |       |
| DELETE | /users/:username          | delete user by username                | ADMIN ONLY |       |
| GET    | /users/:username/roles    | get roles of user by username          | DOCTOR     |       |
| POST   | /users/:username/roles    | add role to user by username           | ADMIN ONLY |       |
| DELETE | /users/:username/roles    | delete role from user by username      | ADMIN ONLY |       |
| PUT    | /password                 | change own password                    | ALL        |       |
| PUT    | /users/:username/password | change password of user by username    | ADMIN ONLY |       |

<details>
  <summary>JSON Format(s)</summary>

### User
```json
{
    "username": "string",
    "password": "string",
    "firstName": "string",
    "lastName": "string"
}
```
### Password
```json
string
```

</details>