package com.bladerco.buyandsellapp.view.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.bladerco.buyandsellapp.view.fragment.AddNewFragment
import com.bladerco.buyandsellapp.view.fragment.MainPageFragment

class MainPageAdapter(fragmentManager: FragmentManager):
    FragmentPagerAdapter(fragmentManager, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private val mainPageFragment = MainPageFragment()
    private val addNewFragment = AddNewFragment()

    override fun getItem(position: Int): Fragment {
        return when(position){
            0 -> mainPageFragment
            else -> addNewFragment
        }
    }

    override fun getCount(): Int = 2


}