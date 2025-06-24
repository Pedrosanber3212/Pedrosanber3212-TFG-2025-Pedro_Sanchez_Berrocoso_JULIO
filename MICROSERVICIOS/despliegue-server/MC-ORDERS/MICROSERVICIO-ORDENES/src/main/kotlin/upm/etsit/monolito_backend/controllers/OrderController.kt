package upm.etsit.monolito_backend.controllers

import UserCredentialsDto
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import upm.etsit.monolito_backend.dto.OrderDto
import upm.etsit.monolito_backend.exception.CustomAccessDeniedException
import upm.etsit.monolito_backend.services.OrderService
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
       // @AuthenticationPrincipal user: UserDetails
        @CookieValue("JSESSIONID") sessionCookie: String
    ): ResponseEntity<OrderDto> {
        val order = orderService.createOrderFromCart(sessionCookie)
        return ResponseEntity.ok(order)
    }



    /**
     * GET ---------------------------------------------------------------------------
     */

    /**
     * GET - Obtener  detalles de una orden (ADMIN)
     */
   // @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/order/{orderUuid}")
    fun getOrderAdmin(
        @PathVariable orderUuid: UUID,
        @CookieValue("JSESSIONID") sessionCookie: String
    ): ResponseEntity<OrderDto> {
        var credentials:UserCredentialsDto =  this.orderService.getMyCredentials(sessionCookie)
        if(credentials.authorities[0].authority != "ROLE_ADMIN"){
            throw CustomAccessDeniedException("No tiene el rol ADMIN")
        }
        println("solo admin")
        val order = orderService.getAdminOrderByUUID(orderUuid, sessionCookie)
        return ResponseEntity.ok(order)
    }

    /**
     * GET - Obtener  detalles de una orden (no - ADMIN)
     */
    @GetMapping("/order/{orderUuid}")
    fun getOrder(
        @PathVariable orderUuid: UUID,
       // @AuthenticationPrincipal userAuth: UserDetails,
        @CookieValue("JSESSIONID") sessionCookie: String
    ): ResponseEntity<OrderDto> {
        val order = orderService.getMyOrderByUUID( orderUuid, sessionCookie)
        return ResponseEntity.ok(order)
    }

    /**
     * GET - Obtener todas las órdenes de todos los users(ADMIN)
     */
   // @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/allOrders")
    fun getAllOrders(
        //@AuthenticationPrincipal user: UserDetails,
        @CookieValue("JSESSIONID") sessionCookie: String
    ): ResponseEntity<List<OrderDto>> {
        var credentials:UserCredentialsDto =  this.orderService.getMyCredentials(sessionCookie)
        if(credentials.authorities[0].authority != "ROLE_ADMIN"){
            throw CustomAccessDeniedException("No tiene el rol ADMIN")
        }


        val orders :List<OrderDto> = orderService.getAdminAllOrders(sessionCookie)
        return ResponseEntity.ok(orders)
    }

    /**
     * GET - Obtener una orden específica por UUID (no - ADMIN)
     */
    @GetMapping("/allMyOrders")
    fun getAllMyOrders(
       // @AuthenticationPrincipal user: UserDetails,
        @CookieValue("JSESSIONID") sessionCookie: String
    ): ResponseEntity<List<OrderDto>> {
        val orders = orderService.getAllMyOrders(sessionCookie)
        return ResponseEntity.ok(orders)
    }


    /**
     * DELETE ---------------------------------------------------------------------------
     */

    /**
     * DELETE - BORRAR ORDEN MEDIANTE UUID
     */
    //@PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/admin/deleteOrder/{orderUUID}")
    fun deleteOrderByUuid(
        @PathVariable orderUUID: UUID,
        @CookieValue("JSESSIONID") sessionCookie: String
    ): ResponseEntity<List<OrderDto>> {

        var credentials:UserCredentialsDto =  this.orderService.getMyCredentials(sessionCookie)
        if(credentials.authorities[0].authority != "ROLE_ADMIN"){
            throw CustomAccessDeniedException("No tiene el rol ADMIN")
        }
        val orders = orderService.deleteAdminOrderByUuid(orderUUID,sessionCookie )
        return ResponseEntity.ok(orders)
    }


    /**
     * PUT ---------------------------------------------------------------------------
     */

    /**
     * PUT - Acutalizar cierta orden mediante uuid
     */
    //@PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/admin/updateOrder/{orderUUID}/{orderStatus}")
    fun updateOrder(
        @PathVariable orderUUID: UUID,
        @PathVariable orderStatus: String,
        @CookieValue("JSESSIONID") sessionCookie: String
    ): ResponseEntity<OrderDto >{
        var credentials:UserCredentialsDto =  this.orderService.getMyCredentials(sessionCookie)
        if(credentials.authorities[0].authority != "ROLE_ADMIN"){
            throw CustomAccessDeniedException("No tiene el rol ADMIN")
        }


        val orderDtoUPD = orderService.updateAdminOrderByUUID(orderUUID, orderStatus , sessionCookie )
        return ResponseEntity.ok(orderDtoUPD)
    }
}

