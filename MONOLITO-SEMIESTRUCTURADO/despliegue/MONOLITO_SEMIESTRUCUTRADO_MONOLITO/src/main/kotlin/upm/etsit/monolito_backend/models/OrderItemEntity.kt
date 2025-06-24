package upm.etsit.monolito_backend.models

import jakarta.persistence.*
import java.util.*


@Entity
@Table(name = "order_items")
data class OrderItemEntity (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    @Column(nullable = false, unique = true)
    val uuid: UUID = UUID.randomUUID(),
    @Column(nullable = false)
    val orderUUID: UUID =  UUID.randomUUID(), // FK a OrderEntity.UUID

    @Column(nullable = false)
    val productUUID: UUID =  UUID.randomUUID(), // FK a ProductEntity.UUID

    @Column(nullable = false)
    val quantity: Int  =0 ,

    @Column(nullable = false)
    val price: Double = 0.0


)