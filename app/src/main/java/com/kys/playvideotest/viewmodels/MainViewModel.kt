package com.kys.playvideotest.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kys.playvideotest.models.VideoList
import com.kys.playvideotest.repository.VideoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(private val repository: VideoRepository) : ViewModel() {

    init {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getVideos()
        }
    }

    val videos: LiveData<VideoList>
        get() = repository.videos
}