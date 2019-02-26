# Jersey-Practice
practice project for Jersey

Project Setup:

1.
Project uses Redis as No-SQL database to store data.
Please install Redis on your workstation before running anything.

For Redis on windows below link will be helpful:

https://github.com/MicrosoftArchive/redis/releases

2.
Apache Tomcat 9 or above should be used as server.


----------------------------------------------------------------------------------------------------------------------------------------


1. Run Consumer.Java which is present in com.casestudy.client package and select appropriate action to insert, update or view record.

2. First insert a record in Redis database using Insert option. The default value inserted will have id:13860428.

3. Now you can either retrieve or update the data.

4. Retrieval of record uses data from external api and redis to send appropriate data to user.

5. Try retrieving the data to see the result.

6. Now update the record using the options given.

7. Retrieve the data again to see the updated value.

---------------------------------------------------------------------------------------------------------------------------------------

Alternatively below are the api endpoints which you can use in postman.

1.
POST:
http://localhost:8080/CaseStudy/api/products/addProduct

Body (JSON)

{
    "current_price": {
        "currency_code": "USD",
        "value": 44.22
    },
    "id": 13860428,
    "name": "The Big Lebowski (Blu-ray)"
}



2.
GET:
http://localhost:8080/CaseStudy/api/products/13860428



3.
PUT:
http://localhost:8080/CaseStudy/api/products/13860428

Body (JSON)

{
    "current_price": {
        "currency_code": "CAD",
        "value": 14.22
    },
    "id": 13860428,
    "name": "The Big Lebowski (Blu-ray)"
}

