package upm.etsit.monolito_backend.dto

import upm.etsit.monolito_backend.models.ShoppingCartItemEntity
import java.time.LocalDate
import java.util.*


data class ShoppingCartDto(
    val uuid: UUID,
    val userUUID: UUID,
    val items: MutableList<ShoppingCartItemDto>
)

data class ShoppingCartItemDto(
    val uuid: UUID,
    val productUUID: UUID,
    val image: String,
    val name: String,
    val shoppingCartUUID: UUID,
    val price: Double,
    val quantity: Int
)






