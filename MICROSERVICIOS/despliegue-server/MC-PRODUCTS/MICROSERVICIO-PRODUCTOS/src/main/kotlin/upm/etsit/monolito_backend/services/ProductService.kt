package upm.etsit.monolito_backend.services

import UserCredentialsDto
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.web.client.RestClient
import org.springframework.web.reactive.function.client.WebClient
import upm.etsit.monolito_backend.dto.*
import upm.etsit.monolito_backend.exception.CustomResourceNotFoundException
import upm.etsit.monolito_backend.models.ProductEntity
import upm.etsit.monolito_backend.models.toProductDto
import upm.etsit.monolito_backend.repositories.ProductRepository
import java.util.*

@Service
class ProductService(
    private val productRepository: ProductRepository,
    @Value("\${URL_MC_USERS}")
    private val userServiceBaseUrl: String,
    private val restClient: RestClient,
   // private val shoppingCartBaseUrl: String,
) {

    fun getMyCredentials(cookie: String): UserCredentialsDto {
        val fullCookie = "JSESSIONID=$cookie"
        println("Enviando cookie: $fullCookie")

        return restClient.get()
            .uri("$userServiceBaseUrl/api/v1/users/me3")
            .header("Cookie", fullCookie)
            .retrieve()
            .body(UserCredentialsDto::class.java)!!
    }

    fun findProductByUuid(uuid: UUID): ProductDto =
        productRepository.findByUuid(uuid)?.toProductDto()
            ?: throw CustomResourceNotFoundException("Producto con uuid $uuid no encontrado")


    fun findAllProductsByName(name: String): List<ProductDto> =
        productRepository.findAllByName(name).map { it.toProductDto() }

    fun getFilteredProducts(
        name: String?,
        minPrice: Double?,
        maxPrice: Double?,
        categoryUUID: UUID?,
        pageable: Pageable
    ): Page<ProductEntity> {
        return productRepository.findByFilters(name, minPrice, maxPrice, categoryUUID, pageable)
    }


    fun createProduct(request: CreateProductRequest): ProductDto {


        val entity = request.toProductEntity()      // extensión declarada más abajo
        return productRepository.save(entity).toProductDto()
    }

    fun updateProductByUuid(
        uuid: UUID,
        request: UpdateProductRequest
    ): ProductDto {

        val productEntity = productRepository.findByUuid(uuid)
            ?: throw CustomResourceNotFoundException("Producto con uuid $uuid no encontrado")

        val updated = productEntity.copy(
            name         = request.name,
            description  = request.description,
            price        = request.price,
            stock        = request.stock,
            image        = request.image,
            categoryUUID = request.categoryUUID
        )

        return productRepository.save(updated).toProductDto()
    }



    fun deleteProductByUuid(
        uuid: UUID
    ) {


        val entity = productRepository.findByUuid(uuid)
            ?: throw CustomResourceNotFoundException("Producto con uuid $uuid no encontrado")

        productRepository.delete(entity)
       // this.shoppingCartService.deleteAllShoppingItemsByProductUUID(uuid)

    }


    fun findProductEntityByUuid(uuid: UUID): ProductEntity {
        return productRepository.findByUuid(uuid)
            ?: throw CustomResourceNotFoundException("Producto con UUID $uuid no encontrado")
    }

}


