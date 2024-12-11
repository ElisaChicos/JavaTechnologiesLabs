# JavaElla
## Laborator 8
## 1) (2p) https://github.com/Trased/JavaElla/pull/1/commits/daa1b7bb19b6e26e3ab61f3677986342296e9c56 
### CRUD Operations:
### CREATE --- POST Request:
> curl -X POST   -H "Content-Type: application/json"   -d '{"activity": "Math", "activityType": "Course", "grade": 9, "comment": "Excellent!"}'   "http://localhost:8080/Lab3_war/api/evaluations?teacherId=CHANGE_ID&studentId=CHANGE_ID"


### READ ---  GET Request:
> curl -X GET \
  "http://localhost:8080/Lab3_war/api/evaluations"
  
#### ALTERNATIV
> curl -X GET \
  "http://localhost:8080/Lab3_war/api/evaluations?teacherId=CHANGE_ID"

#### SAU
> curl -X GET \
  "http://localhost:8080/Lab3_war/api/evaluations?studentId=CHANGE_ID"


### UPDATE --- PUT Request
> curl -X PUT   -H "Content-Type: application/json"   -d '{
    "teacher": {"id": 1},
    "student": {"id": 2},
    "activity": "Math",
    "activityType": "Course",
    "grade": 8,
    "comment": "Improved performance"
  }'   http://localhost:8080/Lab3_war/api/evaluations?evaluationId=EVALUATION_ID


### DELETE --- DELETE Request
> curl -X DELETE http://localhost:8080/Lab3_war/api/evaluations?evaluationId=EVALUATION_ID

___
## 2) (1p) https://github.com/Trased/JavaElla/pull/2/commits/37ec4591cb76ce41babe16389160fcb16d8e611d
### CACHE
#### In cache se stocheaza raspunsurile la requesturile de tip GET, pentru a nu se face interogari nefolositoare in baza de date care vor returna acelasi raspuns.
#### Daca a fost un request de tipul CREATE / UPDATE / DELETE, se va cauta in cache daca exista vro intrare care are student_id / teacher_id / evaluation_id manipulat de acest request, si i se va da drop, iar la urmatorul request de tipul GET, se va face apel la baza de date si se va stoca raspunsul nou in cache.

