import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        method 'POST'
        urlPath("/users")
        body('''{
"username": "jsmith",
"email": "jsmith@mail.com",
"countryCode":  "GB"
}''')
        headers {
            header(contentType(), applicationJson())
        }
    }
    response {
        status OK()
        body('''{
"username": "jsmith",
"email": "jsmith@mail.com",
"countryCode":  "GB",
}''')
        headers {
            header(contentType(), applicationJson())
        }
        bodyMatchers {
            jsonPath('$.id', byRegex("^[0-9a-f]{24}\$"))
        }
    }
}
