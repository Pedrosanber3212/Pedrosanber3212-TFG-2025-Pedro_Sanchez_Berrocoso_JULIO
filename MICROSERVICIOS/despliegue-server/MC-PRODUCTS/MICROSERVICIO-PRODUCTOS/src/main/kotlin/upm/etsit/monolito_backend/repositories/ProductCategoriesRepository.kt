package upm.etsit.monolito_backend.repositories

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import upm.etsit.monolito_backend.models.ProductCategoryEntity
import upm.etsit.monolito_backend.models.ProductEntity

import java.util.*

@Repository
interface ProductCategoriesRepository: JpaRepository<ProductCategoryEntity, Long> {


    fun findByName(username: String): ProductCategoryEntity?
    fun findByUuid(uuid: UUID): ProductCategoryEntity?
    fun existsByName(name: String): Boolean


}