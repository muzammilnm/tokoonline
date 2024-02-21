# Endpoint Documentation

## Authentication

Endpoints for user authentication.

### POST /logins

Send a request to log in the user.

#### Request

```json
POST /login
Content-Type: application/json

{
  "email": "johndoe@gmail.com",
  "password": "password123"
}
```
#### Response
```json
200 OK
{
  "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJKb2huIERvZSIsImlhdCI6MTY0NjU4MzY3OX0.-dqOSV8Tc0EDUKPd4S8I5V8nC_7r5NfLsnM4iB60pEo",
  "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJKb2huIERvZSIsImlhdCI6MTY0NjU4MzY3OX0.-dqOSV8Tc0EDUKPd4S8I5V8nC_7r5NfLsnM4iB60pEo",
  "type": "Bearer"
}
```
```json
401 UNAUTHORIZED
{}
```

### POST /registers

Send a request to create a new account.

Parameters:
* email

#### Request

```json
POST /registers
Content-Type: application/json

{
  "firstName": "john",
  "lastName": "doe",
  "username": "johndoe",
  "email": "johndoe@gmail.com",
  "password": "password123"
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
400 BADREQUEST
{}
```

### POST /refresh-tokens

Send a request to generate new token

#### Request

```json
POST /refresh-tokens
Content-Type: application/json
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJKb2huIERvZSIsImlhdCI6MTY0NjU4MzY3OX0.-dqOSV8Tc0EDUKPd4S8I5V8nC_7r5NfLsnM4iB60pEo

{
  "firstName": "john",
  "lastName": "doe",
  "username": "johndoe",
  "email": "johndoe@gmail.com",
  "password": "password123"
}
```
#### Response
```json
200 OK
{
  "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJKb2huIERvZSIsImlhdCI6MTY0NjU4MzY3OX0.-dqOSV8Tc0EDUKPd4S8I5V8nC_7r5NfLsnM4iB60pEo",
  "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJKb2huIERvZSIsImlhdCI6MTY0NjU4MzY3OX0.-dqOSV8Tc0EDUKPd4S8I5V8nC_7r5NfLsnM4iB60pEo",
  "type": "Bearer"
}
```
```json
400 BADREQUEST
{}
```