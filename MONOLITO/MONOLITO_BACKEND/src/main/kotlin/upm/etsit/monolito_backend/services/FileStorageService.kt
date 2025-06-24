package upm.etsit.monolito_backend.services

import com.amazonaws.AmazonServiceException
import com.amazonaws.auth.AWSStaticCredentialsProvider
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.AmazonS3ClientBuilder
import com.amazonaws.services.s3.transfer.TransferManager
import com.amazonaws.services.s3.transfer.TransferManagerBuilder
import com.amazonaws.services.s3.transfer.Upload
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import upm.etsit.monolito_backend.exception.CustomException
import java.io.File

@Service
class FileStorageService(
    private val userService: UserService
) {

    @Value("\${s3.accessKey}")
    lateinit var accessKey: String

    @Value("\${s3.secretKey}")
    lateinit var secretKey: String

    @Value("\${s3.region}")
    lateinit var region: String

    @Value("\${s3.productImagesBucket}")
    lateinit var productImagesBucket: String

    private fun buildS3Client(): AmazonS3 {
        val credentials = BasicAWSCredentials(accessKey, secretKey)
        return AmazonS3ClientBuilder.standard()
            .withRegion(region)
            .withCredentials(AWSStaticCredentialsProvider(credentials))
            .build()
    }

    fun uploadProductImageFile(productUUID: String, file: File): String? {
        val s3Client = buildS3Client()
        val transferManager: TransferManager = TransferManagerBuilder.standard()
            .withS3Client(s3Client)
            .build()

        return try {
            val upload: Upload = transferManager.upload(productImagesBucket, productUUID, file)
            upload.waitForCompletion()
            "https://$productImagesBucket.s3.amazonaws.com/$productUUID"
        } catch (e: AmazonServiceException) {
            System.err.println("Error uploading file: ${e.errorMessage}")
            throw CustomException("Error SUBIENDO ARCHIVO: ${e.errorMessage}")
        } finally {
            transferManager.shutdownNow()
        }
    }

    fun deleteProductImageFile(productUUID: String): Boolean {
        val s3Client = buildS3Client()
        return try {
            s3Client.deleteObject(productImagesBucket, productUUID)
            true
        } catch (e: AmazonServiceException) {
            System.err.println("Error deleting file: ${e.errorMessage}")
            false
        }
    }
}
