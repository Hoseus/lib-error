package com.hoseus.error.exception

class TimeoutException(
	message: String? = null,
	cause: Throwable? = null
): ServerErrorException(message = message, cause = cause)
