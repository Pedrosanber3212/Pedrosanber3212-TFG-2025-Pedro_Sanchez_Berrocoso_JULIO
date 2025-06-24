package upm.etsit.monolito_backend.services

import org.springframework.stereotype.Service
import upm.etsit.monolito_backend.dto.CreateProductCategoryRequest
import upm.etsit.monolito_backend.dto.ProductCategoryDto
import upm.etsit.monolito_backend.dto.UpedateProductCategoryRequest
import upm.etsit.monolito_backend.exception.CustomException
import upm.etsit.monolito_backend.exception.CustomResourceNotFoundException
import upm.etsit.monolito_backend.models.ProductCategoryEntity
import upm.etsit.monolito_backend.repositories.ProductCategoriesRepository
import upm.etsit.monolito_backend.repositories.ProductRepository
import java.util.UUID

@Service
class ProductCategoriesService(
    private val productCategoriesRepository: ProductCategoriesRepository,
    private val productRepository: ProductRepository
) {

    fun getAll(): List<ProductCategoryDto> =
        productCategoriesRepository.findAll()
            .map { it.toDto() }

    fun getByUuid(uuid: UUID): ProductCategoryDto {
        val category = productCategoriesRepository.findByUuid(uuid)
            ?: throw CustomResourceNotFoundException("Category with UUID $uuid not found")
        return category.toDto()
    }

    fun createCategory(request: CreateProductCategoryRequest): ProductCategoryDto {
        if (productCategoriesRepository.existsByName(request.name)) {
            throw IllegalArgumentException("Category with name ${request.name} already exists")
        }

        val entity = ProductCategoryEntity(
            uuid = UUID.randomUUID(),
            name = request.name

        )

        return productCategoriesRepository.save(entity).toDto()
    }

    fun updateCategory(request: UpedateProductCategoryRequest): ProductCategoryDto {
        val existing = productCategoriesRepository.findByUuid(request.uuid)
            ?: throw CustomResourceNotFoundException("Category with UUID ${request.uuid} not found")

        val updated = existing.copy(name = request.name)
        return productCategoriesRepository.save(updated).toDto()
    }

    fun deleteCategory(uuid: UUID) {
        val category = productCategoriesRepository.findByUuid(uuid)
            ?: throw CustomResourceNotFoundException("Category with UUID $uuid not found")

        val hasProducts = productRepository.existsByCategoryUUID(uuid)
        if (hasProducts) {
            throw CustomException("No se ha podido borrar la categoría ${category.name} debido a que aun existen productos en el sistema con dicha categoría")
        }

        productCategoriesRepository.delete(category)
    }

    private fun ProductCategoryEntity.toDto(): ProductCategoryDto =
        ProductCategoryDto(uuid = uuid, name = name)
}