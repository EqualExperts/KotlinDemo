package za.co.ee.learning.domain.users

import arrow.core.Either
import za.co.ee.learning.domain.DomainError
import za.co.ee.learning.domain.DomainResult

interface UserRepository {
    fun findByEmail(email: String): DomainResult<User>

    fun findAll(): DomainResult<List<User>>
}
