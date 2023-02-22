package com.hoseus.error.exception

abstract class ServerErrorException(
	message: String? = null,
	cause: Throwable? = null
): HoseusException(message = message, cause = cause)
