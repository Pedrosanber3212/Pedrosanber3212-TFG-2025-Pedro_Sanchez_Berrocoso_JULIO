package upm.etsit.monolito_backend.services

import UserCredentialsDto
import org.hibernate.query.Order
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.client.RestClient
import upm.etsit.monolito_backend.dto.OrderDto
import upm.etsit.monolito_backend.dto.OrderItemDto
import upm.etsit.monolito_backend.dto.ShoppingCartDto
import upm.etsit.monolito_backend.exception.CustomAccessDeniedException
import upm.etsit.monolito_backend.exception.CustomException
import upm.etsit.monolito_backend.exception.CustomResourceNotFoundException
import upm.etsit.monolito_backend.models.*
import upm.etsit.monolito_backend.repositories.*
import java.util.*


@Service
class OrderService(
    private val orderRepository: OrderRepository,
    private val orderItemsRepository: OrderItemsRepository,
   // private val userRepository: UserRepository,
    //private val shoppingCartRepository: ShoppingCartRepository,
    //private val shoppingCartItemRepository: ShoppingCartItemRepository,
    //private val productRepository: ProductRepository
    @Value("\${URL_MC_USERS}")
    private val userServiceBaseUrl: String,
    @Value("\${URL_MC_PRODUCTS}")
    private val productServiceBaseUrl: String,
    @Value("\${URL_MC_CART}")
    private val shoppingCartServiceBaseUrl: String,


    private val restClient: RestClient,
) {

    fun getMyCredentials(cookie: String): UserCredentialsDto {
        val fullCookie = "JSESSIONID=$cookie"
        println("📨 Enviando cookie: $fullCookie")

        return restClient.get()
            .uri("$userServiceBaseUrl/api/v1/users/me3")
            .header("Cookie", fullCookie)
            .retrieve()
            .body(UserCredentialsDto::class.java)!!
    }

    fun getUserEntity(cookie: String): UserEntity {

        val fullCookie = "JSESSIONID=$cookie"
        println("📨 Enviando cookie: $fullCookie")

        var userEntity: UserEntity= restClient.get()
            .uri("$userServiceBaseUrl/api/v1/users/userEntity")
            .header("Cookie", fullCookie)
            .retrieve()
            .body(UserEntity::class.java) ?: throw CustomResourceNotFoundException("No se ha encontrado el user")

        return userEntity;
    }

//-----------SHOPPING -CART -------------------//
    fun getShoppingCartEntityByUserUUID(userUUID:UUID, cookie: String): ShoppingCartEntity{

     val fullCookie = "JSESSIONID=$cookie"
        println("getShoppingCartEntityByUserUUID Enviando cookie: $fullCookie")

        return restClient.get()
            .uri("$shoppingCartServiceBaseUrl/api/v1/shoppingCart/shoppingCartEntityByUserUUID/$userUUID")
            .header("Cookie", fullCookie)
            .retrieve()
            .body(ShoppingCartEntity::class.java)!!


    }
//-----------SHOPPING -CART ITEMS LIST -------------------//

    fun getShoppingCartItemsEntityListByShoppingCartUUID(cartUUID: UUID, cookie: String): MutableList<ShoppingCartItemEntity>{
        val fullCookie = "JSESSIONID=$cookie"
        println("getShoppingCartItemsEntityListByShoppingCartUUID Enviando cookie: $fullCookie")

        val array = restClient.get()
            .uri("$shoppingCartServiceBaseUrl/api/v1/shoppingCart/shoppingCartItemEntityListByCartUUID/$cartUUID")
            .header("Cookie", fullCookie)
            .retrieve()
            .body(Array<ShoppingCartItemEntity>::class.java)!!

        return array.toMutableList()
    }

//-----------DELETE SHOPPING -CART -------------------//

    fun deleteShoppingCart(cartUUID: UUID, cookie: String) {
        val fullCookie = "JSESSIONID=$cookie"
        println("deleteShoppingCart - Enviando cookie: $fullCookie")

        restClient.delete()
            .uri("$shoppingCartServiceBaseUrl/api/v1/shoppingCart")
            .header("Cookie", fullCookie)
            .retrieve()
            .body(Void::class.java)
    }

//????????? A HACER
    fun getProductEntityByProductUUID(productUUID:UUID , cookie: String): ProductEntity{

         val fullCookie = "JSESSIONID=$cookie"
        println("getProductEntityByProductUUID Enviando cookie: $fullCookie")

        return restClient.get()
            .uri("$productServiceBaseUrl/api/v1/products/getProductEntityByProductUUID/$productUUID")
            .header("Cookie", fullCookie)
            .retrieve()
            .body(ProductEntity::class.java)!!


    }

    /**
     * CREAR ORDEN DESDE EL CARRITO DE COMPRAS
     */
    fun createOrderFromCart(cookie: String): OrderDto {
        /*val user = userRepository.findByUsername(user.username)
            ?: throw CustomResourceNotFoundException("Usuario no encontrado")
        */

        val user: UserEntity = this.getUserEntity(cookie)



        /*
            val cart: ShoppingCartEntity = shoppingCartRepository.findByUserUUID(user.uuid)
            ?: throw CustomResourceNotFoundException("Carrito no encontrado")

        */
        System.out.println("createOrderFromCart1")
            val cart: ShoppingCartEntity = this.getShoppingCartEntityByUserUUID(user.uuid , cookie)
        /*
               val cartItems:MutableList<ShoppingCartItemEntity> = shoppingCartItemRepository.findAllByShoppingCartUUID(cart.uuid)
          */
        System.out.println("createOrderFromCart2")
        val cartItems:MutableList<ShoppingCartItemEntity> = this.getShoppingCartItemsEntityListByShoppingCartUUID(cart.uuid, cookie);
        System.out.println("createOrderFromCart3")


        if (cartItems.isEmpty()) throw CustomException("El carrito está vacío")

        val order = orderRepository.save(
            OrderEntity(
                userUUID = user.uuid
            )
        )

        val orderItems : List<OrderItemEntity> = cartItems.map { cartItem ->
            orderItemsRepository.save(
                OrderItemEntity(
                    orderUUID = order.uuid,
                    productUUID = cartItem.productUUID,
                    quantity = cartItem.quantity,
                    price = cartItem.price
                )
            )
        }
        System.out.println("createOrderFromCart3.5")
        //shoppingCartItemRepository.deleteAll(cartItems)
       // this.deleteAllshoppingCartItemRepository(cartItems, cookie)

        //shoppingCartRepository.delete(cart)
        this.deleteShoppingCart(cart.uuid, cookie)
        System.out.println("createOrderFromCart4")

        return builOrderDto(order.uuid, cookie);
    }


    /**
     * OBTENER order by uuid for ADMIN
     */
    fun getAdminOrderByUUID(orderUUID: UUID, cookie: String): OrderDto {

        return builOrderDto(orderUUID,cookie);



    }

    /**
     * GET - Obtener  detalles de una orden (no - ADMIN)
     * SEGURIDAD == VALIDAR EL USUARIO ES PROPIETARIO DE LA ORDEN
     */
    fun getMyOrderByUUID( orderUuid : UUID, cookie: String ): OrderDto{

        //SEGURIDAD
        val generalOrder: OrderEntity = orderRepository.findByUuid(orderUuid)
            ?: throw CustomResourceNotFoundException("No se ha encontrado la orden con orderUUID ${orderUuid}")

        /*
        val user: UserEntity = userRepository.findByUsername(userAuth.username)
            ?: throw CustomResourceNotFoundException("No se ha encontrado el usuario con username ${userAuth.username}")

            */

        val user: UserEntity = this.getUserEntity(cookie)

        if (user.uuid != generalOrder.userUUID){throw CustomAccessDeniedException("No tienen permisos para acceder a esta orden")}

        return builOrderDto(orderUuid , cookie);


    }


    fun getAdminAllOrders(cookie: String): List<OrderDto>{
        val orderList :List<OrderDto>  = orderRepository.findAll().map { orderFound ->
            val orderUuid: UUID = orderFound.uuid;
            //return  dEL ORDERdto de dicha orden
            builOrderDto(orderUuid, cookie)
        }
        return orderList;
    }


    fun getAllMyOrders(cookie: String): List<OrderDto>{
        //BUSQUEDA DE LAS ORDENES DE ESTE USER
       /* val user2: UserEntity = userRepository.findByUsername(user.username)
            ?: throw CustomResourceNotFoundException("No se ha encontrado el usuario con username ${user.username}")
        */

        val user: UserEntity = this.getUserEntity(cookie)



        val listOrderEntity : List<OrderEntity> = orderRepository.findAllByUserUUID(user.uuid)
            ?: throw CustomResourceNotFoundException("No se ha encontrado el usuario con username ${user.username}")
        return listOrderEntity.map { orderFound->
            builOrderDto(orderFound.uuid, cookie)
        }
    }


    fun deleteAdminOrderByUuid(orderUuid:UUID , cookie:String): List<OrderDto>{

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
            builOrderDto(orderUUid, cookie)
        }
        return orderList;
    }

    fun updateAdminOrderByUUID(orderUUID: UUID,orderStatus:String , cookie:String): OrderDto{
        val orderEntity: OrderEntity = orderRepository.findByUuid(orderUUID)
            ?: throw CustomResourceNotFoundException("No se ha encontrado la orden con orderUUID $orderUUID")
        val updatedOrderEntity : OrderEntity = orderEntity.copy(orderStatus = OrderStatus.valueOf(orderStatus))
        val orderUpdatedEntity: OrderEntity = orderRepository.save(updatedOrderEntity)
        return builOrderDto(orderUpdatedEntity.uuid, cookie)


    }

    //-----------------------------------------------------------------------------------------------------



    /**
     * METODO AUX: CONSTRUCCIÓN RESPUESTA DEL DTO RESPUESTA ShoppingCartDto
     */
    fun builOrderDto(orderUuid: UUID, cookie:String): OrderDto{
        val orderItemsListDto: List<OrderItemDto> =
            orderItemsRepository.findAllByOrderUUID(orderUuid)
                .map {
                    //para cada orderItem
                    //extraemos el product
                   /*
                        val product = productRepository.findByUuid(it.productUUID)
                        ?: throw CustomResourceNotFoundException("No se encontró el producto con uuid: ${it.productUUID}")

                    */

                    val product = this.getProductEntityByProductUUID(it.productUUID, cookie);


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
