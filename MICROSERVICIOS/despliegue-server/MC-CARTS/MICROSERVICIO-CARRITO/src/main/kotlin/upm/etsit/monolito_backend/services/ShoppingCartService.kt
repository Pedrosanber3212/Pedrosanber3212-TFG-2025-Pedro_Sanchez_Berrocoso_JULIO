package upm.etsit.monolito_backend.services

import org.apache.catalina.User
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.client.RestClient
import upm.etsit.monolito_backend.dto.ShoppingCartDto
import upm.etsit.monolito_backend.dto.ShoppingCartItemDto
import upm.etsit.monolito_backend.exception.CustomAccessDeniedException
import upm.etsit.monolito_backend.exception.CustomException
import upm.etsit.monolito_backend.exception.CustomResourceNotFoundException
import upm.etsit.monolito_backend.models.*
import upm.etsit.monolito_backend.repositories.ShoppingCartItemRepository
import upm.etsit.monolito_backend.repositories.ShoppingCartRepository
import java.util.UUID
import javax.swing.plaf.multi.MultiListUI

@Service
class ShoppingCartService(
    private val shoppingCartRepository: ShoppingCartRepository,
    private val shoppingCartItemRepository: ShoppingCartItemRepository,
   // private val productRepository: ProductRepository,
    //private val userRepository: UserRepository
    @Value("\${URL_MC_USERS}")
   private val userServiceBaseUrl: String,
    @Value("\${URL_MC_PRODUCTS}")
    private val productServiceBaseUrl: String,
    private val restClient: RestClient,
) {



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

    fun getProductEntity(productUUID: UUID):  ProductEntity? {
          ProductEntity()

        var productEntity: ProductEntity = restClient.get()
            .uri("$productServiceBaseUrl/api/v1/products/getProductEntityByProductUUID/${productUUID}")
            .header("Cookie", "")//para productos no se necesita cookie
            .retrieve()
            .body(ProductEntity::class.java) ?: throw CustomResourceNotFoundException("No se ha encontrado el user")

        return productEntity;
    }



    fun getItemsCart(cookie: String): ShoppingCartDto {
        //buscamos el user
        /*val userEntity: UserEntity = userRepository.findByUsername(userAuth.username)
            ?: throw CustomException("Error buscando el usuario con username: ${userAuth.username}")
        */

        val userEntity :UserEntity = this.getUserEntity(cookie)



        //buscamos el carrito del user
        System.out.println("getItemsCart0:")
        val userShoppingCart: ShoppingCartEntity = shoppingCartRepository.findByUserUUID(userEntity.uuid)
            ?: throw CustomResourceNotFoundException("El usuario no tiene carrito de compras activo")
        System.out.println("getItemsCart:"+ userShoppingCart.uuid)
        return  buildShoppingCartDto( cartUUID = userShoppingCart.uuid,  userUUID= userEntity.uuid)
    }
    fun addItemToCart(cookie: String, productUUID: UUID): ShoppingCartDto {
        //configuración seguridad
        //buscamos el user
        /*val user: UserEntity = userRepository.findByUsername(userAuth.username)
           ?: throw CustomException("Error buscando el usuario con username: ${userAuth.username}")
       */

        val user :UserEntity = this.getUserEntity(cookie )

        //buscamos el carrito del user && SI No está creado lo creamos
        val userShoppingCart: ShoppingCartEntity = shoppingCartRepository.findByUserUUID(user.uuid)
            ?: shoppingCartRepository.save(ShoppingCartEntity(userUUID = user.uuid))
        //check carrito pertenece al user
        if (user.uuid != userShoppingCart.userUUID) {
            throw CustomAccessDeniedException("No es propietario del carrito ");
        }


        /*//buscamos el producto a añadir al carrito
        val product = productRepository.findByUuid(productUUID)
            ?: throw CustomResourceNotFoundException("No se ha encontrado el producto")
        */
        val product = getProductEntity(productUUID)   ?: throw CustomResourceNotFoundException("No se ha encontrado el producto")


        // Verificar si ya existe un ítem con ese producto en el carrito
        val existsCartItemWithProduct = shoppingCartItemRepository.existsByShoppingCartUUIDAndProductUUID(
            userShoppingCart.uuid,
            productUUID
        )

        if (!existsCartItemWithProduct) {
            // Crear el nuevo ítem en el carrito
            shoppingCartItemRepository.save(
                ShoppingCartItemEntity(
                    shoppingCartUUID = userShoppingCart.uuid,
                    productUUID = productUUID,
                    quantity = 1,
                    price = product.price
                )
            )
        } else {
            // buscampos el item si ya existe un ítem con ese producto en el carrito y lo updateamos
            val shoppingCartItemWithProduct = shoppingCartItemRepository.findByShoppingCartUUIDAndProductUUID(
                userShoppingCart.uuid,
                productUUID
            ) ?: throw CustomResourceNotFoundException("No se ha encontrado el item del carrito ")
            shoppingCartItemRepository.save(shoppingCartItemWithProduct.copy(quantity = shoppingCartItemWithProduct.quantity + 1))

        }


        return buildShoppingCartDto(cartUUID = userShoppingCart.uuid,  userUUID= user.uuid)

    }


    /**
     * PARAMETRO USERNAME USADO PARA CONTROLAR SI EL ITEM PERTENECE AL CARRITO DEL USER
     */
    fun removeItemFromCart(cookie: String, cartItemUUID: UUID): ShoppingCartDto{
        //buscamos el user
        /*val user: UserEntity = userRepository.findByUsername(userAuth.username) ?: throw CustomException("Error buscando el usuario con username: ${userAuth.username}")

        */
        val user :UserEntity = this.getUserEntity(cookie )


        //buscamos el item del user && SI No está ==> error al borrar item
        val userShoppingCartItem:ShoppingCartItemEntity = shoppingCartItemRepository.findByUuid(cartItemUUID) ?: throw CustomResourceNotFoundException("No se encontro el carrito de")

        //buscamos el carrito ==>
        val cart = shoppingCartRepository.findByUuid(userShoppingCartItem.shoppingCartUUID)
            ?: throw CustomResourceNotFoundException("No se ha encontrado el carrito")

        //check carrito pertenece al user
        if(user.uuid != cart.userUUID){
            throw CustomAccessDeniedException("No es el propietario del carrito")
        }

        var cartItemUpd :ShoppingCartItemEntity=   userShoppingCartItem.copy(quantity = userShoppingCartItem.quantity-1)

        if(cartItemUpd.quantity===0){
            shoppingCartItemRepository.deleteById(cartItemUpd.id)
        }else{
            shoppingCartItemRepository.save(cartItemUpd)
        }


        return buildShoppingCartDto(cartUUID = cartItemUpd.shoppingCartUUID, userUUID= user.uuid)

    }


    /**
     * VACIAR CARRITO DEL USUARIO
     */
    fun clearCart(cookie: String) {
        //buscamos el userEntity
        /* val userEntity = userRepository.findByUsername(user.username)
            ?: throw CustomResourceNotFoundException("No se ha encontrado el usuario con username: ${user.username}")




       */

        System.out.println("clearCart0")
        val userEntity :UserEntity = this.getUserEntity(cookie )

System.out.println("clearCart1")



        //buscamos el carrito del user
        val shoppingCart = shoppingCartRepository.findByUserUUID(userEntity.uuid)
            ?: throw CustomResourceNotFoundException("No se ha encontrado el carrito del usuario")
        System.out.println("clearCart2")
        //eliminamos todos los items del carrito
        val items = shoppingCartItemRepository.findAllByShoppingCartUUID(shoppingCart.uuid)

        System.out.println("clearCart3")
        shoppingCartItemRepository.deleteAll(items)

        System.out.println("clearCart4")
        shoppingCartRepository.delete(shoppingCart);
    }


    /**
     * METODO AUX: CONSTRUCCIÓN RESPUESTA DEL DTO RESPUESTA ShoppingCartDto
     */
    fun buildShoppingCartDto(cartUUID: UUID, userUUID: UUID): ShoppingCartDto {
        //buscamos todos los items del carrito
        val items = shoppingCartItemRepository.findAllByShoppingCartUUID(cartUUID).map {
            System.out.println("cartUuid1: "+ cartUUID )
            System.out.println("cartUuid1: " + it.uuid )

            System.out.print("cartUuid2:" )
            System.out.print("productUUID:"+it.productUUID )


            /*
             //por cada item buscamos el producto
             var productShoppingCartItem =
                 productRepository.findByUuid(it.productUUID) ?: throw CustomException("Error en buildShoppingCartDto")


        */
            val productShoppingCartItem = getProductEntity( it.productUUID) ?: throw CustomException("Error en buildShoppingCartDto")




            System.out.println("cartUuid2: " + productShoppingCartItem.toString())
            //convertimos entity a dto
            it.toShoppingCartEntityDto(productShoppingCartItem)
        }.toMutableList()

        //devolvemos el dto del carrito
        return ShoppingCartDto(
            uuid = cartUUID,
            userUUID = userUUID,
            items = items
        )

    }


    fun deleteAllShoppingItemsByProductUUID(uuid:UUID){
        this.shoppingCartItemRepository.deleteAllByProductUUID(uuid)
    }


    fun getCartItemsEntityListByCartUUID(cartUUID:UUID):MutableList<ShoppingCartItemEntity>{
        return this.shoppingCartItemRepository.findAllByShoppingCartUUID(cartUUID)
    }


    fun getCartEntityByCartUUID(userUUID:UUID):ShoppingCartEntity{
        return this.shoppingCartRepository.findByUserUUID(userUUID)
            ?: throw CustomResourceNotFoundException("No se encontró el carrito")
    }





}