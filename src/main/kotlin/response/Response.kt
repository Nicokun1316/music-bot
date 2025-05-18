package io.github.nicokun1316.response

sealed interface Response<out T> {
    val value: T
}