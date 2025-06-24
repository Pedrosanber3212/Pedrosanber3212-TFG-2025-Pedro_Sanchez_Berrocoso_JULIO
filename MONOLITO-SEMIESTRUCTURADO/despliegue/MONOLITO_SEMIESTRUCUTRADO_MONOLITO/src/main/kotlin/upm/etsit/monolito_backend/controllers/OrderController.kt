package upm.etsit.monolito_backend.controllers

import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.*
import upm.etsit.monolito_backend.dto.OrderDto
import upm.etsit.monolito_backend.models.UserRole
import upm.etsit.monolito_backend.services.OrderService
import upm.etsit.monolito_backend.services.ProductService
import java.util.UUID

@RestController
@RequestMapping("/api/v1/orders")
class OrderController (
    private val orderService: OrderService
){
    /**
     * POST ---------------------------------------------------------------------------
     */
    /**
     * POST - Crear una orden a partir del carrito del usuario
     */
    @PostMapping("/create")
    fun createOrderFromCart(
        @AuthenticationPrincipal user: UserDetails
    ): ResponseEntity<OrderDto> {
        val order = orderService.createOrderFromCart(user)
        return ResponseEntity.ok(order)
    }



    /**
     * GET ---------------------------------------------------------------------------
     */

    /**
     * GET - Obtener  detalles de una orden (ADMIN)
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/order/{orderUuid}")
    fun getOrderAdmin(
        @PathVariable orderUuid: UUID
    ): ResponseEntity<OrderDto> {
        println("solo admin")
        val order = orderService.getAdminOrderByUUID(orderUuid)
        return ResponseEntity.ok(order)
    }

    /**
     * GET - Obtener  detalles de una orden (no - ADMIN)
     */
    @GetMapping("/order/{orderUuid}")
    fun getOrder(
        @PathVariable orderUuid: UUID,
        @AuthenticationPrincipal userAuth: UserDetails
    ): ResponseEntity<OrderDto> {
        val order = orderService.getMyOrderByUUID(userAuth, orderUuid)
        return ResponseEntity.ok(order)
    }

    /**
     * GET - Obtener todas las órdenes de todos los users(ADMIN)
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/allOrders")
    fun getAllOrders(
        @AuthenticationPrincipal user: UserDetails
    ): ResponseEntity<List<OrderDto>> {
        val orders :List<OrderDto> = orderService.getAdminAllOrders()
        return ResponseEntity.ok(orders)
    }

    /**
     * GET - Obtener una orden específica por UUID (no - ADMIN)
     */
    @GetMapping("/allMyOrders")
    fun getAllMyOrders(
        @AuthenticationPrincipal user: UserDetails
    ): ResponseEntity<List<OrderDto>> {
        val orders = orderService.getAllMyOrders(user)
        return ResponseEntity.ok(orders)
    }


    /**
     * DELETE ---------------------------------------------------------------------------
     */

    /**
     * DELETE - BORRAR ORDEN MEDIANTE UUID
     */
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/admin/deleteOrder/{orderUUID}")
    fun deleteOrderByUuid(
        @PathVariable orderUUID: UUID
    ): ResponseEntity<List<OrderDto>> {
        val orders = orderService.deleteAdminOrderByUuid(orderUUID)
        return ResponseEntity.ok(orders)
    }


    /**
     * PUT ---------------------------------------------------------------------------
     */

    /**
     * PUT - Acutalizar cierta orden mediante uuid
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/admin/updateOrder/{orderUUID}/{orderStatus}")
    fun updateOrder(
        @PathVariable orderUUID: UUID,
        @PathVariable orderStatus: String
    ): ResponseEntity<OrderDto >{
        val orderDtoUPD = orderService.updateAdminOrderByUUID(orderUUID, orderStatus )
        return ResponseEntity.ok(orderDtoUPD)
    }
}

