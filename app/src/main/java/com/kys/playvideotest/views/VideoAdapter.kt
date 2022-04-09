package com.kys.playvideotest.views

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kys.playvideotest.R
import com.kys.playvideotest.models.VideoList
import kotlinx.android.synthetic.main.video_item_layout.view.*

class VideoAdapter(val context: Context, val videos: VideoList) :
    RecyclerView.Adapter<VideoAdapter.VideoViewHolder>() {

    private lateinit var mListener: onItemClickListener

    interface onItemClickListener {
        fun onItemClick(position: Int, videoURL: String)
    }

    fun setOnItemClickListener(listener: onItemClickListener) {
        mListener = listener
    }

    class VideoViewHolder(itemView: View, listener: onItemClickListener, videos: VideoList) :
        RecyclerView.ViewHolder(itemView) {

        var videoImage = itemView.iv_video

        init {
            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition, videos.videoUrls[adapterPosition])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.video_item_layout, parent, false)
        return VideoViewHolder(view, mListener, videos)
    }

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        val videoImg = videos.thumbnailUrls[position]
        if (videoImg !== null)
            Glide.with(context).load(videoImg).into(holder.videoImage)
    }

    override fun getItemCount(): Int {
        return videos.videoUrls.size
    }

}