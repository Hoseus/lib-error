package com.hoseus.error

import com.hoseus.error.exception.*
import com.hoseus.error.mapper.DefaultErrorMapper
import com.hoseus.error.mapper.ErrorMapper
import com.hoseus.error.model.BadRequestErrorDto
import com.hoseus.error.model.DefaultErrorDto
import com.hoseus.error.model.ErrorDto
import com.hoseus.error.model.RequestError
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContainAll
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeTypeOf

class ErrorMapperSpec: BehaviorSpec() {
	init {
		Given("ErrorMapper") {
			data class Response<T> (
				val status: Int,
				val body: T
			)

			val errorMapper: ErrorMapper<Response<ErrorDto>> = DefaultErrorMapper(
				responseBuilder = { status, body -> Response(status = status, body = body) }
			)

			When("BadRequestException with errors") {
				val requestErrors = listOf(
					RequestError(description = "bad format in field 1"),
					RequestError(description = "bad format in field 2")
				)

				val e = BadRequestException(
					errors = requestErrors
				)

				val result = errorMapper.mapException(e)

				Then("Mapped result") {
					result.status shouldBe 400
					result.body.shouldBeTypeOf<BadRequestErrorDto>()
					result.body.code shouldBe "bad_request"
					result.body.errors shouldContainAll requestErrors
				}
			}

			When("BadRequestException without errors") {
				val e = BadRequestException(
					errors = listOf()
				)

				val result = errorMapper.mapException(e)

				Then("Mapped result") {
					result.status shouldBe 400
					result.body.shouldBeTypeOf<BadRequestErrorDto>()
					result.body.code shouldBe "bad_request"
					result.body.errors.shouldBeEmpty()
				}
			}

			When("NotFoundException with message") {
				val e = NotFoundException(
					message = "My resource was not found"
				)

				val result = errorMapper.mapException(e)

				Then("Mapped result") {
					result.status shouldBe 404
					result.body.shouldBeTypeOf<DefaultErrorDto>()
					result.body.code shouldBe "not_found"
					result.body.message shouldBe "My resource was not found"
				}
			}

			When("NotFoundException without message") {
				val e = NotFoundException()

				val result = errorMapper.mapException(e)

				Then("Mapped result") {
					result.status shouldBe 404
					result.body.shouldBeTypeOf<DefaultErrorDto>()
					result.body.code shouldBe "not_found"
					result.body.message shouldBe "Resource not found"
				}
			}

			When("BusinessException with message") {
				val e = object: BusinessException(
					message = "Business validation failed"
				) {}

				val result = errorMapper.mapException(e)

				Then("Mapped result") {
					result.status shouldBe 422
					result.body.shouldBeTypeOf<DefaultErrorDto>()
					result.body.code shouldBe "business_error"
					result.body.message shouldBe "Business validation failed"
				}
			}

			When("BusinessException without message") {
				val e = object: BusinessException() {}

				val result = errorMapper.mapException(e)

				Then("Mapped result") {
					result.status shouldBe 422
					result.body.shouldBeTypeOf<DefaultErrorDto>()
					result.body.code shouldBe "business_error"
					result.body.message shouldBe "Something went wrong"
				}
			}

			When("TimeoutException with message") {
				val e = TimeoutException(
					message = "Timeout doing something"
				)

				val result = errorMapper.mapException(e)

				Then("Mapped result") {
					result.status shouldBe 500
					result.body.shouldBeTypeOf<DefaultErrorDto>()
					result.body.code shouldBe "server_timeout"
					result.body.message shouldBe "The server timed out when communicating with another service"
				}
			}

			When("TimeoutException without message") {
				val e = TimeoutException()

				val result = errorMapper.mapException(e)

				Then("Mapped result") {
					result.status shouldBe 500
					result.body.shouldBeTypeOf<DefaultErrorDto>()
					result.body.code shouldBe "server_timeout"
					result.body.message shouldBe "The server timed out when communicating with another service"
				}
			}

			When("CommunicationException with message") {
				val e = CommunicationException(
					message = "Communication error when calling redis or something"
				)

				val result = errorMapper.mapException(e)

				Then("Mapped result") {
					result.status shouldBe 500
					result.body.shouldBeTypeOf<DefaultErrorDto>()
					result.body.code shouldBe "server_error"
					result.body.message shouldBe "The server failed to establish connection with another service"
				}
			}

			When("CommunicationException without message") {
				val e = CommunicationException()

				val result = errorMapper.mapException(e)

				Then("Mapped result") {
					result.status shouldBe 500
					result.body.shouldBeTypeOf<DefaultErrorDto>()
					result.body.code shouldBe "server_error"
					result.body.message shouldBe "The server failed to establish connection with another service"
				}
			}

			When("Any other exception with message") {
				val e = UnexpectedException(
					message = "Something went wrong and i don't know what"
				)

				val result = errorMapper.mapException(e)

				Then("Mapped result") {
					result.status shouldBe 500
					result.body.shouldBeTypeOf<DefaultErrorDto>()
					result.body.code shouldBe "server_error"
					result.body.message shouldBe "Something went wrong"
				}
			}

			When("Any other exception without message") {
				val e = UnexpectedException()

				val result = errorMapper.mapException(e)

				Then("Mapped result") {
					result.status shouldBe 500
					result.body.shouldBeTypeOf<DefaultErrorDto>()
					result.body.code shouldBe "server_error"
					result.body.message shouldBe "Something went wrong"
				}
			}
		}
	}
}
