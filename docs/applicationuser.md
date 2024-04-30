## Application User

Endpoints for user modification

### POST /application-users{email}

Send a request to lupdate application user.

Path Variable:
* email

#### Request

```json
POST /application-users{email}
Content-Type: application/json
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJKb2huIERvZSIsImlhdCI6MTY0NjU4MzY3OX0.-dqOSV8Tc0EDUKPd4S8I5V8nC_7r5NfLsnM4iB60pEo

{
  "firstName": "john",
  "lastName": "doe",
  "username": "johndoe",
}
```
#### Response
```json
201 CREATED
{
  "email": "johndoe@gmail.com"
}
```
```json
401 UNAUTHORIZED
{}
```
```json
400 BADREQUEST
{}
```