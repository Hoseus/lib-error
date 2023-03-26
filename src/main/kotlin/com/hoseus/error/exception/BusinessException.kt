package com.hoseus.error.exception

abstract class BusinessException(
	message: String? = null,
	cause: Throwable? = null
): HoseusException(message = message, cause = cause)
