package com.hoseus.error.mapper

/**
 * Maps an exception to a response of type [T].
 */
fun interface ErrorMapper<T> {
	/**
	 * @param e The exception.
	 * @return The response [T].
	 */
	fun mapException(e: Throwable): T
}
