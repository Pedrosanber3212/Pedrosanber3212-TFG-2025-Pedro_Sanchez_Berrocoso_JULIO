package upm.etsit.monolito_backend.dto



import upm.etsit.monolito_backend.models.OrderStatus
import java.time.LocalDate
import java.util.*
data class OrderDto(
    val uuid: UUID,
    val userUUID: UUID,
    val creationDate: LocalDate,
    val items: List<OrderItemDto>,
    val status: OrderStatus
)




data class OrderItemDto(
    val uuid: UUID,
    val orderUUID: UUID,
    val productUUID: UUID,
    val quantity: Int,
    val price: Double,
    val image: String,
    val name: String,
)
