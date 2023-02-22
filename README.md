# lib-error

### Description

Library that provides common exceptions and a simple error mapper for mapping exceptions to responses.
Can be used for example in a controller, to map an exception to a response that te user can understand.

### Dependency

```gradle
implementation("com.hoseus:lib-error:0.1.0")
```

### Usage

#### Default error mapper
The DefaultErrorMapper has defined mappings for the exceptions in this library and for unknown exceptions.

#### Models
The BadRequestException will be mapped to a BadRequestErrorDto:
```json
{
    "code": "some_code",
    "errors": [
       {
          "description": "Format error in field 1" 
       },
       {
          "description": "Field 2 must be present"
       }
    ]
}
```

All the other exceptions will be mapped to a DefaultErrorDto:
```json
{
    "code": "some_code",
    "message": "some message"
}
```

#### Mappings
| Exception              | Status | Code             | Message                                                                                                                                                            |
|------------------------|--------|------------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| BadRequestException    | 400    | "bad_request"    | Instead of message has a list of request errors with a description obtained from the exception. If the exception has an empty list of erros the response will too. |
| NotFoundException      | 404    | "not_found"      | The message of the exception. If no message is present: "Resource not found".                                                                                      |
| BusinessException      | 422    | "business_error" | The message of the exception. If no message is present: "Something went wrong".                                                                                    |
| TimeoutException       | 500    | "server_timeout" | "The server timed out when communicating with another service".                                                                                                    |
| CommunicationException | 500    | "server_error"   | "The server failed to establish connection with another service".                                                                                                  |
| Any other exception    | 500    | "server_error"   | "Something went wrong".                                                                                                                                            |

#### Example
```kotlin
val errorMapper = DefaultErrorMapper(
  responseBuilder = { status, body ->
     RestResponse.status(
        RestResponse.Status.fromStatusCode(status),
        body
     )
  }
)

try {
    doSomething()
} catch(e: Exception) {
   return errorMapper.mapException(e)
}
```
#### Extend the DefaultErrorMapper
```kotlin
class CustomErrorMapper: DefaultErrorMapper<RestResponse<ErrorDto>>(
   responseBuilder = { status, body ->
      RestResponse.status(
         RestResponse.Status.fromStatusCode(status),
         body
      )
   }
) {
   override fun mapException(e: Throwable): RestResponse<ErrorDto> =
      when(e) {
         is ResteasyReactiveViolationException -> {
            this.responseBuilder.build(
               status = 400,
               body = BadRequestErrorDto(code = "bad_request", errors = e.constraintViolations.map { RequestError(it.message) }.distinct())
            )
         }
         else -> super.mapException(e)
      }
}
```

### Release and versioning:

I used [uplift](https://upliftci.dev/)

#### 1) Release
```shell
uplift release
```
