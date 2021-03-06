# international-school-socialworker-BE


```https://lambda-intschools.herokuapp.com```

## ENDPOINTS

### Authorization Enpoints

####  Create a new user with user-type schooladmin
*Grants the account an authority of 'ROLE_SCHOOLADMIN' and will make all School Admin endpoints accessible. For more information on authority, check Administrative Endpoints section*

`POST: /createnewuser/schooladmin`
 
```
Body:
{
   "username": "string",
   "password": "string"
}
```
    

#### Create a new user with user-type socialworker
*Grants the account an authority of 'ROLE_SOCIALWORKER' and will make all Social Worker endpoints accessible. For more information on authority, check Administrative Endpoints section*

`POST: /createnewuser/socialworker`

```
Body:
{
   "username": "string",
   "password": "string"
}
```

#### Logout current user

`GET: /oauth/revoke-token`

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

#### Update username and password of currently logged in user 

`PUT: /users/myinfo`

```
All fields are optional
Body:
{
   "username": "string",
   "password": "string"
}
```

---

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
#### Delete currently logged in School Admin

```DELETE: /schooladmins/myinfo```

#### Find School Admin by Id
*Will only be accessible to an administrative account. Not accessible by School Admins or Social Workers.*

```GET: /schooladmins/{id}```

#### Find all School Admins
*Will only be accessible to an administrative account. Not accessible by School Admins or Social Workers.*

```GET: /schooladmins/all```

---

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
#### Delete currently logged in Social Worker

```DELETE: /socialworkers/myinfo```

#### Find Social Worker by Id
*Will only be accessible to School Admins.*

```GET: /socialworkers/{id}```

#### Find all Social Workers
*Will only be accessible to School Admins.*

```GET: /socialworkers/all```

#### Assign a Social Worker to current School Admin's school
*Can only be accessed by School Admins. The Social worker will automatically be assigned to current School Admin's school. Multple Social workers can be assigned to the same school and a Social Worker can be assigned to multiple schools*

```POST: /socialworkers/assigntoschool/{workerId}```

#### Remove a Social Worker from current School Admin's school
*Can only be accessed by School Admins. The Social worker will also lose all of their assigned students*

```DELETE: /socialworkers/removefromschool/{workerId}```

#### Join an Organization as a Social Worker
*Can only be accessed by Social Workers*

```PUT: /socialworkers/joinorg/{orgId}```

#### Leave current Social Worker's Organization
*Can only be accessed by Social Workers*

```DELETE: /socialworkers/leaveorg```

---

### School Endpoints

#### Find School by School Id
*Can only be accessible to Social Workers*

```GET: /schools/school/{id}```

#### Find all School
*Can only be accessible to Social Workers*

```GET: /schools/all```

#### Get current School Admin's school
*Can only be accessible to School Admins and will automatically return currently logged in School Admin's school*

```GET: /schools/myschool```

#### Create a School 
*Can only be accessible to School Admins and a school admin can only own one school*

```POST: /schools/myschool```
```
Body: 
{
    "schoolName": "string", (required)
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

---

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
Body: 
{
   "gradeNumber": integer (required)
} 
```
#### Update a grade in currently logged in School Admin's school
*Can only be accessed by School Admins. The grade has to belong to currently logged in School Admin*

```PUT: /grades/update/{id}```

```
Body: 
{
   "gradeNumber": integer (required)
} 
```

#### Delete a grade from currently logged in School Admin's School
*Can only be accessed by School Admins. The grade has to belong to currently logged in School Admin*

```DELETE: /grades/delete/{id}```

---

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
Body: 
{
   "className": "string" (required)
} 
```
#### Update a class in currently logged in School Admin's school
*Can only be accessed by School Admins. The class has to belong to currently logged in School Admin*

```PUT: /classes/update/{id}```
```
Body: 
{
   "className": integer (required)
} 
```

#### Delete a class from currently logged in School Admin's School
*Can only be accessed by School Admins. The class has to belong to currently logged in School Admin*

```DELETE: /classes/delete/{id}```

---

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

```PUT: /students/update/{id}```

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

#### Assign Student to a Social Worker
*Can only be accessed by School Admins and the student has to be enrolled in the School Admin's school*

```POST: /students/{studentId}/assigntoworker/{workerId}```

#### Assign Student to a Class
*Can only be accessed by School Admins and the student has to be enrolled in the School Admin's school*

```POST: /students/{studentId}/assigntoclass/{classId}```

#### Assign Student to a Grade
*Can only be accessed by School Admins and the student has to be enrolled in the School Admin's school*

```POST: /students/{studentId}/assigntograde/{gradeId}```

#### Assign Student to current School Admin's school
*Can only be accessed by School Admins. The student will automatically be assigned to the currently logged in School Admin's school*

```POST: /students/{studentId}/assigntoschool```

#### Remove a Student's current Social Worker
*Can only be accessed by School Admins. The student has to be enrolled in the currently logged in School Admin's school*

```DELETE: /students/{studentId}/removefromworker```

#### Remove a Student from current Grade
*Can only be accessed by School Admins. The student has to be enrolled in the currently logged in School Admin's school*

```DELETE: /students/{studentId}/removefromgrade```

#### Remove a Student from current Class
*Can only be accessed by School Admins. The student has to be enrolled in the currently logged in School Admin's school*

```DELETE: /students/{studentId}/removefromclass```

#### Remove a Student from current School
*Can only be accessed by School Admins. The student has to be enrolled in the currently logged in School Admin's school*

```DELETE: /students/{studentId}/removefromschool```

---

### Visit Endpoints

#### Find Visit by Id
*Can be accessed by all accounts*

```GET: /visits/visit/{id}```

#### Find all Visits
*Cannot be accessed by School Admins or Social Workers. Can only be accessed by administrative account*

```GET: /visits/all```

#### Find all Social Worker Visits
*Can only be accessed by Social Workers.The returned visits will all belong to the currently logged in Social Worker*

```GET: /visits/socialworker/all```

#### Find all School Visits
*Can only be accessed by School Admins. The returned visits will all belong to the currently logged in School Admin's school*

```GET: /visits/schooladmin/all```

#### Create a new Visit
*Can only be accessed by School Admins. The created visit will automatically be assigned to the currently logged in School Admin's school*

```POST: /visits/new/{workerId}```

```
All fields are optional
Body: 
{
   "visitReason": "string",
   "visitDescription": "string", 
   "visitDate": "string"
}
```

#### Update an existing Visit
*Can only be accessed by School Admins. The Visit has to belong to the currently logged in School Admin's school*

```PUT: /visits/update/{visitId}```

```
All fields are optional
Body: 
{
   "visitReason": "string",
   "visitDescription": "string", 
   "visitDate": "string"
}
```

#### Delete an existing Visit
*Can only be accessed by School Admins. The Visit has to belong to the currently logged in School Admin's school*

```DELETE: /visits/delete/{visitId}```

---

### Image Upload Endpoint
*Can be accessed by all accounts* 

```POST: /image/upload```

```BODY: Send the image file as form-data with key "image"```

The response will contain the url where the image can be accessed. Store the url in the "photoUrl" field of appropriate entity and update that entity at the respective endpoint

---

### Organization Endpoints

#### Find Organization by Id
*Can be accessed by all accounts*

```GET: /orgs/org/{id}```

#### Find all Organizations
*Can be accessed by all accounts*

```GET: /orgs/all```

#### Create a new Organization
*Can only be accessed by an administrative account. Cannot be accessed by Social Workers or School Admins*

```POST: /orgs/new```

```
All fields are optional
Body: 
{
   "organizationName": "string",
   "organizationMission": "string", 
}
```

#### Update an existing Organization
*Can only be accessed by an administrative account. Cannot be accessed by Social Workers or School Admins*

```PUT: /orgs/update/{id}```

```
All fields are optional
Body: 
{
   "organizationName": "string",
   "organizationMission": "string", 
}
```

#### Delete an existing Organization
*Can only be accessed by an administrative account. Cannot be accessed by Social Workers or School Admins*

```DELETE: /orgs/delete/{id}```

---

### Adminstrative endpoints
*All these endpoints can only be accessed by an administrative account. Cannot be accessed by School Admins and Social Workers. Every account in the system can have a role of admin (roleId: 3), schooladmin (roleId: 1), and socialworker(roleId: 2). These endpoints should not be used in production*

#### List all Users
*Lists all users with their respective authorities

`GET: /users/users`

#### Get User by Id
*Finds a User based on the User's Id*

`GET: /users/user/{userId}`

#### Create new User with a Role(s) and authority
*Creates a new User with a Role(s) based on the Role Id(s) provided. The user can have multiple roles, but should never be done in production.*

`POST: /users/user`

```
Body: 
{
	"username": "schooladmin1",
	"password": "password1", 
	"userRoles": [
            {
                "role": {
                    "roleId": 1
                }
            }
   ]
}
```

#### Update a User's information

`PUT: /users/user/{userId}`

```
Body: 
{
	"username": "string",
	"password": "string", 
	"userRoles": [
            {
                "role": {
                    "roleId": 1
                }
            }
   ]
}
```

#### Delete a User's information 

`DELETE: /users/user/{userId}`


















