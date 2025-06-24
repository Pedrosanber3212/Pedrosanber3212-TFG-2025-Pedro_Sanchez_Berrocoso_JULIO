package upm.etsit.monolito_backend.repositories

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import upm.etsit.monolito_backend.dto.OrderDto
import upm.etsit.monolito_backend.models.OrderEntity
import upm.etsit.monolito_backend.models.OrderItemEntity
import java.util.UUID

@Repository
interface OrderRepository: JpaRepository<OrderEntity,Long> {
    fun findByUuid(orderUUID: UUID): OrderEntity?
    fun findAllByUserUUID(userUUID: UUID) : List<OrderEntity>?

}