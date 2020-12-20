import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        method 'POST'
        urlPath("/users")
        body('''{
"username": "rroe",
"email": "rroe@mail.com",
"countryCode":  "FR",
"firstName": "Richard",
"lastName":  "Roe",
"phoneNumber": "+339877654321"
}''')
        headers {
            header(contentType(), applicationJson())
        }
    }
    response {
        status OK()
        body('''{
"username": "rroe",
"email": "rroe@mail.com",
"countryCode":  "FR",
"firstName": "Richard",
"lastName":  "Roe",
"phoneNumber": "+339877654321"
}''')
        headers {
            header(contentType(), applicationJson())
        }
        bodyMatchers {
            jsonPath('$.id', byRegex("^[0-9a-f]{24}\$"))
        }
    }
}
