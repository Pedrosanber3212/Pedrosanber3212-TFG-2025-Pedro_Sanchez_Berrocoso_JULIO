package upm.etsit.monolito_backend.services

import com.amazonaws.AmazonServiceException
import com.amazonaws.auth.AWSStaticCredentialsProvider
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.client.builder.AwsClientBuilder
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

    @Value("\${s3.endpoint}")
    lateinit var endpoint: String

    @Value("\${s3.region}")
    lateinit var region: String



    @Value("\${s3.productImagesBucket}")
    lateinit var productImagesBucket: String

    @Value("\${s3.useCustomClient}")
    lateinit var useCustomClient: String


    // --------------------------------------------------------------------------------


    fun uploadProductImageFile(productUUID: String, file: File): String? {
        return uploadFile(productImagesBucket, productUUID, file)
    }

    fun deleteProductImageFile(productUUID: String): Boolean {
        return deleteFile(productImagesBucket, productUUID)
    }

    // --------------------------------------------------------------------------------

    private fun uploadFile(bucketName: String, keyName: String, file: File): String? {
        val transferManager = if (useCustomClient.toBoolean()) {
            createCustomTransferManager()
        } else {
            TransferManagerBuilder.standard().build()
        }

        try {
            val upload: Upload = transferManager.upload(bucketName, keyName, file)
            upload.waitForCompletion()

            if (useCustomClient.toBoolean()) {
                return   "$endpoint/$bucketName/$keyName"
            } else {
                return   "https://${bucketName}.s3.amazonaws.com/$keyName"
            }

        } catch (e: AmazonServiceException) {
            System.err.println("Error uploading file: ${e.errorMessage}")
            throw CustomException("Error SUBIENDO ARCHIVO: "+ e.errorMessage)
        } finally {
            transferManager.shutdownNow()
        }
    }

    private fun deleteFile(bucketName: String, keyName: String): Boolean {
        var s3Client: AmazonS3 = if (useCustomClient.toBoolean()) {
            createCustomS3Client()
        } else {
            AmazonS3ClientBuilder.defaultClient()
        }

        return try {
            s3Client.deleteObject(bucketName, keyName)
            true
        } catch (e: AmazonServiceException) {
            System.err.println("Error deleting file: ${e.errorMessage}")
            false
        }
    }

    private fun createCustomTransferManager(): TransferManager {
        return TransferManagerBuilder.standard()
            .withS3Client(createCustomS3Client())
            .build()
    }

    fun createCustomS3Client(): AmazonS3 {
        val credentials = BasicAWSCredentials(accessKey, secretKey)
        return AmazonS3ClientBuilder.standard()
            .withEndpointConfiguration(AwsClientBuilder.EndpointConfiguration(endpoint, region))
            .withCredentials(AWSStaticCredentialsProvider(credentials))
            .withPathStyleAccessEnabled(true)
            .build()
    }
}
