package com.example.daitssuapi.domain.infra.service

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
) {
    fun uploadImageToS3(
        userId: Long,
        domain: String,
        fileName: String,
        imageByteArray: ByteArray,
    ): String {
        val amazonS3 = S3Client.builder().build()

        val fileNameExceptExtension = fileName.substringBeforeLast(".")
        val extension = if (fileName.substringAfterLast(".") in listOf("webp", "WEBP")) "webp" else "jpg"

        val keyName = "$profile/$domain/$userId/image/" + LocalDateTime.now()
            .toString() + "-" + fileNameExceptExtension + "." + extension

        val objectAcl = ObjectCannedACL.PUBLIC_READ

        val putObjectRequest = PutObjectRequest
            .builder()
            .bucket(bucketName)
            .key(keyName)
            .acl(objectAcl)
            .build()

        amazonS3.putObject(
            putObjectRequest,
            RequestBody.fromInputStream(ByteArrayInputStream(imageByteArray), imageByteArray.size.toLong()),
        )

        val url = amazonS3.utilities().getUrl(
            GetUrlRequest.builder()
                .bucket(bucketName)
                .key(keyName)
                .build(),
        ).toString()

        amazonS3.close()

        return url
    }

    fun deleteImageFromS3(
        url: String,
    ) {
        val amazonS3 = S3Client.builder().build()
        val decodedUrlString = URLDecoder.decode(url, "UTF-8").replace("%3A", ":")

        val key = decodedUrlString.substringAfter(endPointUrl)

        val deleteRequest = DeleteObjectRequest.builder()
            .bucket(bucketName)
            .key(key)
            .build()

        amazonS3.deleteObject(deleteRequest)

        amazonS3.close()
    }

    companion object {
        const val bucketName = "daitssu-bucket"
        const val endPointUrl = "https://daitssu-bucket.s3.ap-northeast-2.amazonaws.com/"
    }
}


