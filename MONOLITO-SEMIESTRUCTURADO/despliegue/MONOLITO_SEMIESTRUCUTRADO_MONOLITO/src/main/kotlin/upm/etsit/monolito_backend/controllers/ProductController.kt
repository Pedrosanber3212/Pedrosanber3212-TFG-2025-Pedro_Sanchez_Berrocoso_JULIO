package upm.etsit.monolito_backend.controllers

import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.*
import upm.etsit.monolito_backend.dto.*
import upm.etsit.monolito_backend.models.ProductEntity
import upm.etsit.monolito_backend.models.UserRole
import upm.etsit.monolito_backend.models.toProductDto
import upm.etsit.monolito_backend.services.ProductService
import java.util.*


@RestController
@RequestMapping("/api/v1/products")
class ProductController(
    private val productService: ProductService
) {


    @GetMapping("/{uuid}")
    fun getProduct(@PathVariable uuid: UUID, @AuthenticationPrincipal currentUser: UserDetails): ResponseEntity<Any> {
        return ResponseEntity.ok(productService.findProductByUuid(uuid) )
    }

    @GetMapping("/page")
        fun getPageProducts(
            @RequestParam(required = false) name: String?,
            @RequestParam(required = false) minPrice: Double?,
            @RequestParam(required = false) maxPrice: Double?,
            @RequestParam(required = false) categoryUUID: UUID?,
            @RequestParam(required = true) page: Int,
            @RequestParam(required = true) size: Int,
            @RequestParam(required = true) sort: String
        ): ResponseEntity<List<ProductDto>> {
        val sortParts = sort.split(",")
        val sortField = sortParts.getOrElse(0) { "name" } // por defecto: name
        val sortDirection = sortParts.getOrNull(1)?.uppercase() ?: "ASC" // por defecto: ASC

        val pageable = PageRequest.of(
            page ?: 0,
            size ?: 15,
            if (sortDirection == "DESC") Sort.by(sortField).descending()
            else Sort.by(sortField).ascending()
        )
        System.out.println( "______" + name +  minPrice +  maxPrice +  categoryUUID   +  page + size  )
        val productsPage = productService.getFilteredProducts(
            name, minPrice, maxPrice, categoryUUID, pageable
        )

        val productDtoList = productsPage.content.map { it.toProductDto() }

        return ResponseEntity.ok(productDtoList)
        }





    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    fun post(
        @AuthenticationPrincipal currentUser: UserDetails,
        @RequestBody createProductRequest: CreateProductRequest
    ): ResponseEntity<ProductDto> {
        val productDto = productService.createProduct( createProductRequest     )
        return ResponseEntity.ok(productDto);
    }

    /**
     * UPDATE - PRODUCTO
     * SOLO SE PUEDE HACER SIEMPRE QUE no existan ordenes de compra con este producto en estado diferente de SHIPPED
     * Y ademas si se cumple lo anterior hay que actaulizar todos los shoppingcartItems qeu contengan dicho producto
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{uuid}")
    fun updateProduct(
        @PathVariable uuid: UUID,
        @RequestBody updateProductRequest: UpdateProductRequest,
        @AuthenticationPrincipal currentUser: UserDetails
    ): ResponseEntity<ProductDto> {

        return ResponseEntity.ok(productService.updateProductByUuid(uuid , updateProductRequest))
    }

    /**
     * delete -
     * solo se puede hacer cuando no existan ordenes de compra con este producto en estado diferente de SHIPPED
     * y Y ademas si se cumple lo anterior hay que borrar todos aquellos shoppingCartItems que tengan dicho producto en su interior
     */
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{uuid}")
    fun deleteProduct(
        @PathVariable uuid: UUID,
        @AuthenticationPrincipal currentUser: UserDetails
    ): ResponseEntity<Void> {
        productService.deleteProductByUuid(uuid )
        return ResponseEntity.ok(null);
    }


}