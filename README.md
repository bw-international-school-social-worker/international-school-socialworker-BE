
# international-school-socialworker-BE


```https://lambda-intschools.herokuapp.com```

## ENDPOINTS

### Authorization Enpoints

####  Create a new user with user-type schooladmin

POST: /createnewuser/schooladmin

Body: 
 
   ```{ 
    "username": "Username string here",
    "password": "Password string here"
    }
  ```
    

#### Create a new user with user-type socialworker

`POST: /createnewuser/socialworker`

Body: 
   ``` 
   { 
    "username": "Username string here",
    "password": "Password string here"
    }
  ```

#### Logout current user

`GET: /createnewuser/socialworker`

#### Login and get access token

`curl -X POST --user "intworkers:lambdaintworkers" -d "grant_type=password&username=admin&password=password" http://lambda-intschools.herokuapp.com/oauth/token`

`POST: /oauth/token`

``` 
Headers: 
Authorization: Basic aW50d29ya2VyczpsYW1iZGFpbnR3b3JrZXJz
Content-Type: application/x-www-form-urlencoded
Body:
grant_type : password
username : your username
password : your password
```
The request grants an "access_token" in the JSON response and must be sent in as a header Authorization: Bearer access_token_here  for **ALL OTHER ENDPOINTS**. The token will be valid for 1 hour.

### School Admin Information Endpoints

#### Get  information of currently logged in School Admin
`GET: /schooladmins/myinfo`

#### Update information of currently logged in School Admin
`PUT: /schooladmins/myinfo`
```
All fields are optional
Body: 
{
    "firstName": "string",
    "lastName": "string",
    "phone": "string",
    "email": "string",
    "photoUrl": "string",
}
```
#### Delete currently logged in user
`DELETE: /schooladmins/myinfo`

#### Find School Admin by Id
*Will only be accessible to an administrative account. Not accessible by School Admins or Social Workers.*
`GET: /schooladmins/{id}`

#### Find all School Admins
*Will only be accessible to an administrative account. Not accessible by School Admins or Social Workers.*
`GET: /schooladmins/all`

### Social Worker Information Endpoints

#### Get  information of currently logged in Social Worker
`GET: /socialworkers/myinfo`

#### Update information of currently logged in Social Worker
`PUT: /socialworkers/myinfo`
```
All fields are optional
Body: 
{
    "firstName": "string",
    "lastName": "string",
    "phone": "string",
    "email": "string",
    "photoUrl": "string",
}
```
#### Delete currently logged in user
`DELETE: /socialworkers/myinfo`

#### Find Social Worker by Id
*Will only be accessible to School Admins.*
`GET: /socialworkers/{id}`

#### Find all Social Workers
*Will only be accessible to School Admins.*
`GET: /socialworkers/all`
