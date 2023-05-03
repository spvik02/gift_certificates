# gift_certificates

JDK version: 17 

Build tool: Gradle

Web server: Apache Tomcat

Database: PostgreSQL

Testing: JUnit 5.+, Mockito

Controllers

- get
  - findAll. Parameters page and pageSize are optional 
    - for tags: http://localhost:8080/api/tags?page=1&pageSize=20
    - for certificates: http://localhost:8080/api/certificates?page=1&pageSize=20
  - findById
    - http://localhost:8080/api/tags/{id}
    - http://localhost:8080/api/certificates/{id}
  - findByParameters. All parameters are optional
    - http://localhost:8080/api/certificates/filter?tagName=water&substring=nat&dateSortType=asc&nameSortType=desc
- post
  - http://localhost:8080/api/tags with body
  ``` 
  {
    "name" : "spring"
  }
  ```
  - http://localhost:8080/api/certificates with body. If passed tag has no id new tag will be created in database
  ```
  {
    "name" : "swimming pool on nature",
    "description" : "swim swim swim",
    "price" : 3.14,
    "duration" : 14,
    "tags":[{"id":22, "name":"water"}, {"name":"spring"}]
  }
  ```
- put (updates all fields)
  - same as for post. If any field isn't specified it will be updated to null/0
- patch (updates passed fields)
  - same as for post. If any field isn't specified it will not be updated
- delete
  - same as for post. 
  
  
Error handling (wxamples)
- http://localhost:8080/api/tags/222

![image](https://user-images.githubusercontent.com/111181469/235832883-e71a75f9-f3c9-4a2d-9f08-c19350e982a3.png)
- http://localhost:8080/api/tags?page=1&pageSize=no

![image](https://user-images.githubusercontent.com/111181469/235832949-3217df84-e6d3-4924-8d51-1b0598131db2.png)

