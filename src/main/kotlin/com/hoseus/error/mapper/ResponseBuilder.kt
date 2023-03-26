package com.hoseus.error.mapper

/**
 * Builds a response [R] from a http status and body [B].
 */
fun interface ResponseBuilder<B, R> {
	/**
	 * @param status The http status of the response.
	 * @param body The body [B] of the response.
	 * @return Response [R].
	 */
	fun build(status: Int, body: B): R
}
