package upm.etsit.monolito_backend.models
import jakarta.persistence.*

import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID


@Entity
@Table(name = "users")
data class UserEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    val uuid: UUID = UUID.randomUUID(),

    @Column(nullable = false, unique = true)
    val username: String = "",

    @Column(nullable = false)
    val name: String = "",

    val birthdate: LocalDate = LocalDate.now(),

    @Column(nullable = false, unique = false)
    val email: String = "",

    @Column(nullable = false, unique = false)
    val address: String = "",



    @Column(nullable = false)
    val password: String = "",

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    val role: UserRole = UserRole.CUSTOMER,




)




enum class UserRole {
    ADMIN, CUSTOMER
}



