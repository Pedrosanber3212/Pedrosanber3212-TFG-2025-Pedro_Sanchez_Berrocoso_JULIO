package upm.etsit.monolito_backend.repositories

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import upm.etsit.monolito_backend.models.ShoppingCartEntity
import upm.etsit.monolito_backend.models.ShoppingCartItemEntity
import java.util.*


@Repository
    interface ShoppingCartItemRepository : JpaRepository<ShoppingCartItemEntity, Long> {
        fun findAllByShoppingCartUUID(shoppingCartUUID: UUID): MutableList<ShoppingCartItemEntity>

        fun findAllByProductUUID(productUUID: UUID): MutableList<ShoppingCartItemEntity>
        fun findByUuid(shoppingCartItemUUID: UUID): ShoppingCartItemEntity?;

    fun existsByShoppingCartUUIDAndProductUUID(shoppingCartUUID: UUID, productUUID: UUID): Boolean
    fun findByShoppingCartUUIDAndProductUUID(shoppingCartUUID: UUID, productUUID: UUID): ShoppingCartItemEntity?;

    fun deleteByUuid(uuid: UUID)

    fun deleteAllByProductUUID(productUUID: UUID)

}


