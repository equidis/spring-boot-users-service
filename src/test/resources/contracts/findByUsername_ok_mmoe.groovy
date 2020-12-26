import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        method 'GET'
        urlPath("/users") {
            queryParameters {
                parameter 'username': 'mmoe'
            }
        }
    }
    response {
        status OK()
        body('''{
"username": "mmoe",
"email": "mmoe@mail.com",
"countryCode":  "GB",
"id":  "5fe773204edcff0fbfbf45e4"
}''')
        headers {
            header(contentType(), applicationJson())
        }
    }
}
