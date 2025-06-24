package upm.etsit.monolito_backend.controllers

import UserCredentialsDto
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import upm.etsit.monolito_backend.dto.CreateProductCategoryRequest
import upm.etsit.monolito_backend.dto.ProductCategoryDto
import upm.etsit.monolito_backend.dto.UpedateProductCategoryRequest
import upm.etsit.monolito_backend.exception.CustomAccessDeniedException
import upm.etsit.monolito_backend.services.ProductCategoriesService
import java.util.*


@RestController
@RequestMapping("/api/v1/productcategories")
class ProductCategoriesController(
    private val productCategoriesService: ProductCategoriesService
) {

    /**
     *PRUEBA LLAMADA AL MONOLITO
     * interceptar cookie y usarla para llaamra a user microservice
     */

    @GetMapping("/prueba")
    fun get(@CookieValue("JSESSIONID") sessionCookie: String): ResponseEntity<Any> {
        System.out.println("prueba")
        val credentials = productCategoriesService.getMyCredentials(sessionCookie)
        return ResponseEntity.ok(credentials)
    }

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
    //@PreAuthorize("hasRole('ADMIN')")
    fun createCategory(
        @RequestBody request: CreateProductCategoryRequest
        ,@CookieValue("JSESSIONID") sessionCookie: String
    ): ResponseEntity<ProductCategoryDto> {
        var credentials:UserCredentialsDto =  this.productCategoriesService.getMyCredentials(sessionCookie)
        if(credentials.authorities[0].authority != "ROLE_ADMIN"){
            throw CustomAccessDeniedException("No tiene el rol ADMIN")
        }

        val created = productCategoriesService.createCategory(request)
        return ResponseEntity.ok(created)
    }

    /**
     * UPDATE product category
     */
    @PutMapping
    // @PreAuthorize("hasRole('ADMIN')")
    fun updateCategory(
        @RequestBody request: UpedateProductCategoryRequest
        ,@CookieValue("JSESSIONID") sessionCookie: String
    ): ResponseEntity<ProductCategoryDto> {
       var credentials:UserCredentialsDto =  this.productCategoriesService.getMyCredentials(sessionCookie)
        if(credentials.authorities[0].authority != "ROLE_ADMIN"){
            throw CustomAccessDeniedException("No tiene el rol ADMIN")
        }
        val updated = productCategoriesService.updateCategory(request)
        return ResponseEntity.ok(updated)
    }

    /**
     * DELETE category by UUID (if no products exist in the category)
     */
    @DeleteMapping("/{uuid}")
    // @PreAuthorize("hasRole('ADMIN')")
    fun deleteCategory(@PathVariable uuid: UUID): ResponseEntity<Any> {

        return ResponseEntity.ok(productCategoriesService.deleteCategory(uuid))
    }
}
