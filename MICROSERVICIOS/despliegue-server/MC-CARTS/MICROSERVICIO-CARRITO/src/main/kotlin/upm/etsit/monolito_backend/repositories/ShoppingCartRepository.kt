package upm.etsit.monolito_backend.repositories

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import upm.etsit.monolito_backend.models.ShoppingCartEntity
import upm.etsit.monolito_backend.models.ShoppingCartItemEntity
import java.util.UUID

@Repository
interface ShoppingCartRepository : JpaRepository<ShoppingCartEntity,Long> {
    fun findByUserUUID(userUUID: UUID) : ShoppingCartEntity?

    fun findByUuid ( shoppingCartUUID : UUID ) : ShoppingCartEntity?

}