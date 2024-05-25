# VetRA
Veterinary Record Access is an API as part of a larger project to supply veterinary clinics with a tool to improve communication with their clients. 

This API recieves information from a veterinary clinic's practice management software about each patients diagnostic test results and current prescriptions. It stores that information in a database to be queried in order to supply and store information from the VetRA website. 

This API also provides authentication and authorization. 
- An "OWNER" user may view information about their pets including tests and medications, view messages sent by or sent to them, and send messages and refill requests. They may also update their password.
- A "DOCTOR" user may view any information about any patient, view messages sent by or sent to them, and send and manipulate messages and refill requests. They may also update their password.
- An "ADMIN" user may do all of the above, as well as manipulate user information and delete data.

## How
Java, PostgreSQL, Spring Boot

---

See [documentation](documentation) folder for more extensive user capability descriptions as well as endpoint paths and their behavior.

See [getting started](documentation/getting-started.md) document to set up and test VetRA.
