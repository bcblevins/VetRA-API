As an authenticated user, I should be able to:

| View                                                   | Role          |
|--------------------------------------------------------|---------------|
| all users                                              | DOCTOR        |
| one user                                               | DOCTOR, OWNER |
| all pets for a specific owner                          | DOCTOR, OWNER |
| one pet for a specific owner                           | DOCTOR, OWNER |
| all tests for a specific pet                           | DOCTOR, OWNER |
| one test for a specific pet                            | DOCTOR, OWNER |
| all prescriptions for a specific pet                   | DOCTOR, OWNER |
| one prescription for a specific pet                    | DOCTOR, OWNER |
| all refill requests                                    | DOCTOR        |
| one refill request                                     | DOCTOR        |
| messages in the context of a test                      | DOCTOR, OWNER |
| all messages that I have sent, or have been sent to me | DOCTOR, OWNER |
| one message that I have sent, or has been sent to me   | DOCTOR, OWNER |

| Create                                                 | Role          | 
|--------------------------------------------------------|---------------|
| messages to a specific doctor                          | OWNER         |
| messages in the context of a test                      | DOCTOR, OWNER |
| a prescription refill request                          | OWNER         |
| a new user                                             | ADMIN ONLY    |
| a new pet for a specific user                          | DOCTOR        |
| a new prescription for a specific pet                  | DOCTOR        |

| Update                                                 | Role          |
|--------------------------------------------------------|---------------|
| a user (self)                                          | DOCTOR, OWNER |
| a user (any)                                           | ADMIN ONLY    |
| a pet for a specific owner                             | DOCTOR        |
| a prescription for a specific pet                      | DOCTOR        |
| a message                                              | ADMIN ONLY    |
| a refill request to approved                           | DOCTOR        |
| a refill request to denied                             | DOCTOR        |
| a refill request to pending                            | ADMIN ONLY    |

| Delete                                                 | Role          |
|--------------------------------------------------------|---------------|
| a user                                                 | ADMIN ONLY    |
| a pet for a specific owner                             | ADMIN ONLY    |
| a prescription for a specific pet                      | ADMIN ONLY    |
| a test for a specific pet                              | ADMIN ONLY    |
| a refill request                                       | ADMIN ONLY    |
| a message                                              | ADMIN ONLY    |