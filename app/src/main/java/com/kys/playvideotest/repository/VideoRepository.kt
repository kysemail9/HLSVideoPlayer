package com.kys.playvideotest.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.kys.playvideotest.api.VideoService
import com.kys.playvideotest.models.VideoList

class VideoRepository(private val videoService: VideoService) {

    private val videosLiveData = MutableLiveData<VideoList>()

    val videos: LiveData<VideoList>
        get() = videosLiveData


    suspend fun getVideos() {
        val result = videoService.getHLSTestingVideos()
        if (result?.body() != null) {
            videosLiveData.postValue(result.body())
        }
    }
}