package com.bladerco.buyandsellapp.view.adapter

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.bladerco.buyandsellapp.R
import com.bladerco.buyandsellapp.model.data.Post
import com.bladerco.buyandsellapp.view.PostDetailActivity
import com.bumptech.glide.Glide

class PostAdapter(var listOfPost: List<Post>) : RecyclerView.Adapter<PostAdapter.postHolder>() {

    private var postList: List<Post> = mutableListOf()

    fun updateList(postList: List<Post>) {
        this.postList = postList
        notifyDataSetChanged()
    }


    inner class postHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cvPost: CardView = itemView.findViewById(R.id.cv_item)
        val ivPostedImage: ImageView = itemView.findViewById(R.id.iv_posted_image)
        val tvTitle: TextView = itemView.findViewById(R.id.tv_title)
        val tvPrice: TextView = itemView.findViewById(R.id.tv_price)
        val tvPostedBy: TextView = itemView.findViewById(R.id.tv_posted_by)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): postHolder =
        postHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_post, parent, false))

    override fun getItemCount(): Int = postList.size

    override fun onBindViewHolder(holder: postHolder, position: Int) {
        val post = postList[position]

        holder.apply {
            Glide.with(itemView.context)
                .load(post.imageUrl)
                .into(ivPostedImage)

            tvTitle.text = post.title
            tvPrice.text = post.price
            tvPostedBy.text = "Posted by ${post.postedBy}"

            cvPost.setOnClickListener {
                val intent = Intent(itemView.context, PostDetailActivity::class.java)
                intent.putExtra("imageUrl" , post.imageUrl)
                intent.putExtra("title", post.title)
                intent.putExtra("description", post.description)
                intent.putExtra("price" , post.price)
                intent.putExtra("bid", post.bidCount)
                intent.putExtra("postedBy" , post.postedBy)
                intent.putExtra("position", position)
                itemView.context.startActivity(intent)
            }
        }
    }
}