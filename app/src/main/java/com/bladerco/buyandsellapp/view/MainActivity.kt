package com.bladerco.buyandsellapp.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.cardview.widget.CardView
import androidx.viewpager.widget.ViewPager
import com.bladerco.buyandsellapp.R
import com.bladerco.buyandsellapp.view.adapter.MainPageAdapter
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity(), ViewPager.OnPageChangeListener {

    private lateinit var bottomNavView: BottomNavigationView
    private lateinit var viewPager: ViewPager
    private lateinit var mainPageAdapter: MainPageAdapter
    private lateinit var btnLogOut: CardView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnLogOut = findViewById(R.id.cv_log_out_button)
        bottomNavView = findViewById(R.id.main_bottom_nav)
        viewPager = findViewById(R.id.main_viewpager)
        viewPager.addOnPageChangeListener(this)
        mainPageAdapter = MainPageAdapter(supportFragmentManager)
        viewPager.adapter = mainPageAdapter


        bottomNavView.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.posts_item -> loadFragment(0)
                else -> loadFragment(1)
            }
            true
        }

        btnLogOut.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }


    fun loadFragment(i: Int) {
        viewPager.currentItem = i

    }

    override fun onPageScrollStateChanged(state: Int) {

    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

    }

    override fun onPageSelected(position: Int) {
        bottomNavView.selectedItemId = when(position){
            0 -> R.id.posts_item
            else -> R.id.add_new_item
        }
    }
}