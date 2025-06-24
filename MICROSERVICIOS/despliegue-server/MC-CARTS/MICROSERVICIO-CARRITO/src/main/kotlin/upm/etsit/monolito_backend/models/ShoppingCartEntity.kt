package upm.etsit.monolito_backend.models
import jakarta.persistence.*
import java.time.LocalDateTime
import java.util.UUID


@Entity
@Table(name = "shopping_carts")
data class ShoppingCartEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    @Column(nullable = false, unique = true)
    val uuid: UUID = UUID.randomUUID(),

    @Column(nullable = false, unique= true)
    val userUUID: UUID= UUID.randomUUID()

)

