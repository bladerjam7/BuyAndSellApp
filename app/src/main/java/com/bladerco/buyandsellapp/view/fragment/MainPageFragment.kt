package com.bladerco.buyandsellapp.view.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.bladerco.buyandsellapp.R
import com.bladerco.buyandsellapp.view.LoginActivity
import com.bladerco.buyandsellapp.view.adapter.PostAdapter
import com.bladerco.buyandsellapp.viewmodel.ItemViewModel
import com.google.firebase.auth.FirebaseAuth

class MainPageFragment : Fragment() {

    private lateinit var btnAddNew: CardView
    private lateinit var btnLogOut: CardView
    private lateinit var rvPostRecyclerView: RecyclerView

    private val itemViewModel: ItemViewModel by viewModels()
    private val postAdapter: PostAdapter = PostAdapter(listOf())


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_main_page, container,false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.apply {
            rvPostRecyclerView = findViewById(R.id.rv_list_of_post)
        }


        rvPostRecyclerView.adapter = postAdapter

        /*btnAddNew.setOnClickListener {
            startActivity(Intent(view.context, AddNewActivity::class.java))
        }*/
        itemViewModel.getPosts().observe(viewLifecycleOwner, Observer {
            postAdapter.updateList(it)
        })



    }


}