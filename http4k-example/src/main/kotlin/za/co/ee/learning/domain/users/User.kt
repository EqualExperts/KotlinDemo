package za.co.ee.learning.domain.users

import za.co.ee.learning.domain.DomainResult
import java.util.UUID

data class User(
    val id: UUID,
    val email: String,
    val passwordHash: DomainResult<String>,
)
