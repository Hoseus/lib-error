package com.hoseus.error.exception

abstract class HoseusException(
	message: String? = null,
	cause: Throwable? = null
): RuntimeException(message, cause)
