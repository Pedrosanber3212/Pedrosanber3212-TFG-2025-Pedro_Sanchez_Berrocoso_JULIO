package upm.etsit.monolito_backend.services
import org.springframework.http.*
import org.springframework.web.reactive.function.client.WebClient

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.client.RestTemplate
import org.springframework.core.io.FileSystemResource
import org.springframework.http.client.MultipartBodyBuilder
import upm.etsit.monolito_backend.exception.CustomException

import java.awt.PageAttributes
import java.io.File


@Service
class FileStorageService(
    private val userService: UserService,
    @Value("\${URL_MC_FILESTORAGE}")
     private   var URL_MC_FILESTORAGE: String

) {





    // --------------------------------------------------------------------------------


    fun uploadProductImageFile(productUUID: String, file: File): String? {

        System.out.println("uploadProductImageFile uploadProductImageFile productUUID: "+productUUID)
        val builder = MultipartBodyBuilder()
        builder.part("file", FileSystemResource(file))
        builder.part("productUUID", productUUID)

        return try {
            WebClient.builder()
                .baseUrl(URL_MC_FILESTORAGE)
                .build()
                .post()
                .uri("/api/v1/fileStorage/productImage")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .bodyValue(builder.build())
                .retrieve()
                .bodyToMono(String::class.java)
                .block()
        } catch (e: Exception) {
            throw CustomException("Error al subir imagen del producto: ${e.message}")
        }
    }

    fun deleteProductImageFile(productUUID: String): Boolean {
        System.out.println("deleteProductImageFile")
        val webClient: WebClient = WebClient.builder()
            .baseUrl(URL_MC_FILESTORAGE)
            .build()

        return try {
            webClient.delete()
                .uri("/api/v1/fileStorage/productImage/$productUUID")
                .retrieve()
                .toBodilessEntity()
                .block()
            System.out.println("deleteProductImageFile: true")
            true
        } catch (e: Exception) {
            throw CustomException("Error al eliminar imagen del producto: ${e.message}")
        }
    }
}
