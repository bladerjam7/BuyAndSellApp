package com.bladerco.buyandsellapp.view

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.cardview.widget.CardView
import com.bladerco.buyandsellapp.R
import com.bladerco.buyandsellapp.viewmodel.ItemViewModel
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth

class PostDetailActivity : AppCompatActivity() {

    private lateinit var ivPostedImage: ImageView
    private lateinit var tvTitle: TextView
    private lateinit var tvDescription: TextView
    private lateinit var tvPostedBy: TextView
    private lateinit var tvPrice: TextView
    private lateinit var btnEmail: ImageView
    private lateinit var tvBidNumber: TextView
    private lateinit var btnBid: CardView

    private val itemViewModel: ItemViewModel by viewModels()

    private lateinit var imageUrl : String
    private lateinit var title: String
    private lateinit var description: String
    private lateinit var price: String
    private var bidCount: Int = 0
    private lateinit var postedBy: String
    private var position: Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_detail)

        ivPostedImage = findViewById(R.id.iv_posted_image)
        tvTitle = findViewById(R.id.tv_title_detail)
        tvDescription = findViewById(R.id.tv_description)
        tvPostedBy = findViewById(R.id.tv_posted_by)
        tvPrice = findViewById(R.id.tv_price)
        btnEmail = findViewById(R.id.btn_email)
        tvBidNumber = findViewById(R.id.tv_bid_number)
        btnBid = findViewById(R.id.btn_bid)

        imageUrl = intent.getStringExtra("imageUrl").toString()
        title = intent.getStringExtra("title").toString()
        description = intent.getStringExtra("description").toString()
        price = intent.getStringExtra("price").toString()
        bidCount = intent.getIntExtra("bid", 0)
        postedBy = intent.getStringExtra("postedBy").toString()
        position = intent.getIntExtra("position",0)


        Glide.with(this)
            .load(imageUrl)
            .into(ivPostedImage)

        tvTitle.text = title
        tvDescription.text = description
        tvPostedBy.text = postedBy
        tvPrice.text = "$$price"
        tvBidNumber.text = bidCount.toString()

        setUpOnClick()
    }

    private fun setUpOnClick() {
        btnEmail.setOnClickListener {
            val name = FirebaseAuth.getInstance().currentUser?.displayName
            val intent = Intent(Intent.ACTION_SENDTO).apply {
                setData(Uri.parse("mailto:$postedBy"))
                putExtra(Intent.EXTRA_EMAIL, name)
                putExtra(Intent.EXTRA_SUBJECT, "I am interested in buying your $title")
            }

            startActivity(intent)
        }

        btnBid.setOnClickListener {
            tvBidNumber.text = (++bidCount).toString()
            itemViewModel.uploadBid(bidCount, position)
        }
    }
}