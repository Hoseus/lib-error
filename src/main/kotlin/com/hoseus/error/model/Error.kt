package com.hoseus.error.model

interface ErrorDto {
	val code: String
}

data class DefaultErrorDto(
	override val code: String,
	val message: String,
): ErrorDto

data class BadRequestErrorDto(
	override val code: String,
	val errors: List<RequestError>,
): ErrorDto

data class RequestError(
	val description: String,
)
