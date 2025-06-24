data class UserCredentialsDto(
    val password: String?,                    // normalmente null
    val username: String,
    val authorities: List<AuthorityDto>,
    val accountNonExpired: Boolean,
    val accountNonLocked: Boolean,
    val credentialsNonExpired: Boolean,
    val enabled: Boolean
)

data class AuthorityDto(
    val authority: String                     // ejemplo: "ROLE_CUSTOMER"
)
