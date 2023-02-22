package com.hoseus.error

import com.hoseus.error.exception.*
import com.hoseus.error.mapper.DefaultErrorMapper
import com.hoseus.error.mapper.ErrorMapper
import com.hoseus.error.mapper.ResponseBuilder
import com.hoseus.error.model.BadRequestErrorDto
import com.hoseus.error.model.DefaultErrorDto
import com.hoseus.error.model.ErrorDto
import com.hoseus.error.model.RequestError
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContainAll
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeTypeOf

class ResponseBuilderSpec: BehaviorSpec() {
	init {
		Given("ResponseBuilder") {
			data class Response<T> (
				val status: Int,
				val body: T
			)

			val responseBuilder = ResponseBuilder<ErrorDto, Response<ErrorDto>>{ status, body ->
				Response(status = status, body = body)
			}

			When("Passed status and body") {
				val status = 500
				val body = DefaultErrorDto(code = "server_error", message = "Something went wrong")

				val result = responseBuilder.build(status, body)

				Then("Created response") {
					result.status shouldBe status
					result.body shouldBe body
				}
			}
		}
	}
}
