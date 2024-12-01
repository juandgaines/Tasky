package com.juandgaines.core.data.auth

import android.util.Patterns
import com.juandgaines.core.domain.auth.PatternValidator

object EmailPatternValidator: PatternValidator {
    override fun matches(value: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(value).matches()
    }
}