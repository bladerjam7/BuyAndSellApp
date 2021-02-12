package com.bladerco.buyandsellapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.bladerco.buyandsellapp.model.ItemRepository
import com.bladerco.buyandsellapp.model.data.Post

class ItemViewModel : ViewModel() {

    fun getPosts(): LiveData<List<Post>> = ItemRepository.getPost()

    fun uploadPost(post: Post){
        ItemRepository.updatePost(post)
    }

    fun uploadBid(bid: Int, position: Int){
        ItemRepository.updatePostBid(bid, position)
    }

    fun getIdValue()
    {
        ItemRepository.getItemKey()
    }
}