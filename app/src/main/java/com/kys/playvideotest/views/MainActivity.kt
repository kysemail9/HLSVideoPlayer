package com.kys.playvideotest.views

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil.setContentView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.kys.playvideotest.R
import com.kys.playvideotest.api.RetrofitHelper
import com.kys.playvideotest.api.VideoService
import com.kys.playvideotest.databinding.ActivityMainBinding
import com.kys.playvideotest.repository.VideoRepository
import com.kys.playvideotest.viewmodels.MainViewModel
import com.kys.playvideotest.viewmodels.MainViewModelFactory


class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    lateinit var mainViewModel: MainViewModel
    lateinit var adapter: VideoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = setContentView(this, R.layout.activity_main)

        val videoService = RetrofitHelper.getInstance().create(VideoService::class.java)
        val repository = VideoRepository(videoService)

        mainViewModel =
            ViewModelProvider(this, MainViewModelFactory(repository)).get(MainViewModel::class.java)

        mainViewModel.videos.observe(this, Observer {
            Log.e("KYS", it.toString())

            adapter = VideoAdapter(this, it)
            binding.rvVideoList.adapter = adapter
            adapter.setOnItemClickListener(object : VideoAdapter.onItemClickListener {
                override fun onItemClick(position: Int, videoURL: String) {

                    val intent = Intent(this@MainActivity, VideoActivity::class.java)
                    intent.putExtra("videoURL", videoURL)
                    startActivity(intent)
                }
            })
            binding.rvVideoList.layoutManager = LinearLayoutManager(this)
        })
    }
}