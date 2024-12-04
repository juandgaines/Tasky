package com.juandgaines.agenda.data.agenda

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.core.net.toUri
import com.juandgaines.agenda.domain.agenda.FileCompressionResult
import com.juandgaines.agenda.domain.agenda.FileCompressor
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

class LocalImageFileCompressor @Inject constructor(
    private val context: Context
) : FileCompressor {

    override val maxValueCompression: Long
        get() =  1 * 1024 * 1024 // 1 MB

    override suspend fun compressLocalFiles(files: List<String>): FileCompressionResult {
        val compressedFiles = mutableListOf<File>()
        var failedFiles = 0

        files.map{
            it.toUri()
        }.forEach { uri ->
            try {
                // Open InputStream from URI
                val inputStream = context.contentResolver.openInputStream(uri) ?: return@forEach

                // Decode the InputStream into a Bitmap
                val bitmap = BitmapFactory.decodeStream(inputStream)
                inputStream.close()

                // Compress the Bitmap using JPEG format
                val outputStream = ByteArrayOutputStream()
                var quality = 100 // Start with the highest quality

                do {
                    outputStream.reset() // Reset the stream before each compression
                    bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)
                    quality -= 10 // Decrease quality by 10% each iteration
                } while (outputStream.size() > maxValueCompression && quality > 10)

                // If the file is small enough, save it
                if (outputStream.size() <= maxValueCompression) {
                    // Create a temporary file to save the compressed image
                    val compressedFile = File.createTempFile("compressed_", ".jpg", context.cacheDir)
                    val fileOutputStream = FileOutputStream(compressedFile)
                    fileOutputStream.write(outputStream.toByteArray())
                    fileOutputStream.flush()
                    fileOutputStream.close()

                    compressedFiles.add(compressedFile) // Add to the list of compressed files
                }

            } catch (e: Exception) {
                e.printStackTrace() // Log the exception and skip this URI
                failedFiles++
            }
        }

        return FileCompressionResult(failedFiles, compressedFiles)
    }
}