# Investment Funds API

Technical test project built with Spring Boot.

Este repositorio  corresponde  únicamente corresponde a una prueba técnica personal.

## Local
El proyecto tiene un Datastore local para persistencia de datos. Por decfecto para probar de manera local.
Para correr el proyecto local es necesario levantar la base de datos en docker con el comando docker run -p 8000:8000 amazon/dynamodb-local

 Cambiar la configuracion del bean  en el archivo DynamoDBConfig y colocarlo asi 
     @Bean
    public DynamoDbClient dynamoDbClient() {
        return DynamoDbClient.builder()
                .endpointOverride(URI.create("http://localhost:8000"))
                .region(Region.US_EAST_1)
                .credentialsProvider(
                        StaticCredentialsProvider.create(
                                AwsBasicCredentials.create("dummy", "dummy")
                        )
                )
                .build();
    }


## Architecture

The system allows clients to subscribe and cancel investment funds while validating business rules such as minimum investment amount and available balance.

### Technologies

- Java 17
- Spring Boot
- Maven
- Lombok
- JUnit
- Mockito
- AWS dynamodb
- AWS Elastic Beanstalk
---

## Architecture Diagram

Client → REST API → Services → Repositories
