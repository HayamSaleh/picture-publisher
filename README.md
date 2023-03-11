# Picture Publisher Spring Boot Application

The objective of this project is to create a platform for image publishing where users can upload their pictures, and an admin can review and either approve or reject them. The project utilizes Spring Boot as its framework and PostgresSQL as the database, with Swagger2 and Swagger-UI for API documentation.

## Requirements

For building and running the application you need:

- [JDK 1.8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
- [Maven 3](https://maven.apache.org)


##Technologies Used
* Spring Boot
* PostgradSQL
* Swagger2
* Swagger-UI

## Cloning and Running the application locally

*Clone the repository*

```shell
git clone https://github.com/HayamSaleh/picture-publisher.git
```

*Build the application*

```shell
cd publisher
mvn clean install
```

*Run the application*

```shell
java -jar target/publisher-0.0.1-SNAPSHOT.jar
```

The project is auto create the database for simplicity
The application should now be running on http://localhost:8080.


**Use Swagger-UI to interact with the APIs**

Open a web browser and navigate to http://localhost:8080/swagger-ui.html.

## Login
Admin: username=admin password=admin123

Use the following POST endpoint for logging: http://localhost:8080/login?username=admin&password=admin123

## Unit Testing

The project has sample unit tests. To run the unit tests, use the following command:

```shell
mvn test
```
