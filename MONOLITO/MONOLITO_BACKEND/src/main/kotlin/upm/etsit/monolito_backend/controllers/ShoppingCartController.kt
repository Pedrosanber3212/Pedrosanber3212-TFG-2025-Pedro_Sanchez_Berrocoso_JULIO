package upm.etsit.monolito_backend.controllers

import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import upm.etsit.monolito_backend.dto.ShoppingCartDto
import upm.etsit.monolito_backend.services.ShoppingCartService
import java.util.*

@RestController
@RequestMapping("/api/v1/shoppingCart")
class ShoppingCartController(
    private val shoppingCartService: ShoppingCartService
) {

    @GetMapping
    fun getItemsCart(
        @AuthenticationPrincipal user: UserDetails
    ): ResponseEntity<ShoppingCartDto> {
        val shoppingCartDto: ShoppingCartDto  = shoppingCartService.getItemsCart(user)
        return ResponseEntity.ok(shoppingCartDto)
    }
    @PostMapping("/{productUUID}")
    fun addItemToCart(
        @AuthenticationPrincipal user: UserDetails
        ,@PathVariable productUUID: UUID
    ): ResponseEntity<ShoppingCartDto> {
        val shoppingCartDto: ShoppingCartDto  = shoppingCartService.addItemToCart(user, productUUID)
        return ResponseEntity.ok(shoppingCartDto)
    }

    
    @DeleteMapping("/{shoppingCartItemUUID}")
    fun removeItemFromCart(
        @AuthenticationPrincipal user: UserDetails
        ,@PathVariable shoppingCartItemUUID: UUID
    ): ResponseEntity<ShoppingCartDto> {
        val shoppingCartDto: ShoppingCartDto  = shoppingCartService.removeItemFromCart(user, shoppingCartItemUUID)
        return ResponseEntity.ok(shoppingCartDto)
    }

    
    @DeleteMapping
    fun clearCart(@AuthenticationPrincipal user: UserDetails): ResponseEntity<Any> {
        shoppingCartService.clearCart(user)
        return ResponseEntity.ok("");
    }
}


