# Please read

### How to run

Just run as any regular spring boot app with gradle. You will need JDK 11 or above. 

```
./gradlew bootRun
```

### Security

To invoke any of the endpoints you will need an `access token` token:

```
curl --location --request POST 'http://localhost:8080/oauth2/token' \
--header 'Authorization: Basic c29tZS1jbGllbnQtaWQ6c29tZS1jbGllbnQtc2VjcmV0' \
--form 'grant_type="client_credentials"'
```

### Endpoints

- GET http://localhost:8080/api/v1/transactions
- GET http://localhost:8080/api/v1/transactions/{uuid}
- POST http://localhost:8080/api/v1/transactions
- PUT http://localhost:8080/api/v1/transactions/{uuid}
- DELETE http://localhost:8080/api/v1/transactions/{uuid}

Oh, and the health check endpoint I'm just using `Actuator's`:

- GET http://localhost:8080/actuator/health
  
Instructions  did not explicitly say I had to create my own :)

Some examples:

Get all transactions
```
curl --location --request GET 'http://localhost:8080/api/v1/transactions' \
--header 'Authorization: Bearer <access_token>'
```

Transactions can be filtered by amount, currency, description or status:
```
curl --location --request GET 'http://localhost:8080/api/v1/transactions?description=transaction&currency=USD' \
--header 'Authorization: <access_token>'
```

Create new transaction
```
curl --location --request POST 'http://localhost:8080/api/v1/transactions' \
--header 'Authorization: Bearer <access_token>' \
--header 'Content-Type: application/json' \
--data-raw '{"description": "test description", "status": "REFUNDED", "amount": "666.66", "currency": "USD", "merchantUuid": "daccd4ef-e34c-4494-bece-292eafdc2786", "merchantName": "John"}'
```

### Database

As requested, a simple `H2` in memory database is being used. Some data is loaded during startup by means of `data.sql`. When the application is running you can access the database by accessing:

```
http://localhost:8080/h2-console

JDBC URL: jdbc:h2:mem:merchantdb
User Name: sa
(empty password)
```

### Tests 

As you can see in the tests I've decided to use [Spock](https://spockframework.org/spock/docs/1.3/all_in_one.html#_getting_started). It's a groovy based test framework. Just way simpler to use than JUnit 5.

```
./gradlew test
```

