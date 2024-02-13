package com.app.vanshavali.landing.view

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.vanshavali.R
import com.app.vanshavali.application.RoomApplication
import com.app.vanshavali.base.BaseActivity
import com.app.vanshavali.databinding.ListActivityBinding
import com.app.vanshavali.home.view.LandingActivity
import com.app.vanshavali.landing.adapter.MainListAdapter
import com.app.vanshavali.landing.clickinterface.MainOnItemClickListener
import com.app.vanshavali.landing.entity.MainEntity
import com.app.vanshavali.landing.viewmodel.ListActivityViewModel
import com.app.vanshavali.listdetail.view.ListActivityDetails
import com.app.vanshavali.utils.Animatoo
import com.app.vanshavali.utils.Utils.Companion.getJsonDataFromAsset
import com.google.gson.Gson
import org.json.JSONObject

class ListActivity : BaseActivity(), MainOnItemClickListener {
    private val listActivityViewModel: ListActivityViewModel by viewModels {
        ListActivityViewModel.ListActivityViewModelFactory((application as RoomApplication).repositoryMain)
    }

    private val ARG_CATEGORY_ID by lazy { "categoryId" }
    private val ARG_CATEGORY_NAME by lazy { "categoryName" }
    private val ARG_CATEGORY_DETAILS by lazy { "categoryDetail" }
    private val ARG_CATEGORY_IMAGE by lazy { "categoryImage" }


    private lateinit var categoryId: String
    private lateinit var categoryName: String
    private lateinit var categoryDetail: String
    private lateinit var categoryImage: String
    private var binding: ListActivityBinding? = null
    private var recordSize = 0
    private var adapter: MainListAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        window.statusBarColor = ContextCompat.getColor(this, R.color.white)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_list)

        if (intent != null) {
            categoryId = intent.getStringExtra(ARG_CATEGORY_ID).toString()
            categoryName =
                intent.getStringExtra(ARG_CATEGORY_NAME).toString()
            categoryDetail =
                intent.getStringExtra(ARG_CATEGORY_DETAILS).toString()
            categoryImage =
                intent.getStringExtra(ARG_CATEGORY_IMAGE).toString()
        }


        val recyclerView = findViewById<RecyclerView>(R.id.recyclerview)
        adapter =
            MainListAdapter(this, repository = (application as RoomApplication).repositoryMain)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        binding?.progressBar?.visibility = View.VISIBLE
        listActivityViewModel.allList.observe(this) { catEntity ->
            Log.i("getAllCategories", "test ".plus(catEntity.size))
            binding?.count = "".plus(catEntity.size)
            if (recordSize == catEntity.size) {
                catEntity.let {
                    adapter?.submitList(it)
                    adapter?.mainList = it as MutableList<MainEntity>
                    binding?.progressBar?.visibility = View.GONE
                }
            }
        }

        binding?.searchInput?.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(newText: Editable) {
                adapter?.filter?.filter(newText)
            }

            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            ) {
            }
        })

        binding?.iconHome?.setOnClickListener {
            val intent =
                Intent(applicationContext, LandingActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            Animatoo.animateSlideLeft(this)
        }
        listActivityViewModel.delete()
        initListDatabase()
    }

    override fun onResume() {
        super.onResume()
        Handler().postDelayed({
            if (binding?.searchInput?.text?.length!! >= 0) {
                adapter?.filter?.filter(binding?.searchInput?.text.toString())
                Log.i("onResume ", binding?.searchInput?.text.toString())
            }
        }, 1000)

    }

    private fun initListDatabase() {

        val jsonFileString = getJsonDataFromAsset(applicationContext, "main_list.json")
        val jsonObject = JSONObject(jsonFileString)

        val status = jsonObject.getString("status")
        val message = jsonObject.getString("message")
        if (status.equals("1", ignoreCase = true)) {
            val categoryList = jsonObject.getJSONArray("main_list")
            recordSize = categoryList.length()
            if (categoryList.length() > 0) {
                for (i in 0 until categoryList.length()) {
                    val item = categoryList.getJSONObject(i)
                    if (item.getString("category_id").equals(categoryId)) {
                        val mainEntity = MainEntity(
                            memberId = item.optString("member_id"),
                            parentId = item.optString("parent_id"),
                            nameEnglish = item.optString("name_english"),
                            nameHindi = item.optString("name_hindi"),
                            isAlive = item.optString("is_alive"),
                            noOfChildren = item.optString("no_of_children"),
                            wifeId = item.optString("wife_id"),
                            noOfMarriage = item.optString("no_of_marriage"),
                            item.optString("page_no"),
                            item.optString("category_id"),
                            item.optString("dob"),
                            item.optString("doe"),
                            item.optString("profile_image"),
                            item.optString("address"),
                            item.optString("mobile_number"),
                            item.optString("occupation"),
                            item.optString("position_hold"),
                            item.optString("from_to_year"),
                            item.optString("remarks"),
                        )
                        listActivityViewModel.insert(mainEntity)
                    }
                }
            }
        } else {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }
    }

    override fun OnItemClick(mainEntity: MainEntity) {
        //Toast.makeText(this, "".plus(mainEntity.nameEnglish), Toast.LENGTH_SHORT).show()
        val intent = Intent(this, ListActivityDetails::class.java)
        intent.putExtra("details", getStringFromData(mainEntity))
        startActivity(intent)
    }

    private fun getStringFromData(mainEntity: MainEntity): String {
        val gson = Gson()
        return gson.toJson(mainEntity)
    }
}