package upm.etsit.monolito_backend.models

import jakarta.persistence.*
import upm.etsit.monolito_backend.dto.ProductDto
import java.time.LocalDateTime
import java.util.UUID


@Entity
@Table(name = "products")
data class ProductEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    val uuid: UUID = UUID.randomUUID(),


    @Column(nullable = false, unique = false)
    val name: String= "",

    @Column(nullable = false, unique = false)
    val description: String = "",

    @Column(nullable = false)
    val price: Double = 0.0,

    @Column(nullable = false)
    val stock: Int = 0,

    val image: String= "", 

    @Column(nullable = false)
    val categoryUUID:UUID =  UUID.randomUUID(), // FK a ProductCategoryEntity.id


)

fun ProductEntity.toProductDto(): ProductDto{
    return ProductDto(uuid = this.uuid, name = this.name,  description = this.description , price = this.price, stock = this.stock , image= this.image, categoryUUID = this.categoryUUID )
}
