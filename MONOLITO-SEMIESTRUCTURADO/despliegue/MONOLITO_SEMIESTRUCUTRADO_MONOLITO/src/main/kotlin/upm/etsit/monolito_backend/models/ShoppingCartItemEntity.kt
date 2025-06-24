package upm.etsit.monolito_backend.models
import jakarta.persistence.*
import upm.etsit.monolito_backend.dto.ShoppingCartItemDto
import java.time.LocalDateTime
import java.util.UUID


@Entity
    @Table(name = "shopping_cart_items")
data class ShoppingCartItemEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    val uuid: UUID = UUID.randomUUID(),

    @Column(nullable = false, unique = false)
    val shoppingCartUUID: UUID = UUID.randomUUID(),


    @Column(nullable = false, unique = false)
    val productUUID: UUID=  UUID.randomUUID(),


    @Column(nullable = false)
    val quantity: Int= 0,

    @Column(nullable = false)
    val price: Double = 0.0,

    )


fun ShoppingCartItemEntity.toShoppingCartEntityDto(product: ProductEntity): ShoppingCartItemDto{
    return ShoppingCartItemDto(uuid = this.uuid,  productUUID = this.productUUID , price = product.price, quantity = this.quantity,
       image= product.image ,name= product.name, shoppingCartUUID = this.shoppingCartUUID )
}
