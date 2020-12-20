import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        method 'POST'
        urlPath("/users")
        body('''{
"username": "jsmith",
"email": "jsmith",
"countryCode":  "FR",
"firstName": "John",
"lastName":  "Smith",
"phoneNumber": "+98371248124"
}''')
        headers {
            header(contentType(), applicationJson())
        }
    }
    response {
        status BAD_REQUEST()
        body(
                message: $(anyNonEmptyString()),
                code: 400
        )
        headers {
            header(contentType(), applicationJson())
        }
    }
}
