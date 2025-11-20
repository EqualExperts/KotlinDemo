package za.co.ee.learning.domain.security

import za.co.ee.learning.domain.DomainResult

interface PasswordProvider {
    fun encode(password: String): DomainResult<String>

    fun matches(
        password: String,
        encodedPassword: String,
    ): Boolean
}
