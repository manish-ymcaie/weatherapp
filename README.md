# Getting Started

### Reference Documentation
For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/3.4.0/maven-plugin)
* [Create an OCI image](https://docs.spring.io/spring-boot/3.4.0/maven-plugin/build-image.html)

### Maven Parent overrides

Due to Maven's design, elements are inherited from the parent POM to the project POM.
While most of the inheritance is fine, it also inherits unwanted elements like `<license>` and `<developers>` from the parent.
To prevent this, the project POM contains empty overrides for these elements.
If you manually switch to a different parent and actually want the inheritance, you need to remove those overrides.

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