package com.example.mvvm.adapter

import android.content.ClipData.Item
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.size.Scale
import com.example.mvvm.DetailesMovieActivity
import com.example.mvvm.R
import com.example.mvvm.databinding.ItemRowBinding
import com.example.mvvm.response.MovieListResponse
import com.example.mvvm.utils.Constants.POSTER_BASEURL

class MovieAdapter : RecyclerView.Adapter<MovieAdapter.ViewHolder>() {

    private lateinit var binding: ItemRowBinding
    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val inflater = LayoutInflater.from(parent.context)
        binding = ItemRowBinding.inflate(inflater, parent, false)
        context = parent.context
        return ViewHolder()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    inner class ViewHolder: RecyclerView.ViewHolder(binding.root){
        fun bind(item: MovieListResponse.Result){
            binding.apply{
                tvMovieName.text = item.title
                tvRate.text = item.vote_average.toString()
                val moviePosterURL = POSTER_BASEURL + item.poster_path
                imgMovie.load(moviePosterURL){
                    crossfade(true)
                    placeholder(R.drawable.poster_placeholder)
                    scale(Scale.FILL)
                }
                tvLang.text = item.original_language
                tvMovieDateReleased.text = item.release_date

                root.setOnClickListener {
                    val intent = Intent(context, DetailesMovieActivity::class.java)
                    intent.putExtra("id", item.id)
                    context.startActivity(intent)
                }
            }

        }

    }

    private val differCallback = object : DiffUtil.ItemCallback<MovieListResponse.Result>(){
        override fun areItemsTheSame(
            oldItem: MovieListResponse.Result,
            newItem: MovieListResponse.Result
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: MovieListResponse.Result,
            newItem: MovieListResponse.Result
        ): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this,differCallback)
}