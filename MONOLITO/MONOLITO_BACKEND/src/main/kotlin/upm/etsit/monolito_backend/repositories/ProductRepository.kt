package upm.etsit.monolito_backend.repositories

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import upm.etsit.monolito_backend.models.ProductEntity

import java.util.*

@Repository
interface ProductRepository: JpaRepository<ProductEntity,Long> {

    fun findByName(username: String): ProductEntity?
    fun findByUuid(uuid: UUID): ProductEntity?
    fun existsByName(name: String): Boolean

    fun findAllByName(name: String): MutableList<ProductEntity>

    fun existsByCategoryUUID(uuid: UUID): Boolean;


    @Query("""
        SELECT p FROM ProductEntity p
        WHERE 1 = 1 
            and (
                  :name IS NULL OR LOWER(p.name) LIKE CONCAT('%', lower(CAST(:name AS string)), '%')
            
                OR 

                    LOWER(p.description) LIKE CONCAT('%', lower(CAST(:name AS string)), '%')
            )       
            
        AND (:minPrice IS NULL OR p.price >= :minPrice)
        AND (:maxPrice IS NULL OR p.price <= :maxPrice)
        AND (:categoryUUID IS NULL OR CAST(p.categoryUUID AS string) = CAST(:categoryUUID AS string) )
    """)
    fun findByFilters(
        @Param("name") name: String?,
        @Param("minPrice") minPrice: Double?,
        @Param("maxPrice") maxPrice: Double?,
        @Param("categoryUUID") categoryUUID: UUID?,
        pageable: Pageable
    ): Page<ProductEntity>

}