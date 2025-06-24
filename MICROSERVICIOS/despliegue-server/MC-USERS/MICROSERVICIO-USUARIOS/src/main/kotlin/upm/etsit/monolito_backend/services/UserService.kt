package upm.etsit.monolito_backend.services

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.server.ResponseStatusException
import upm.etsit.monolito_backend.dto.UserDto
import upm.etsit.monolito_backend.dto.UserRegisterRequestDto
import upm.etsit.monolito_backend.dto.UserUpdateRequest
import upm.etsit.monolito_backend.dto.toUserEntity
import upm.etsit.monolito_backend.exception.*
import upm.etsit.monolito_backend.models.UserEntity
import upm.etsit.monolito_backend.models.UserRole
import upm.etsit.monolito_backend.repositories.UserRepository
import upm.etsit.monolito_backend.models.toUserDto
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.UUID

@Service
class UserService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder
) : UserDetailsService {

    override fun loadUserByUsername(username: String): UserDetails {
        val user = userRepository.findByUsername(username)?: throw UsernameNotFoundException("Usuario no encontrado: $username")

        return org.springframework.security.core.userdetails.User.builder()
            .username(user.username)
            .password(user.password)
            .roles(user.role.name)
            .build()
    }

    /*
    *get findByUsername
     */
    fun findByUsername(username:String): UserEntity{
        return this.userRepository.findByUsername(username) ?: throw CustomResourceNotFoundException("No se ha encontrado el usuario");
    }

    /**
     * POST!!!!
     */
    fun register(userRegisterRequestDto: UserRegisterRequestDto): UserDto? {
        if (userRepository.existsByUsername(userRegisterRequestDto.username)) {
            println("/register email ya registrado")
            throw CustomConflictException("Username ya registrado")
        }
        println("/register email ya registrado userRegisterRequestDto: " + userRegisterRequestDto.toString())
        return userRepository.save(userRegisterRequestDto.copy(password = passwordEncoder.encode(userRegisterRequestDto.password), role = enumValueOf<UserRole>("CUSTOMER")) .toUserEntity()).toUserDto()

    }

    /**
     * POST!!!!
     */
    //@PreAuthorize("hasRole('ADMIN')")
    fun register_admin(userRegisterRequestDto: UserRegisterRequestDto): UserDto? {
        if (userRepository.existsByUsername(userRegisterRequestDto.username)) {
            println("/register email ya registrado")
            throw CustomConflictException("Username ya registrado")
        }
        println("/register email ya registrado userRegisterRequestDto: " + userRegisterRequestDto.toString())
        return userRepository.save(userRegisterRequestDto.copy(password = passwordEncoder.encode(userRegisterRequestDto.password), role = enumValueOf<UserRole>("ADMIN")) .toUserEntity()).toUserDto()

    }


    /**
     * GET!!!!!
     */
    fun findAllUsers(): List<UserDto> {
        return userRepository.findAll()
            .map { entity -> entity.toUserDto() }
    }

    fun findUserByUsername(username:String): UserDto {
        return userRepository.findByUsername(username)?.toUserDto() ?: throw CustomResourceNotFoundException("Error: usuario $username no encontrado")
    }



    fun findUserByUuid(uuid:   UUID): UserDto? {

        val userEntity = userRepository.findByUuid(uuid)
        return userEntity?.toUserDto()
    }





    /**
     * update!!!!!!!!!
     */
    fun updateUser(
      uuid: UUID,
      userUpdateRequest: UserUpdateRequest,
      role: UserRole,
      usernameRequester: String
    ): UserDto {
        if(role.name != "ADMIN"){
            //validamos que el usuario a updatear sea el mismo que el que solicita el update
            val userRequester  = userRepository.findByUsername(usernameRequester)?: throw CustomException(null)
            if(userRequester.uuid != uuid){
                throw CustomAccessDeniedException(null)
            }

        }
        val userToUpdate = userRepository.findByUuid(uuid)?: throw CustomResourceNotFoundException(null)
        return userRepository.save(userUpdateRequest.toUserEntity().copy(id = userToUpdate.id, uuid = userToUpdate.uuid, password = userToUpdate.password, username = userToUpdate.username, role = userToUpdate.role)).toUserDto()

    }

    /**
     * Elimina a un usuario; sólo permitido para ADMIN o si es el mismo
     */
    fun deleteUser(
        uuid: UUID,
        role: UserRole,
        usernameRequester: String
    ) {
        if(role.name != "ADMIN"){
            //validamos que el usuario a deletear sea el mismo que el que solicita el update
            val userRequester  = userRepository.findByUsername(usernameRequester)?: throw CustomException(null)
            if(userRequester.uuid != uuid){
                throw CustomAccessDeniedException(null)
            }

        }
        val userToDelete = userRepository.findByUuid(uuid)?: throw CustomResourceNotFoundException(null)
         userRepository.deleteById(userToDelete.id)

    }


}