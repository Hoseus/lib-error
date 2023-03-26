package com.hoseus.error.mapper

import com.hoseus.error.model.BadRequestErrorDto
import com.hoseus.error.model.DefaultErrorDto
import com.hoseus.error.model.ErrorDto
import com.hoseus.error.exception.*

/**
 * [ErrorMapper] that offers a default response for each type of exception.
 * A [ResponseBuilder] must be provided for building the responses.
 * Can be extended to override or add behaviour.
 *
 */
open class DefaultErrorMapper<T>(
	protected val responseBuilder: ResponseBuilder<ErrorDto, T>
): ErrorMapper<T> {
	override fun mapException(e: Throwable): T =
		when(e) {
			is BadRequestException -> this.responseBuilder.build(
				status = 400,
				body = BadRequestErrorDto(code = "bad_request", errors = e.errors.distinct()),
			)
			is NotFoundException -> this.responseBuilder.build(
				status = 404,
				body = DefaultErrorDto(code = "not_found", message = e.message?:"Resource not found")
			)
			is BusinessException -> this.responseBuilder.build(
				status = 422,
				body = DefaultErrorDto(code = "business_error",  message = e.message?:"Something went wrong")
			)
			is TimeoutException -> this.responseBuilder.build(
				status = 500,
				body = DefaultErrorDto(code = "server_timeout", message = "The server timed out when communicating with another service")
			)
			is CommunicationException -> this.responseBuilder.build(
				status = 500,
				body = DefaultErrorDto(code = "server_error", message = "The server failed to establish connection with another service")
			)
			else -> this.responseBuilder.build(
				status = 500,
				body = DefaultErrorDto(code = "server_error", message = "Something went wrong")
			)
	}
}
