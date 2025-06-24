package upm.etsit.monolito_backend.controllers

import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import upm.etsit.monolito_backend.dto.CreateProductCategoryRequest
import upm.etsit.monolito_backend.dto.ProductCategoryDto
import upm.etsit.monolito_backend.dto.UpedateProductCategoryRequest
import upm.etsit.monolito_backend.services.ProductCategoriesService
import upm.etsit.monolito_backend.services.UserService
import java.util.*


@RestController
@RequestMapping("/api/v1/productcategories")
class ProductCategoriesController(
    private val productCategoriesService: ProductCategoriesService
) {

    /**
     * GET all product categories
     */
    @GetMapping
    fun getAll(): ResponseEntity<List<ProductCategoryDto>> {
        val categories = productCategoriesService.getAll()
        return ResponseEntity.ok(categories)
    }

    /**
     * GET category by UUID
     */
    @GetMapping("/{uuid}")
    fun getByUuid(@PathVariable uuid: UUID): ResponseEntity<ProductCategoryDto> {
        val category = productCategoriesService.getByUuid(uuid)
        return ResponseEntity.ok(category)
    }

    /**
     * CREATE product category
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    fun createCategory(
        @RequestBody request: CreateProductCategoryRequest
    ): ResponseEntity<ProductCategoryDto> {
        val created = productCategoriesService.createCategory(request)
        return ResponseEntity.ok(created)
    }

    /**
     * UPDATE product category
     */
    @PutMapping
    @PreAuthorize("hasRole('ADMIN')")
    fun updateCategory(
        @RequestBody request: UpedateProductCategoryRequest
    ): ResponseEntity<ProductCategoryDto> {
        val updated = productCategoriesService.updateCategory(request)
        return ResponseEntity.ok(updated)
    }

    /**
     * DELETE category by UUID (if no products exist in the category)
     */
    @DeleteMapping("/{uuid}")
    @PreAuthorize("hasRole('ADMIN')")
    fun deleteCategory(@PathVariable uuid: UUID): ResponseEntity<Any> {

        return ResponseEntity.ok(productCategoriesService.deleteCategory(uuid))
    }
}
