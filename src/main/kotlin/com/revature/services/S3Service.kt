package com.revature.services

import org.springframework.stereotype.Service
import software.amazon.awssdk.auth.credentials.EnvironmentVariableCredentialsProvider
import software.amazon.awssdk.core.sync.RequestBody
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest
import software.amazon.awssdk.services.s3.model.GetUrlRequest
import software.amazon.awssdk.services.s3.model.PutObjectRequest
import software.amazon.awssdk.services.s3.model.S3Exception

@Service
open class S3Service {

    companion object {
        private const val BUCKET_NAME = "pl9ed-s3"
        private val S3_REGION = Region.US_EAST_1
        private const val BUCKET_PATH = "p1-refactor/"
    }

    private val s3 = S3Client.builder().region(S3_REGION).credentialsProvider(EnvironmentVariableCredentialsProvider.create())
            .build()

    /**
     * @return returns URL, empty string if error during upload
     */
    fun putObject(objKey: String, file: ByteArray, s3:S3Client = this.s3, bucketName:String = BUCKET_NAME):String {
        try {
            var response = s3.putObject(PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(BUCKET_PATH + objKey)
                    .build(), RequestBody.fromBytes(file))
            val urlRequest = GetUrlRequest.builder()
                    .bucket(BUCKET_NAME)
                    .key(BUCKET_PATH+objKey)
                    .region(S3_REGION)
                    .build()
            return s3.utilities().getUrl(urlRequest).toExternalForm()
        } catch (e: S3Exception) {
            // TODO log
        }
        return ""
    }

    // needed for mockito
    fun putObject(objKey:String, file:ByteArray):String {
        return putObject(objKey, file, s3, BUCKET_NAME)
    }
    fun deleteObject(objKey:String): Boolean {
        return deleteObject(objKey, s3, BUCKET_NAME)
    }

    fun deleteObject(objKey:String ,s3:S3Client = this.s3, bucketName:String = BUCKET_NAME): Boolean {
        try {
            val deleteRequest = DeleteObjectRequest.builder()
                    .key(BUCKET_PATH + objKey)
                    .bucket(bucketName)
                    .build()
            val deleteResponse = s3.deleteObject(deleteRequest)
            if (deleteResponse.sdkHttpResponse().statusCode()/100 == 2) {
                return true
            }
        } catch (e: S3Exception) {
            // TODO log
        }
        return false
    }

}