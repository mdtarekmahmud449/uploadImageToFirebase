package com.example.todo

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.todo.databinding.SampleViewBinding
import com.example.todo.model.UploadDataClass

class ImageAdapter(val context: Context, val lists: List<UploadDataClass>): RecyclerView.Adapter<ImageAdapter.ImageViewHolder>() {
    class ImageViewHolder(val binding: SampleViewBinding): RecyclerView.ViewHolder(binding.root){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
       val view = SampleViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ImageViewHolder(view)
    }

    override fun getItemCount(): Int {
        return lists.size
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        with(holder.binding){
            val url = lists[position].imageUrl
            Glide.with(context).load(url).into(imageView)
            nameTv.text = lists[position].name
            emailTv.text = lists[position].email
            phoneTv.text = lists[position].phone
        }
    }
}