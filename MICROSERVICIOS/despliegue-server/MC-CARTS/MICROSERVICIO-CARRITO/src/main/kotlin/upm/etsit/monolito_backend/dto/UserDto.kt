package upm.etsit.monolito_backend.dto

import org.apache.catalina.User
import upm.etsit.monolito_backend.models.UserEntity
import upm.etsit.monolito_backend.models.UserRole
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*


data class UserRegisterRequestDto(
    val name: String,
    val username: String,
    val birthdate: LocalDate,
    val address: String,
    val email: String   ,
    val password: String,
    val role: UserRole,

)

fun UserRegisterRequestDto.toUserEntity():UserEntity{
    return UserEntity (name= this.name, username =  this.username, birthdate = this.birthdate, password =  this.password, email = this.email, address = this.address, role = this.role)
}

//Usado en las respuestas back-front
data class UserDto(
    val uuid: UUID,
    val name: String,
    val username: String,
    val email: String,
    val address: String,
    val role: String,
    val birthdate: LocalDate, // YYYY-MM-DD

)


data class UserUpdateRequest(
    val name: String,
    val email: String,
    val address: String,
    val birthdate: LocalDate, // YYYY-MM-DD
)


fun UserUpdateRequest.toUserEntity():UserEntity{
    return UserEntity(name= this.name,  birthdate = this.birthdate, email = this.email , address = this.address  )
}
