package com.example.daitssuapi.domain.infra.service

import com.example.daitssuapi.common.constants.FileFormat
import com.example.daitssuapi.common.enums.ErrorCode
import com.example.daitssuapi.common.exception.DefaultException
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import software.amazon.awssdk.core.sync.RequestBody
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest
import software.amazon.awssdk.services.s3.model.GetUrlRequest
import software.amazon.awssdk.services.s3.model.ObjectCannedACL
import software.amazon.awssdk.services.s3.model.PutObjectRequest
import java.io.ByteArrayInputStream
import java.net.URLDecoder
import java.time.LocalDateTime

@Service
class S3Service(
    private val profile: String,
    @Value("\${aws.s3.bucket}") private val s3BucketName: String,
    @Value("\${aws.s3.url}") private val s3Endpoint: String,
) {
    fun uploadImageToS3(
        userId: Long,
        domain: String,
        fileName: String,
        imageByteArray: ByteArray,
    ): String {
        val amazonS3 = S3Client.builder().build()

        val fileNameExceptExtension = fileName.substringBeforeLast(".")
        val extension = if (fileName.substringAfterLast(".") in FileFormat.webp) "webp" else "jpg"

        val pathKey = "$profile/$domain/$userId/image/"
        val fileKey = LocalDateTime.now().toString() + "-" + fileNameExceptExtension + "." + extension
        val keyName = pathKey + fileKey

        val objectAcl = ObjectCannedACL.PUBLIC_READ

        val putObjectRequest = PutObjectRequest
            .builder()
            .bucket(s3BucketName)
            .key(keyName)
            .acl(objectAcl)
            .build()

        runCatching {
            amazonS3.putObject(
                putObjectRequest,
                RequestBody.fromInputStream(ByteArrayInputStream(imageByteArray), imageByteArray.size.toLong()),
            )
        }.onFailure {
            throw DefaultException(errorCode = ErrorCode.S3_UPLOAD_FAILED)
        }

        val url = amazonS3.utilities().getUrl(
            GetUrlRequest.builder()
                .bucket(s3BucketName)
                .key(keyName)
                .build(),
        ).toString()

        amazonS3.close()

        return url
    }

    fun deleteFromS3ByUrl(
        url: String,
    ) {
        val amazonS3 = S3Client.builder().build()
        val decodedUrlString = URLDecoder.decode(url, "UTF-8").replace("%3A", ":")

        val key = decodedUrlString.substringAfter(s3Endpoint)

        val deleteRequest = DeleteObjectRequest.builder()
            .bucket(s3BucketName)
            .key(key)
            .build()

        runCatching {
            amazonS3.deleteObject(deleteRequest)
        }.onFailure {
            throw DefaultException(errorCode = ErrorCode.S3_UPLOAD_FAILED)
        }

        amazonS3.close()
    }
}


