package upm.etsit.monolito_backend.controllers

import com.sun.security.auth.UserPrincipal
import jakarta.servlet.http.HttpServletRequest
import org.apache.tomcat.util.net.openssl.ciphers.Authentication
import org.springframework.context.annotation.Role
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import upm.etsit.monolito_backend.dto.UserDto
import upm.etsit.monolito_backend.dto.UserRegisterRequestDto
import upm.etsit.monolito_backend.dto.UserUpdateRequest
import upm.etsit.monolito_backend.models.UserRole

import upm.etsit.monolito_backend.services.UserService
import java.awt.Image
import java.io.File
import java.net.http.HttpHeaders
import java.nio.file.Files
import java.nio.file.Paths
import java.util.UUID

@RestController
    @RequestMapping("/api/v1/users")
class UserController(
    private val userService: UserService
) {



    @GetMapping("/me_info")
    fun me3(@AuthenticationPrincipal principal: UserDetails): ResponseEntity<UserDto> {
        var userdto: UserDto = userService.findUserByUsername(principal.username)
        return ResponseEntity.ok(userdto)
    }


    @GetMapping("/me")
    fun me(): ResponseEntity<String> {
        return ResponseEntity.ok("Login successful")
    }

   

    @GetMapping("/me3")
    fun me( @AuthenticationPrincipal authentication: UserDetails?): ResponseEntity<UserDetails> {


        return ResponseEntity.ok(authentication)
    }


    /**
     * POST!!!!--------------------------------------------------------------------------------------
     */
    @PostMapping("/register")
    fun register(@RequestBody userRegisterRequestDto: UserRegisterRequestDto): ResponseEntity<Any>{
        println("/register")
        return ResponseEntity.ok(userService.register(userRegisterRequestDto))

    }



    /**
     * POST!!!!--------------------------------------------------------------------------------------
     */
    @PostMapping("/register_admin")
    fun register_admin(@RequestBody userRegisterRequestDto: UserRegisterRequestDto): ResponseEntity<Any>{
        println("/register_admin")
        return ResponseEntity.ok(userService.register_admin(userRegisterRequestDto))

    }


    /**
     * GET!!!!--------------------------------------------------------------------------------------
     */

    /**
     * Listar todos los usuarios.
     * Solo puede hacerlo un usuario con rol ADMIN.
     */

    @GetMapping
    fun getAllUsers(): ResponseEntity<Any> {
       return  ResponseEntity.ok( userService.findAllUsers(), )

    }

    /**
     * Obtener detalle de 1 usuario
     * - ADMIN puede ver a cualquiera
     * - Un usuario solo puede ver su propio perfil.
     */
@PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{uuid}")
    fun getUserByUuid(@PathVariable uuid: UUID ,  @AuthenticationPrincipal currentUser: UserDetails): ResponseEntity<Any> {
        val roleName = currentUser.authorities.first().authority
        val roleEnum = UserRole.valueOf(roleName.substringAfter("_"))

        return ResponseEntity.ok(userService.findUserByUuid(uuid  ))
    }




    /**
     * PUT!!!!--------------------------------------------------------------------------------------
     */

    /**
     * Actualizar datos del usuario
     * - ADMIN puede actualizar cualquier usuario
     * - El usuario solo puede actualizar su propio perfil
     */
    @PutMapping("/{uuid}")
    fun updateUser(
        @PathVariable uuid: UUID,
        @RequestBody userUpdateRequest: UserUpdateRequest,
        @AuthenticationPrincipal currentUser: UserDetails
    ): ResponseEntity<UserDto> {
        val roleName = currentUser.authorities.first().authority
        val roleEnum = UserRole.valueOf(roleName.substringAfter("_"))

        return ResponseEntity.ok(userService.updateUser(uuid , userUpdateRequest,roleEnum, currentUser.username));
    }




    /**
     * DELETE!!!!--------------------------------------------------------------------------------------
     */
    /**
     * Borrar usuario (solo ADMIN). U SE BORRA A SÍ MISMO
     */
    @DeleteMapping("/{uuid}")
    fun deleteUser(
        @PathVariable uuid: UUID,
        @AuthenticationPrincipal currentUser: UserDetails
    ): ResponseEntity<Void> {

        val roleName = currentUser.authorities.first().authority
        val roleEnum = UserRole.valueOf(roleName.substringAfter("_"))


        userService.deleteUser(uuid , roleEnum, currentUser.username)
        return ResponseEntity.ok(null);
    }
}
