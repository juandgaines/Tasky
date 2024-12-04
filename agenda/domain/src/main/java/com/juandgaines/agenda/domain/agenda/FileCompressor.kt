package com.juandgaines.agenda.domain.agenda

import java.io.File

interface FileCompressor {
    val maxValueCompression:Long
    suspend fun compressLocalFiles(files: List<String>): FileCompressionResult
}

data class FileCompressionResult(
    val failedFiles: Int,
    val listFiles: List<File>
)