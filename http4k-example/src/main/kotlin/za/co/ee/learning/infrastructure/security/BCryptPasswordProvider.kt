package za.co.ee.learning.infrastructure.security

import arrow.core.raise.either
import org.mindrot.jbcrypt.BCrypt
import za.co.ee.learning.domain.DomainError
import za.co.ee.learning.domain.DomainResult
import za.co.ee.learning.domain.security.PasswordProvider

class BCryptPasswordProvider : PasswordProvider {
    override fun encode(password: String): DomainResult<String> =
        either {
            try {
                BCrypt.hashpw(password, BCrypt.gensalt())
            } catch (e: Exception) {
                raise(DomainError.ValidationError("Error encoding password: ${e.message}"))
            }
        }

    override fun matches(
        password: String,
        encodedPassword: String,
    ): Boolean = BCrypt.checkpw(password, encodedPassword)
}
