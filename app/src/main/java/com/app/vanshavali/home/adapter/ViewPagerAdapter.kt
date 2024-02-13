package com.app.vanshavali.home.adapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.app.vanshavali.fraagment.OnboardingFragment
import com.app.vanshavali.home.entity.CatEntity

class ViewPagerAdapter(
    fragmentActivity: FragmentActivity,
    private val context: Context,
    private val catEntity: List<CatEntity>
) :
    FragmentStateAdapter(fragmentActivity) {

    override fun createFragment(position: Int): Fragment {
        return OnboardingFragment.newInstance(
            catEntity.get(position).categoryId,
            catEntity.get(position).categoryName,
            catEntity.get(position).categoryDetail,
            catEntity.get(position).categoryImage)
    }



    override fun getItemCount(): Int {
        return catEntity.size
    }

    fun getSelectedCatDetails(position: Int?): CatEntity? {
        return position?.let { catEntity.get(it) }
    }
}