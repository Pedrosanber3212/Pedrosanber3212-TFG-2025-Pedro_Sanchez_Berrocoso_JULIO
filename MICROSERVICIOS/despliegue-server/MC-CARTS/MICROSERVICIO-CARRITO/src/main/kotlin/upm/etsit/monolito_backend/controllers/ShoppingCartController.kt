package upm.etsit.monolito_backend.controllers

import org.springframework.http.ResponseEntity

import org.springframework.web.bind.annotation.*
import upm.etsit.monolito_backend.dto.ShoppingCartDto
import upm.etsit.monolito_backend.models.ShoppingCartEntity
import upm.etsit.monolito_backend.models.ShoppingCartItemEntity
import upm.etsit.monolito_backend.services.ShoppingCartService
import java.util.*

@RestController
@RequestMapping("/api/v1/shoppingCart")
class ShoppingCartController(
    private val shoppingCartService: ShoppingCartService
) {

    @GetMapping
    fun getItemsCart(
        //@AuthenticationPrincipal user: UserDetails
        @CookieValue("JSESSIONID") sessionCookie: String
    ): ResponseEntity<ShoppingCartDto> {
        val shoppingCartDto: ShoppingCartDto  = shoppingCartService.getItemsCart(sessionCookie)
        return ResponseEntity.ok(shoppingCartDto)
    }
    @PostMapping("/{productUUID}")
    fun addItemToCart(
        //@AuthenticationPrincipal user: UserDetails,
        @PathVariable productUUID: UUID
        ,@CookieValue("JSESSIONID") sessionCookie: String
    ): ResponseEntity<ShoppingCartDto> {
        val shoppingCartDto: ShoppingCartDto  = shoppingCartService.addItemToCart(sessionCookie, productUUID)
        return ResponseEntity.ok(shoppingCartDto)
    }

    /**
     * DELETE -  Eliminamos item del carrito por UUID del item
     */
    @DeleteMapping("/{shoppingCartItemUUID}")
    fun removeItemFromCart(
        //@AuthenticationPrincipal user: UserDetails,
        @PathVariable shoppingCartItemUUID: UUID
        ,@CookieValue("JSESSIONID") sessionCookie: String
    ): ResponseEntity<ShoppingCartDto> {
        val shoppingCartDto: ShoppingCartDto  = shoppingCartService.removeItemFromCart(sessionCookie, shoppingCartItemUUID)
        return ResponseEntity.ok(shoppingCartDto)
    }

    /**
     * DELETE-Vaciamos carrito completo
     */
    @DeleteMapping
    fun clearCart(
      //  @AuthenticationPrincipal user: UserDetails)
        @CookieValue("JSESSIONID") sessionCookie: String)
    : ResponseEntity<Any> {
        System.out.println("clearCartOriginalController")

        shoppingCartService.clearCart(sessionCookie)
        return ResponseEntity.ok("");
    }




    @GetMapping("/shoppingCartEntityByUserUUID/{userUUID}")
    fun ShoppingCartEntityByUserUUID(
        //@AuthenticationPrincipal user: UserDetails
        @CookieValue("JSESSIONID") sessionCookie: String,
        @PathVariable userUUID: UUID
    ): ResponseEntity<ShoppingCartEntity> {

        return ResponseEntity.ok(shoppingCartService.getCartEntityByCartUUID(userUUID));
    }




    @GetMapping("/shoppingCartItemEntityListByCartUUID/{cartUUID}")
    fun getShoppingCartItemsEntityListByShoppingCartUUID(
        //@AuthenticationPrincipal user: UserDetails
        @CookieValue("JSESSIONID") sessionCookie: String,
        @PathVariable cartUUID: UUID
    ): ResponseEntity<MutableList<ShoppingCartItemEntity>> {

        return ResponseEntity.ok(shoppingCartService.getCartItemsEntityListByCartUUID(cartUUID));
    }












}


