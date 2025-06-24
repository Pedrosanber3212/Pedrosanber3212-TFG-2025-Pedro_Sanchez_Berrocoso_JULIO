package upm.etsit.monolito_backend.services

import org.hibernate.query.Order
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import upm.etsit.monolito_backend.dto.OrderDto
import upm.etsit.monolito_backend.dto.OrderItemDto
import upm.etsit.monolito_backend.dto.ShoppingCartDto
import upm.etsit.monolito_backend.exception.CustomAccessDeniedException
import upm.etsit.monolito_backend.exception.CustomException
import upm.etsit.monolito_backend.exception.CustomResourceNotFoundException
import upm.etsit.monolito_backend.models.OrderEntity
import upm.etsit.monolito_backend.models.OrderItemEntity
import upm.etsit.monolito_backend.models.OrderStatus
import upm.etsit.monolito_backend.models.UserEntity
import upm.etsit.monolito_backend.repositories.*
import java.util.*


@Service
class OrderService(
    private val orderRepository: OrderRepository,
    private val orderItemsRepository: OrderItemsRepository,
    private val userRepository: UserRepository,
    private val shoppingCartRepository: ShoppingCartRepository,
    private val shoppingCartItemRepository: ShoppingCartItemRepository,
    private val productRepository: ProductRepository
) {

    /**
     * CREAR ORDEN DESDE EL CARRITO DE COMPRAS
     */
    fun createOrderFromCart(user: UserDetails): OrderDto {
        val user = userRepository.findByUsername(user.username)
            ?: throw CustomResourceNotFoundException("Usuario no encontrado")

        val cart = shoppingCartRepository.findByUserUUID(user.uuid)
            ?: throw CustomResourceNotFoundException("Carrito no encontrado")

        val cartItems = shoppingCartItemRepository.findAllByShoppingCartUUID(cart.uuid)
        if (cartItems.isEmpty()) throw CustomException("El carrito está vacío")

        val order = orderRepository.save(
            OrderEntity(
                userUUID = user.uuid
            )
        )

        val orderItems = cartItems.map { cartItem ->
            orderItemsRepository.save(
                OrderItemEntity(
                    orderUUID = order.uuid,
                    productUUID = cartItem.productUUID,
                    quantity = cartItem.quantity,
                    price = cartItem.price
                )
            )
        }

        shoppingCartItemRepository.deleteAll(cartItems)
        shoppingCartRepository.delete(cart)

        return builOrderDto(order.uuid);
    }


    /**
     * OBTENER order by uuid for ADMIN
     */
    fun getAdminOrderByUUID(orderUUID: UUID): OrderDto {

        return builOrderDto(orderUUID);



    }

    /**
     * GET - Obtener  detalles de una orden (no - ADMIN)
     * SEGURIDAD == VALIDAR EL USUARIO ES PROPIETARIO DE LA ORDEN
     */
    fun getMyOrderByUUID(userAuth: UserDetails , orderUuid : UUID ): OrderDto{

        //SEGURIDAD
        val generalOrder: OrderEntity = orderRepository.findByUuid(orderUuid)
            ?: throw CustomResourceNotFoundException("No se ha encontrado la orden con orderUUID ${orderUuid}")
        val user: UserEntity = userRepository.findByUsername(userAuth.username)
            ?: throw CustomResourceNotFoundException("No se ha encontrado el usuario con username ${userAuth.username}")

        if (user.uuid != generalOrder.userUUID){throw CustomAccessDeniedException("No tienen permisos para acceder a esta orden")}

        return builOrderDto(orderUuid);


    }


    fun getAdminAllOrders(): List<OrderDto>{
        val orderList :List<OrderDto>  = orderRepository.findAll().map { orderFound ->
            val orderUuid: UUID = orderFound.uuid;
            //return  dEL ORDERdto de dicha orden
            builOrderDto(orderUuid)
        }
        return orderList;
    }


    fun getAllMyOrders(user: UserDetails): List<OrderDto>{
        //BUSQUEDA DE LAS ORDENES DE ESTE USER
        val user: UserEntity = userRepository.findByUsername(user.username)
            ?: throw CustomResourceNotFoundException("No se ha encontrado el usuario con username ${user.username}")
        val listOrderEntity : List<OrderEntity> = orderRepository.findAllByUserUUID(user.uuid)
            ?: throw CustomResourceNotFoundException("No se ha encontrado el usuario con username ${user.username}")
        return listOrderEntity.map { orderFound->
            builOrderDto(orderFound.uuid)
        }
    }


    fun deleteAdminOrderByUuid(orderUuid:UUID): List<OrderDto>{

        val generalOrder: OrderEntity = orderRepository.findByUuid(orderUuid)
            ?: throw CustomResourceNotFoundException("No se ha encontrado la orden con orderUUID ${orderUuid}")

        val listOrderItemEntity: List<OrderItemEntity> = orderItemsRepository.findAllByOrderUUID(generalOrder.uuid)

        listOrderItemEntity.map {orderItemEntity ->
            orderItemsRepository.delete(orderItemEntity);
        }

        orderRepository.delete(generalOrder);


        val orderList :List<OrderDto>  = orderRepository.findAll().map { orderFound ->
            val orderUUid: UUID = orderFound.uuid;
            //return  dEL ORDERdto de dicha orden
            builOrderDto(orderUUid)
        }
        return orderList;
    }

    fun updateAdminOrderByUUID(orderUUID: UUID,orderStatus:String): OrderDto{
        val orderEntity: OrderEntity = orderRepository.findByUuid(orderUUID)
            ?: throw CustomResourceNotFoundException("No se ha encontrado la orden con orderUUID $orderUUID")
        val updatedOrderEntity : OrderEntity = orderEntity.copy(orderStatus = OrderStatus.valueOf(orderStatus))
        val orderUpdatedEntity: OrderEntity = orderRepository.save(updatedOrderEntity)
        return builOrderDto(orderUpdatedEntity.uuid)


    }

    //-----------------------------------------------------------------------------------------------------



    /**
     * METODO AUX: CONSTRUCCIÓN RESPUESTA DEL DTO RESPUESTA ShoppingCartDto
     */
    fun builOrderDto(orderUuid: UUID): OrderDto{
        val orderItemsListDto: List<OrderItemDto> =
            orderItemsRepository.findAllByOrderUUID(orderUuid)
                .map {
                    //para cada orderItem
                    //extraemos el product
                    val product = productRepository.findByUuid(it.productUUID)
                        ?: throw CustomResourceNotFoundException("No se encontró el producto con uuid: ${it.productUUID}")
                    OrderItemDto(
                        uuid = it.uuid, orderUUID = it.orderUUID,
                        productUUID = it.productUUID, quantity = it.quantity,
                        price = it.price, image = product.image, name = product.name
                    )
                }
        val generalOrder: OrderEntity = orderRepository.findByUuid(orderUuid)
            ?: throw CustomResourceNotFoundException("No se ha encontrado la orden con orderUUID ${orderUuid}")

        return OrderDto(
            uuid =  generalOrder.uuid, userUUID = generalOrder.userUUID, creationDate = generalOrder.creationDate,
            status = generalOrder.orderStatus, items = orderItemsListDto
        )
    }


}
