package com.hoseus.error.exception

class CommunicationException(
	message: String? = null,
	cause: Throwable? = null
): ServerErrorException(message = message, cause = cause)
