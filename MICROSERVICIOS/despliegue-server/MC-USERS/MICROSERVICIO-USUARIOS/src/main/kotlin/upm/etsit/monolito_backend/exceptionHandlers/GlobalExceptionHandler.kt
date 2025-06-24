package upm.etsit.monolito_backend.exception

import org.springframework.http.HttpStatus.*
import org.springframework.http.ResponseEntity
import org.springframework.security.access.AccessDeniedException
import org.springframework.web.bind.annotation.*

@RestControllerAdvice
class GlobalExceptionHandler {

    // 400 - Datos inválidos
    @ExceptionHandler(CustomIllegalArgumentException::class)
    fun handleBadRequest(ex: Exception): ResponseEntity<String> {
        return ResponseEntity.status(BAD_REQUEST).body(ex.message ?: "Solicitud mal formada")
    }



    //  403 - Acceso denegado
    @ExceptionHandler(CustomAccessDeniedException::class)
    fun handleForbidden(ex: Exception): ResponseEntity<String> {
        return ResponseEntity.status(FORBIDDEN).body("No tienes permisos para acceder a este recurso")
    }

    // 🔍 404 - Recurso no encontrado
    @ExceptionHandler(CustomResourceNotFoundException::class)
    fun handleNotFound(ex: Exception): ResponseEntity<String> {
        return ResponseEntity.status(NOT_FOUND).body(ex.message ?: "Recurso no encontrado")
    }

    // 409 - Conflicto (duplicados .)
    @ExceptionHandler(CustomConflictException::class)
    fun handleConflict(ex: Exception): ResponseEntity<String> {

        return ResponseEntity.status(CONFLICT).body(ex.message ?: "Conflicto en los datos enviados")
    }

    // 500 - Error inesperado
    @ExceptionHandler(CustomException::class)
    fun handleServerError(ex: Exception): ResponseEntity<String> {
        //ex.printStackTrace()
        return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(ex.message ?:"Error inesperado en el servidor")
    }
}


class CustomResourceNotFoundException(message: String?) : RuntimeException(message)

class CustomConflictException(message: String?) : RuntimeException(message)

class CustomAccessDeniedException(message: String?) : RuntimeException(message)


class CustomIllegalArgumentException(message: String?) : RuntimeException(message)

class CustomException(message: String?) : RuntimeException(message)
