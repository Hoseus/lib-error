package com.hoseus.error.exception

import com.hoseus.error.model.RequestError

class BadRequestException(
	val errors: List<RequestError>,
	cause: Throwable? = null,
): HoseusException(message = null, cause = cause)
