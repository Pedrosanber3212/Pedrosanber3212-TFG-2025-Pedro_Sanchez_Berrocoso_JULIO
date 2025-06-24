package upm.etsit.monolito_backend.repositories

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import upm.etsit.monolito_backend.models.OrderItemEntity
import java.util.*


@Repository
interface OrderItemsRepository: JpaRepository<OrderItemEntity, Long> {

    fun findAllByOrderUUID( orderUUID: UUID): List<OrderItemEntity>

}


