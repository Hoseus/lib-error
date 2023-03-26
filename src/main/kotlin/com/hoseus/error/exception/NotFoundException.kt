package com.hoseus.error.exception

class NotFoundException(
	message: String? = null,
	cause: Throwable? = null
): HoseusException(message = message, cause = cause)
