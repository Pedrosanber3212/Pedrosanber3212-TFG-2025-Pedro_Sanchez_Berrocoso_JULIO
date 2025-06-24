package upm.etsit.monolito_backend.repositories

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import upm.etsit.monolito_backend.models.UserEntity
import java.util.*





@Repository
interface UserRepository : JpaRepository<UserEntity, Long> {
    fun findByUsername(username: String): UserEntity?
    fun findByUuid(uuid: UUID): UserEntity?
    fun findByEmail(email: String): UserEntity?
    fun existsByUsername(email: String): Boolean


    // Otras funciones de búsqueda personalizadas
}
