
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

```GET: /schooladmins/myinfo```

#### Update information of currently logged in School Admin

```PUT: /schooladmins/myinfo````
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

```DELETE: /schooladmins/myinfo```

#### Find School Admin by Id
*Will only be accessible to an administrative account. Not accessible by School Admins or Social Workers.*

```GET: /schooladmins/{id}```

#### Find all School Admins
*Will only be accessible to an administrative account. Not accessible by School Admins or Social Workers.*

```GET: /schooladmins/all```

### Social Worker Information Endpoints

#### Get  information of currently logged in Social Worker

```GET: /socialworkers/myinfo```

#### Update information of currently logged in Social Worker

```PUT: /socialworkers/myinfo```
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

```DELETE: /socialworkers/myinfo```

#### Find Social Worker by Id
*Will only be accessible to School Admins.*

```GET: /socialworkers/{id}```

#### Find all Social Workers
*Will only be accessible to School Admins.*

```GET: /socialworkers/all```

### School Endpoints

#### Find School by School Id
*Can only be accessible to Social Workers*

```GET: /schools/school/{id}```

#### Find all School
*Can only be accessible to Social Workers*

```GET: /schools/all```

#### Create a School 
*Can only be accessible to School Admins and a school admin can only own one school*

```POST: /schools/myschool```
```
All fields are optional
Body: 
{
    "schoolName": "string",
    "dateEstablished": "string"
}
```
#### Update a School
*Can only be accessed by School Admins and the school admin can only update his/her own school*

```PUT: /schools/myschool```
```
All fields are optional
Body: 
{
    "schoolName": "string",
    "dateEstablished": "string"
}
```

#### Delete a School
*Can only be accessed by School Admins and the school admin can only delete his/her own school*

```DELETE: /schools/myschool```

### Grade Endpoints

#### View all grades and students within
*Can only be accessed by an administrative account. Cannot by accessed by School Admins or Social Workers*

```GET: /grades/all```

#### View all grades and students in currently logged in School Admin's school
*Can only be accessed by School Admins*

```GET: /grades/mygrades```

#### Create a new grade in currently logged in School Admin's school
*Can only be accessed by School Admins*

```POST: /grades/new```

```
All fields are optional
Body: 
{
   "gradeNumber": integer
} 
```
#### Update a grade in currently logged in School Admin's school
*Can only be accessed by School Admins. The grade has to belong to currently logged in School Admin*

```PUT: /grades/update/{id}```

```
All fields are optional
Body: 
{
   "gradeNumber": integer
} 
```

#### Delete a grade from currently logged in School Admin's School
*Can only be accessed by School Admins. The grade has to belong to currently logged in School Admin*

```DELETE: /grades/delete/{id}```

### Class Endpoints

#### View all classes and students within
*Can only be accessed by an administrative account. Cannot by accessed by School Admins or Social Workers*

```GET: /classes/all```

#### View all classes and students in currently logged in School Admin's school
*Can only be accessed by School Admins*

```GET: /classes/mygrades```

#### Create a new class in currently logged in School Admin's school
*Can only be accessed by School Admins*

```POST: /classes/new```

```
All fields are optional
Body: 
{
   "className": "string"
} 
```
#### Update a class in currently logged in School Admin's school
*Can only be accessed by School Admins. The class has to belong to currently logged in School Admin*

```PUT: /classes/update/{id}```

```
All fields are optional
Body: 
{
   "className": integer
} 
```

#### Delete a class from currently logged in School Admin's School
*Can only be accessed by School Admins. The class has to belong to currently logged in School Admin*

```DELETE: /classes/delete/{id}```

### Student Endpoints

#### Find an existing student by Id
*Accessible to all accounts*

```GET: /students/student/{id}```

#### Find all students
*Accessible to all accounts*

```GET: students/all```

#### Find all students assigned to currently logged in Social Worker
*Can only be accessed by Social Worker accounts and will only return students that are currently assigned to the logged in Social Worker*

```GET: students/socialworker/all```

#### Find all students enrolled in currently logged in School Admin's school
*Can only be accessed by School Admins and will only return students that are enrolled in currently logged in School Admin's school*

```GET: students/schooladmin/all```

#### Create a new student 
*Can only be accessed by School Admins.*
**The School Admin has to be assigned to a school in order to create a student. The student will automatically be enrolled in the School Admin's school once created**

```POST: /students/new```

```
All fields are optional
Body: 
{
   "firstName": "string",
   "lastName": "string", 
   "photoUrl": "string", 
   "backgroundStory": "string",
   "status": "string",
   "age": integer, 
   "hasInsurance": boolean, 
   "insuranceExpiration": "string", 
   "hasBirthCertificate": boolean, 
   "specialNeeds": "string", 
   "contactInfo": "string" 
}
```

#### Update an existing student
*Can only be accessed by School Admins. The student has to be enrolled in the currently logged in School Admin's school.*

```POST: /students/update/{id}```

```
All fields are optional
Body: 
{
   "firstName": "string",
   "lastName": "string", 
   "photoUrl": "string", 
   "backgroundStory": "string",
   "status": "string",
   "age": integer, 
   "hasInsurance": boolean, 
   "insuranceExpiration": "string", 
   "hasBirthCertificate": boolean, 
   "specialNeeds": "string", 
   "contactInfo": "string" 
}
```

#### Delete a student
*Can only be accessed by School Admins. The student must be enrolled in the currently logged in School Admin's school*

```DELETE: /students/delete/{id}```






