package com.app.vanshavali.home.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.app.vanshavali.R
import com.app.vanshavali.application.RoomApplication
import com.app.vanshavali.base.BaseActivity
import com.app.vanshavali.databinding.LandingActivityBinding
import com.app.vanshavali.home.adapter.ViewPagerAdapter
import com.app.vanshavali.home.entity.CatEntity
import com.app.vanshavali.home.viewholder.LandingActivityViewModel
import com.app.vanshavali.utils.Animatoo
import com.app.vanshavali.utils.Utils.Companion.getJsonDataFromAsset
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject

class LandingActivity : BaseActivity() {
    private var viewPagerAdapter: ViewPagerAdapter? = null
    private val landingActivityViewModel: LandingActivityViewModel by viewModels {
        LandingActivityViewModel.LandingActivityViewModelFactory((application as RoomApplication).repository)
    }
    private val ARG_CATEGORY_ID = "categoryId"
    private val ARG_CATEGORY_NAME = "categoryName"
    private val ARG_CATEGORY_DETAILS = "categoryDetail"
    private val ARG_CATEGORY_IMAGE = "categoryImage"

    private var binding: LandingActivityBinding? = null

    private var isLoadedFromDB = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        landingActivityViewModel.allCategories.observe(this) { catEntity ->
            if (catEntity.isNotEmpty()) {
                viewPagerSetup(catEntity)
            }
        }
        binding?.btnCreateAccount?.setOnClickListener {
            val catDetails = viewPagerAdapter?.getSelectedCatDetails(position = getItem())
            val intent =
                Intent(applicationContext, CatDetailsActivity::class.java)
                    .putExtra(ARG_CATEGORY_ID, catDetails?.categoryId)
                    .putExtra(ARG_CATEGORY_NAME, catDetails?.categoryName)
                    .putExtra(ARG_CATEGORY_DETAILS, catDetails?.categoryDetail)
                    .putExtra(ARG_CATEGORY_IMAGE, catDetails?.categoryImage)
            startActivity(intent)
            Animatoo.animateSlideLeft(this)
        }
        landingActivityViewModel.delete()
        //initCategoryDatabase()
        landingActivityViewModel.isAllDataSavedLiveObserver().observe(this) {
            Log.e("observe", "onCreate: $it", )
        }
        landingActivityViewModel.getCategoryListFromFirebase(
            (application as RoomApplication).firebaseDatabase.child(
                "categories"
            )
        )
    }

    private fun viewPagerSetup(list: List<CatEntity>) {
        viewPagerAdapter = ViewPagerAdapter(this, this, list)
        binding?.viewPager?.adapter = viewPagerAdapter
        binding?.viewPager?.offscreenPageLimit = 1
        binding?.viewPager?.let { TabLayoutMediator(pageIndicator, it) { _, _ -> }.attach() }
        binding?.viewPager?.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
            }

            override fun onPageScrolled(arg0: Int, arg1: Float, arg2: Int) {}
            override fun onPageScrollStateChanged(arg0: Int) {}
        })
    }

    private fun getItem(): Int? {
        return binding?.viewPager?.currentItem
    }

    private fun initCategoryDatabase() {
        val jsonFileString = getJsonDataFromAsset(applicationContext, "category.json")
        val jsonObject = JSONObject(jsonFileString)

        val status = jsonObject.getString("status")
        val message = jsonObject.getString("message")
        if (status.equals("1", ignoreCase = true)) {
            val categoryList = jsonObject.getJSONArray("category")
            if (categoryList.length() > 0) {
                for (i in 0 until categoryList.length()) {
                    val item = categoryList.getJSONObject(i)
                    val catEntity = CatEntity(
                        categoryId = item.getString("category_id"),
                        item.getString("category_name"),
                        item.getString("category_detail"),
                        item.getString("category_image"),
                    )
                    landingActivityViewModel.insert(catEntity)
                }
            }
        } else {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }
    }

//    override fun OnItemClick(catEntity: CatEntity) {
//     //   Toast.makeText(this, "".plus(position), Toast.LENGTH_SHORT).show()
//        var  intent   = Intent(this, ListActivity::class.java)
//        intent.putExtra("category_id", catEntity.categoryId)
//        startActivity(intent)
//    }

}