# Account API

Sample application of a simple bank account API.


## Run the application

### From IDE

Run the `Application` class.

### Docker

For example: build an image, start a container and stop it when done. 

```
$ docker build -t bankapi .
$ docker run -d -p 4567:4567 --name bankapi1 bankapi
$ docker stop bankapi1  
```

   
## Play with the sample data   
   
There are 4 sample users/accounts already created when you launch the app (see the [Application.java:109](https://github.com/fmaylinch/bankapi/blob/master/src/main/java/com/codethen/bankapi/Application.java#L109)).

First you need to login with some user to get a JWT token:

```
curl -X POST \
  http://localhost:4567/api/v1/user/login \
  -H 'Content-Type: application/json' \
  -d '{"username":"john","password":"john-password"}' 
```

Then, use that token in the next account calls. For example to get the account balance:

```
curl -X GET \
  http://localhost:4567/api/v1/account/balance \
  -H 'Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqb2huI'
'
```

And to transfer money to other account:

```
curl -X POST \
  http://localhost:4567/api/v1/account/transfer \
  -H 'Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqb2huI' \
  -H 'Content-Type: application/json' \
  -d '{"toUser":"mary","units":500}'
```

Note that transfers only work if both accounts have the same currency.

## Decisions taken in this application

### Domain

- Only one account per username, although the model would easily support multiple accounts per user.
- Currencies are handled in cents as integers (we call them `units`), to avoid rounding and precision issues.
- It's not possible to transfer between different currencies, but it could be supported given some conversion service.  

### Architecture

- API is built with [Spark](http://sparkjava.com/).
- The domain is isolated from the architecture (frameworks, API, databases, etc).

### Security

- Passwords are stored with BCrypt, using [jBCrypt](https://github.com/jeremyh/jBCrypt).
- API is secured with [JWT](https://jwt.io/), using the [JJWT](https://github.com/jwtk/jjwt) library.