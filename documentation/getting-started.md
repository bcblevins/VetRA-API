# Getting Started

## Requirements
 - PostgreSQL and pgAdmin 4 (or equivalent database management tool) installed on your machine.
 - Java 17 or higher installed on your machine (lower versions may work but are not tested).
 - IntelliJ IDEA (or equivalent IDE) installed on your machine.
 - Postman (or equivalent API testing tool) installed on your machine.

## Setting up the Database
1. Open pgAdmin 4 and set postgres1 as the password (not the master password, the password for the user 'postgres').
2. Create a new database called `Vetra'
3. Open the query tool and open the VetRA.sql file located in the database folder of this project.
4. Run the script to create the tables and insert the data. Ensure that the script runs without any errors.

## Running the Application
1. Open the project in IntelliJ IDEA (or equivalent IDE).
2. Run the application by clicking the green play button in the top right corner of the IDE (you may need to navigate to the VetraApplication.java file before this option is available).
3. The application should now be running on `http://localhost:8080/`. Ensure that the console does not display any errors (there will be some displayed messages, but no errors).

## Authenticating as an ADMIN
1. Open Postman (or equivalent API testing tool).
2. Import the VetRA.postman_collection.json file located in the postman folder of this project.
3. Start with the POST 'auth' request to authenticate as a user. You should see the following in the 'body' tab:
```json
{
    "username": "admin",
    "password": "admin"
}
```
4. Click 'Send' and you should receive a token in the 'body' tab. Copy the string without the quotes after "token": 
```json
{
    "accessToken": {
        "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsInVpZCI6LTEsIm5iZiI6MTcxNjEyMTk4MCwiaXNzIjoiZnJhaG8tc2VjdXJpdHkiLCJleHAiOjE3MTYxMjU1ODAsImlhdCI6MTcxNjEyMTk4MCwiYXV0aG9yaXRpZXMiOlsiQURNSU4iXSwianRpIjoiNzcxNmQzMDItZTQ0Mi00NDczLThjYjYtMjU4ZWM2OGM5YzRkIn0.Knah1kZMUyQv63J2yXdtn2M5hUb7AAj65NYk9niTpKg",
        "expiresIn": 3600
    },
    "refreshToken": null
}
```
5. Navigate to the 'Authorization' tab in Postman and select 'Bearer Token' from the 'Type' dropdown. Paste the token you copied into the 'Token' field.

## Using the API
You can now use the API by selecting a request from the collection on the left side of Postman. You can find information about each request in the 'api-endpoints.md' file located in the documentation folder of this project.
