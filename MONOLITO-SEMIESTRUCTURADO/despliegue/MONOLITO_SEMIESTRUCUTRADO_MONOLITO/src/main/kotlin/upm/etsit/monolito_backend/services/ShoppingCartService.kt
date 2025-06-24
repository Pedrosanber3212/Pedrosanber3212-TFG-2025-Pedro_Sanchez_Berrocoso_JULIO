package upm.etsit.monolito_backend.services

import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import upm.etsit.monolito_backend.dto.ShoppingCartDto
import upm.etsit.monolito_backend.dto.ShoppingCartItemDto
import upm.etsit.monolito_backend.exception.CustomAccessDeniedException
import upm.etsit.monolito_backend.exception.CustomException
import upm.etsit.monolito_backend.exception.CustomResourceNotFoundException
import upm.etsit.monolito_backend.models.ShoppingCartEntity
import upm.etsit.monolito_backend.models.ShoppingCartItemEntity
import upm.etsit.monolito_backend.models.UserEntity
import upm.etsit.monolito_backend.models.toShoppingCartEntityDto
import upm.etsit.monolito_backend.repositories.ProductRepository
import upm.etsit.monolito_backend.repositories.ShoppingCartItemRepository
import upm.etsit.monolito_backend.repositories.ShoppingCartRepository
import upm.etsit.monolito_backend.repositories.UserRepository
import java.util.UUID
import javax.swing.plaf.multi.MultiListUI

@Service
class ShoppingCartService(
   private val shoppingCartRepository: ShoppingCartRepository,
   private val shoppingCartItemRepository: ShoppingCartItemRepository,
    private val productRepository: ProductRepository,
    private val userRepository: UserRepository
) {

    fun getItemsCart(userAuth: UserDetails): ShoppingCartDto {
        //buscamos el user
        val userEntity: UserEntity = userRepository.findByUsername(userAuth.username)
            ?: throw CustomException("Error buscando el usuario con username: ${userAuth.username}")
        //buscamos el carrito del user
        System.out.println("getItemsCart0:")
        val userShoppingCart: ShoppingCartEntity = shoppingCartRepository.findByUserUUID(userEntity.uuid)
            ?: throw CustomResourceNotFoundException("El usuario no tiene carrito de compras activo")
        System.out.println("getItemsCart:"+ userShoppingCart.uuid)
        return  buildShoppingCartDto(cartUUID = userShoppingCart.uuid,  userUUID= userEntity.uuid)
    }
    fun addItemToCart(userAuth: UserDetails, productUUID: UUID): ShoppingCartDto {
        //configuración seguridad
        //buscamos el user
        val user: UserEntity = userRepository.findByUsername(userAuth.username)
            ?: throw CustomException("Error buscando el usuario con username: ${userAuth.username}")
        //buscamos el carrito del user && SI No está creado lo creamos
        val userShoppingCart: ShoppingCartEntity = shoppingCartRepository.findByUserUUID(user.uuid)
            ?: shoppingCartRepository.save(ShoppingCartEntity(userUUID = user.uuid))
        //check carrito pertenece al user
        if (user.uuid != userShoppingCart.userUUID) {
            throw CustomAccessDeniedException("No es propietario del carrito ");
        }


        //buscamos el producto a añadir al carrito
        val product = productRepository.findByUuid(productUUID)
            ?: throw CustomResourceNotFoundException("No se ha encontrado el producto")

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
    fun removeItemFromCart(userAuth: UserDetails, cartItemUUID: UUID): ShoppingCartDto{
        //buscamos el user
        val user: UserEntity = userRepository.findByUsername(userAuth.username) ?: throw CustomException("Error buscando el usuario con username: ${userAuth.username}")

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
    fun clearCart(user: UserDetails) {
        //buscamos el userEntity
        val userEntity = userRepository.findByUsername(user.username)
            ?: throw CustomResourceNotFoundException("No se ha encontrado el usuario con username: ${user.username}")

        //buscamos el carrito del user
        val shoppingCart = shoppingCartRepository.findByUserUUID(userEntity.uuid)
            ?: throw CustomResourceNotFoundException("No se ha encontrado el carrito del usuario")

        //eliminamos todos los items del carrito
        val items = shoppingCartItemRepository.findAllByShoppingCartUUID(shoppingCart.uuid)
        shoppingCartItemRepository.deleteAll(items)
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
            //por cada item buscamos el producto
            var productShoppingCartItem =
                productRepository.findByUuid(it.productUUID) ?: throw CustomException("Error en buildShoppingCartDto")
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

}