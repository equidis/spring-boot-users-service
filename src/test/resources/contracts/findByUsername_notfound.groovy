import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        method 'GET'
        urlPath("/users") {
            queryParameters {
                parameter 'username': 'unknown'
            }
        }
    }
    response {
        status NOT_FOUND()
        body(
                message: $(anyNonEmptyString()),
                code: 404
        )
        headers {
            header(contentType(), applicationJson())
        }
    }
}
