# Funds Transfer with currency exchange

An application to transfer funds between two accounts with currency exchange.

## Description

This application transfers funds between two accounts with currency exchange.
The Fixer API is used to obtain the exchange rates between two currencies.

## Getting Started

### Installation

* Git clone this repository
* Open the project in an IDE like IntelliJ
* Use the API access key shared over email on line 12 of ExchangeRateClient class
* Build project
* Run the spring boot application FundstransferApplication
* The server should be running on http://localhost:8080/

### Executing the program

* Create 2 test accounts using the following curl commands and note the ids returned -
``` 
curl -d '{"currency":"USD","balance":"3000"}'  -H "Content-Type: application/json" -X POST http://localhost:8080/api/accounts 
```

``` 
curl -d '{"currency":"EUR","balance":"3000"}'  -H "Content-Type: application/json" -X POST http://localhost:8080/api/accounts 
```
    
* Transfer 100 EUR from account 1 to account 2 (replace 1 and 2 with the ids returned in the previous step)

``` 
curl -d '{"debitAccount":1,"creditAccount":2,"currency":"EUR","amount":100}'  -H "Content-Type: application/json" -X POST http://localhost:8080/api/transfers

``` 
* To test parallel executions 
``` 
curl --parallel --parallel-immediate --parallel-max 8 -X POST -d ' {"debitAccount":1,"creditAccount":2,"currency":"EUR","amount":10}' --config urls.txt
``` 
where urls.txt should contain the following to send 10 concurrent requests 
``` 
url = "http://localhost:8080/api/transfers"
url = "http://localhost:8080/api/transfers"
url = "http://localhost:8080/api/transfers"
url = "http://localhost:8080/api/transfers"
url = "http://localhost:8080/api/transfers"
url = "http://localhost:8080/api/transfers"
url = "http://localhost:8080/api/transfers"
url = "http://localhost:8080/api/transfers"
url = "http://localhost:8080/api/transfers"
url = "http://localhost:8080/api/transfers"
``` 
* Other REST APIs for account CRUD operations are also available with PUT, GET and DELETE
* The error cases such as account not found, exchange rate not found, insufficient balance are handled.

## Optimizations
### I would implement the following for a more maintainable PROD project

* @Value property to inject Fixer API key in ExchangeRateClient
* Use mapstruct to map between resource and repository objects