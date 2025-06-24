package upm.etsit.monolito_backend.models
import jakarta.persistence.*
import upm.etsit.monolito_backend.dto.ProductCategoryDto
import java.time.LocalDateTime
import java.util.UUID




@Entity
@Table(name = "product_categories")
data class ProductCategoryEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    val uuid: UUID = UUID.randomUUID(),
    @Column(nullable = false, unique = true)
    val name: String = "",


    val createdAt: LocalDateTime = LocalDateTime.now(),

    val createdBy: UUID = UUID.randomUUID(), // ForeignKey a UserEntity.UUID
    val updatedAt: LocalDateTime? = null,
    val updatedBy: UUID? = null
)


fun ProductCategoryEntity.prodCategoryEntityToDto(): ProductCategoryDto {
    return ProductCategoryDto(this.uuid, this.name);
}