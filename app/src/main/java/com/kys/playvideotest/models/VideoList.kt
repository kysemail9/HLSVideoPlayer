package com.kys.playvideotest.models

data class VideoList(
    val error: Any,
    val errorCode: Int,
    val exceptionData: Any,
    val success: Boolean,
    val thumbnailUrls: List<String>,
    val userDisplayName: Any,
    val videoUrls: List<String>
)