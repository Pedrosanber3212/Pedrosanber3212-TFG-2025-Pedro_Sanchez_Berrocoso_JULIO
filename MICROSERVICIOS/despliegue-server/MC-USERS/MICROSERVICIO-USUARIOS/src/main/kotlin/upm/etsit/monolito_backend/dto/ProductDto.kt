package upm.etsit.monolito_backend.dto

import upm.etsit.monolito_backend.models.ProductEntity
import java.time.LocalDate
import java.util.*


/**
 * DTO-ProductCategory!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
 */
data class ProductCategoryDto(
    val uuid: UUID,
    val name: String,
)

data class CreateProductCategoryRequest(
    val name: String,
)


data class UpedateProductCategoryRequest(
    val uuid: UUID,
    val name: String,
)
/**
 * product!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
  */
//devuelto al pedir un producto
data class ProductDto(
    val uuid: UUID,
    val name: String,
    val description: String,
    val price: Double,
    val stock: Int,
    val image: String,
    val categoryUUID: UUID
)


fun CreateProductRequest.toProductEntity() = ProductEntity(
    name         = name,
    description  = description,
    price        = price,
    stock        = stock,
    image        = image,
    categoryUUID = categoryUUID
)
fun ProductDto.toProductEntity():ProductEntity{
    return ProductEntity()
}

data class CreateProductRequest(
    val name: String,
    val description: String,
    val price: Double,
    val stock: Int,
    val image: String,
    val categoryUUID: UUID
)

data class UpdateProductRequest(
    val name: String,
    val description: String,
    val price: Double,
    val stock: Int,
    val image: String,
    val categoryUUID: UUID
)
