package upm.etsit.monolito_backend.models

import jakarta.persistence.*
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID


@Entity
@Table(name = "orders")
data class OrderEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    @Column(nullable = false, unique = true)
    val uuid: UUID = UUID.randomUUID(),

    @Column(nullable = false)
    val userUUID: UUID = UUID.randomUUID(),  // userUUID del pedido

    @Column(nullable = false)
    val creationDate : LocalDate = LocalDate.now(),

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    val orderStatus: OrderStatus =  OrderStatus.PENDING

)


enum class OrderStatus{
    PENDING, PROCESSING, SHIPPED , DELIVERED, CANCELLED
}