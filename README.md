# Account API

Sample application of a simple bank account API.

## Usage

There are 4 sample users/accounts already created when you launch the app (see the `Application` class).

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

And to transfer money to other account (with same currency):

```
curl -X POST \
  http://localhost:4567/api/v1/account/transfer \
  -H 'Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqb2huI' \
  -H 'Content-Type: application/json' \
  -d '{"toUser":"mary","units":500}'
```

## Decisions taken

### Domain

- Only one account per username, but the model would easily support multiple accounts.
- Currencies are handled in cents as integers, to avoid rounding and precision issues.
- For now it's not possible to transfer between different currencies, but it could be supported given some conversion service.  

### Architecture

- API is built using [Spark](http://sparkjava.com/) because it's a simple and flexible framework.
- The domain is isolated from the architecture (frameworks, API, databases, etc).

### Security

- Passwords are stored using BCrypt
- API is secured using JWT