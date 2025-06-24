package upm.etsit.monolito_backend.controllers

import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import upm.etsit.monolito_backend.services.FileStorageService
import java.io.File

@RestController
@RequestMapping("/api/v1/fileStorage")
class FileStorageController(
    private val fileStorageService: FileStorageService
) {





    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/productImage", consumes = ["multipart/form-data"])
    fun uploadProductImage(
        @RequestParam("file") file: MultipartFile,
        @RequestParam("productUUID") productUUID: String
    ): ResponseEntity<String>{
        return ResponseEntity.ok(
            fileStorageService.uploadProductImageFile(productUUID, convertToTempFile(file))
        )
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/productImage/{productUUID}")
    fun deleteProductImage(@PathVariable productUUID: String): ResponseEntity<Boolean> {
        return ResponseEntity.ok(fileStorageService.deleteProductImageFile(productUUID))
    }

    /**
     * convertir multipart-file a file
     */
    private fun convertToTempFile(file: MultipartFile): File {
        val tempFile = File.createTempFile("upload", file.originalFilename)
        file.transferTo(tempFile)
        return tempFile
    }
}
