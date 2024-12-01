package com.juandgaines.core.domain.auth

interface PatternValidator {
    fun matches(value: String): Boolean
}