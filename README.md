
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

`curl -X POST --user "intworkers:lambdaintworkers" -d "grant_type=password&username=admin&password=password" http://jrmmba-restaurants.heroku.com/oauth/token`

`POST: /oauth/token`

``` 
AUTHBASE64 = "intworkers:lambdaintworkers" // encode this string into base64 
Header "Authorization":"Basic AUTHBASE64" // then replace it here
Body x-www-form-urlencoded
grant_type : password
username : your username
password : your password
```

