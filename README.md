
# international-school-socialworker-BE


```https://buildtipsease.herokuapp.com```

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
The request grants an "access_token" in the json response and must be sent in as a header Authorization: Bearer access_token_here

