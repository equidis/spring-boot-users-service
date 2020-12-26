import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        method 'GET'
        urlPath("/users/5fdb5cdad07bba25f645cd87")
    }
    response {
        status OK()
        body('''{
"username": "jdoe",
"email": "jdoe@mail.com",
"countryCode":  "FR",
"firstName": "John",
"lastName":  "Doe",
"phoneNumber": "+33123456789",
"id":  "5fdb5cdad07bba25f645cd87"
}''')
        headers {
            header(contentType(), applicationJson())
        }
    }
}
