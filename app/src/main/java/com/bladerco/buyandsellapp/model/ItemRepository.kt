package com.bladerco.buyandsellapp.model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bladerco.buyandsellapp.model.data.Post
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

object ItemRepository {

    private val itemLiveData: MutableLiveData<List<Post>> = MutableLiveData()
    private var idCounter = 0L

    val firebaseDB: FirebaseDatabase = FirebaseDatabase.getInstance()

    init {
        firebaseDB.setPersistenceEnabled(true)  // Handles disconnection
    }

    fun getPost(): LiveData<List<Post>>{

        firebaseDB.reference.child("POSTS")
            .addValueEventListener(object: ValueEventListener{
                override fun onCancelled(error: DatabaseError) {
                    Log.d("TAG_X", "Error: $error")
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                   val postList = mutableListOf<Post>()

                        idCounter = snapshot.childrenCount
                        Log.d("TAG", "onDataChange: $idCounter")

                    snapshot.children.forEach {
                        Log.d("TAG_X", "data: ${it.getValue(Post::class.java)?.price}")
                        it.getValue(Post::class.java)?.let { post ->
                            postList.add(post)
                        }
                    }

                    postList.forEach {
                        Log.d("TAG_Y", "postList: ${it.description}")
                    }
                    itemLiveData.value = postList
                }

            })

        return itemLiveData
    }

    fun updatePost(post: Post){
        firebaseDB.reference.child("POSTS").child("${idCounter}").setValue(post)
        //firebaseDB.reference.child("POSTS").push().setValue(post)
    }

    fun updatePostBid(addBid: Int, position: Int) {

        firebaseDB.reference.child("POSTS").child("$position").child("bidCount").setValue(addBid)
    }

    fun getItemKey(){
        /*Log.d("TAG", "getItemKey: ${firebaseDB.reference}")
         firebaseDB.reference.child("POSTS").child("item").setValue("test")*/
    }
}