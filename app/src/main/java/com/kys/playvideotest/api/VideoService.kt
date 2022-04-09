package com.kys.playvideotest.api

import com.kys.playvideotest.models.VideoList
import retrofit2.Response
import retrofit2.http.GET

interface VideoService {

    @GET("video/getHLSTestingVideos")
    suspend fun getHLSTestingVideos() : Response<VideoList>
}