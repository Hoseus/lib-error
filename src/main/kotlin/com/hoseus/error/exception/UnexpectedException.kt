package com.hoseus.error.exception

class UnexpectedException(
	message: String? = "Something went wrong",
	cause: Throwable? = null
): ServerErrorException(message = message, cause = cause)
