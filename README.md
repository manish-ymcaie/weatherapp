# Prerequisites are maven and jdk 17.
# Steps to use this weatherapp are as below:
1) Import the project in you IDE(Eclipse) as a maven project after clonig it from below git repo:

2) Create postgres db named weatherdb on you local postgres server running on port 5432 with user and password as "postgres". If you want different config for database please edit the application.properties.
Now, copy the contents of db.sql from src/main/resource folder and run against you database.

3)In application.properties change the api.key property value with the key created from your account from below url:
https://home.openweathermap.org/api_keys

4)After cloning and changing the configuration in application.properties run below command from weatherapp folder(project folder)
mvn clean install

5)Right click on WeatherappApplication.java and run it as a java application, it will launch the server at port 8080 on localhost.

6) Import the collection json file weather.postman_collection.json on you postman and run the various endpoints. Also, the swagger documentation can be checked at below url after launching the application in step 5.
http://localhost:8080/swagger-ui/index.html

7) Hit various end points to test the weather app features.

8) Tests can be executed from src/main/test folder.