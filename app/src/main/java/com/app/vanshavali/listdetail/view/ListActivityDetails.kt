package com.app.vanshavali.listdetail.view


import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import com.app.vanshavali.R
import com.app.vanshavali.appInstalledOrNot
import com.app.vanshavali.application.RoomApplication
import com.app.vanshavali.databinding.ActivityUserDetailsBinding
import com.app.vanshavali.landing.entity.MainEntity
import com.app.vanshavali.listdetail.adapter.CustomAdapter
import com.app.vanshavali.listdetail.viewmodel.ListActivityDetailsViewModel
import com.app.vanshavali.openBrowser
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.app.vanshavali.landing.clickinterface.MainOnItemClickListener as MainOnItemClickListener1


class ListActivityDetails : AppCompatActivity(), MainOnItemClickListener1 {

    private val listActivityDetailsViewModel: ListActivityDetailsViewModel by viewModels {
        ListActivityDetailsViewModel.ListActivityDetailsViewModelFactory((application as RoomApplication).repositoryMain)
    }
    private lateinit var adapter: CustomAdapter
    private lateinit var binding: ActivityUserDetailsBinding
    var childList: MutableList<MainEntity> = arrayListOf()
    lateinit var mainEntity: MainEntity
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_list_details)
        var details = intent.getStringExtra("details")
        mainEntity = getDataFromString(details)
       // mainEntity.mobileNumber = "9457005007"
        adapter =
            CustomAdapter(
                this,
                repository = (application as RoomApplication).repositoryMain,
                childList, mainEntity.noOfMarriage
            )
        getDetails(mainEntity.memberId, mainEntity.parentId)
        binding.back.setOnClickListener {
            finish()
        }

        binding.call.setOnClickListener {
            if (!mainEntity.mobileNumber.isNullOrBlank() && !mainEntity.mobileNumber.equals(
                    "NA",
                    ignoreCase = true
                )
            ) {
                val intent = Intent(Intent.ACTION_DIAL);
                intent.data = Uri.parse("tel:${mainEntity.mobileNumber}")
                startActivity(intent)
            } else {
                Toast.makeText(this, "Phone number does not exist", Toast.LENGTH_SHORT).show()
            }
        }

        binding.whatsapp.setOnClickListener {
            if (!mainEntity.mobileNumber.isNullOrBlank() && !mainEntity.mobileNumber.equals(
                    "NA",
                    ignoreCase = true
                )
            ) {
                if (appInstalledOrNot("com.whatsapp") || appInstalledOrNot("com.whatsapp.w4b")) {
                    this?.openBrowser("http://api.whatsapp.com/send?phone=${mainEntity.mobileNumber}" + "&text=Hi")
                } else {
                    //  showToastS(pageResponse.language("whatsapp_not_installed", "Whats app not installed on your device"))
                    openBrowser("market://details?id=com.whatsapp")
                }
            } else {
                Toast.makeText(this, "Phone number does not exist", Toast.LENGTH_SHORT).show()
            }
        }

    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    private fun getDetails(memberId: String, parentId: String) {
        listActivityDetailsViewModel.getDataByMemberId(memberId, parentId).observe(this) { list ->
            Log.i("getDetails", "".plus(list.size))
            setDataOnView(list, parentId)
        }
    }

    private fun setDataOnView(userListWithDetail: MutableList<MainEntity>, parentId: String) {
        var childCount: Int? = 0
        binding.contentScroll.noChild = "Children's "
//        val manager = GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false)
        childList.clear()
        childList.addAll(userListWithDetail)

        if (!parentId.isNullOrBlank() && !parentId.equals("0")) {
            if (userListWithDetail.get(0).memberId.equals(parentId)) {

                //Parent data

                mainEntity
                binding?.fName = userListWithDetail.get(0).nameEnglish
                    .plus("\n")
                    .plus("parent id: ").plus(userListWithDetail.get(0).memberId)

                Glide
                    .with(this)
                    .load(userListWithDetail.get(0).profileImage)
                    .placeholder(R.drawable.default_user)
                    .error(R.drawable.default_user)
                    .into(binding.fatherImage)


                // self Data
                Glide
                    .with(this)
                    .load(userListWithDetail.get(1).profileImage)
                    .placeholder(R.drawable.default_user)
                    .error(R.drawable.default_user)
                    .into(binding.userImage)

                mainEntity = userListWithDetail.get(1)
                binding.name = userListWithDetail.get(1).nameEnglish
                    .plus(" / ")
                    .plus(userListWithDetail.get(1).nameHindi)
                binding.title = userListWithDetail.get(1).nameEnglish
                binding.phoneNumber = userListWithDetail.get(1).mobileNumber
                binding.remark = userListWithDetail.get(1).remarks
                binding.doe = userListWithDetail.get(1).doe
                binding.dob = userListWithDetail.get(1).dob
                binding.occupation =
                    "Occupation: ".plus(userListWithDetail.get(1).occupation)
                binding.positionHold =
                    "Position hold: ".plus(userListWithDetail.get(1).positionHold)
                binding.fromTo =
                    "Year From To: ".plus(userListWithDetail.get(1).fromToYear)
                binding.address = "Address: " + userListWithDetail.get(1).address
                binding.wifeId = userListWithDetail.get(1).wifeId
                binding.noOfChild = "".plus(userListWithDetail.size - 2)
                childCount = userListWithDetail.size - 2
                if (childCount <= 0) {
                    binding.noOfMarriage = "NA"
                } else {
                    binding.noOfMarriage = userListWithDetail.get(1).noOfMarriage
                }

                binding.memberId =
                    "User Id: ".plus(userListWithDetail.get(1).memberId)

                binding.fName =
                    "Father's Name: ".plus(userListWithDetail.get(0).nameEnglish)
                binding.parent = "Parent Id: ".plus(userListWithDetail.get(1).parentId)
                binding.parentName = userListWithDetail.get(0).nameEnglish
                // call recycle view data here
                childList.removeAt(0)
                childList.removeAt(0)
            }
        } else {
            //Parent data
            mainEntity = userListWithDetail.get(0)
            binding.fName = "Father's Name: NA"
            binding.parent = "Parent Id: ".plus("NA")
            binding.parentName = "NA"
            binding.title = userListWithDetail.get(0).nameEnglish
            Glide
                .with(this)
                .load(R.drawable.default_user)
                .placeholder(R.drawable.default_user)
                .error(R.drawable.default_user)
                .into(binding.fatherImage)


            // self Data
            Glide
                .with(this)
                .load(userListWithDetail.get(0).profileImage)
                .placeholder(R.drawable.default_user)
                .error(R.drawable.default_user)
                .into(binding.userImage)

            binding.name = userListWithDetail.get(0).nameEnglish
                .plus(" / ")
                .plus(userListWithDetail.get(0).nameHindi)

            binding.phoneNumber = userListWithDetail.get(0).mobileNumber
            binding.remark = userListWithDetail.get(0).remarks
            binding.doe = userListWithDetail.get(0).doe
            binding.dob = userListWithDetail.get(0).dob
            binding.occupation =
                "Occupation: ".plus(userListWithDetail.get(0).occupation)
            binding.positionHold =
                "Position hold: ".plus(userListWithDetail.get(0).positionHold)
            binding.fromTo =
                "Year From To: ".plus(userListWithDetail.get(0).fromToYear)
            binding.address = "Address: " + userListWithDetail.get(0).address
            binding.wifeId = userListWithDetail.get(0).wifeId

            binding.noOfChild = "".plus(userListWithDetail.size - 1)
            childCount = userListWithDetail.size - 1
            if (childCount <= 0) {
                binding.noOfMarriage = "NA"
            } else {
                binding.noOfMarriage = userListWithDetail.get(0).noOfMarriage
            }
            binding.noOfMarriage = userListWithDetail.get(0).noOfMarriage
            binding.fName = "Father's Name: NA"
            binding.memberId = "User Id: ".plus(userListWithDetail.get(0).memberId)
            childList.removeAt(0)
        }

        if (childList != null && childList.size > 0) {
            binding.contentScroll.liWithData.visibility = View.VISIBLE
            binding.contentScroll.noData.visibility = View.GONE
            adapter.updataList(childList, mainEntity.noOfMarriage)
        } else {
            binding.contentScroll.liWithData.visibility = View.GONE
            binding.contentScroll.noData.visibility = View.VISIBLE
        }
        binding.contentScroll.childRecycle.adapter = adapter
        binding.contentScroll.childRecycle.layoutManager =
            GridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false)

        binding.relParent.setOnClickListener {
            if (!parentId.isNullOrBlank() && !parentId.equals("0")) {
                getDetails(userListWithDetail.get(0).memberId, userListWithDetail.get(0).parentId)
            }
        }
        binding.fatherImage.setOnClickListener {
            if (!parentId.isNullOrBlank() && !parentId.equals("0")) {
                getDetails(userListWithDetail.get(0).memberId, userListWithDetail.get(0).parentId)
            }
        }
        binding.fatherName.setOnClickListener {
            if (!parentId.isNullOrBlank() && !parentId.equals("0")) {
                getDetails(userListWithDetail.get(0).memberId, userListWithDetail.get(0).parentId)
            }
        }

    }

    private fun getDataFromString(details: String?): MainEntity {
        val gson = Gson()
        details?.let { Log.i("ListActivityDetails", it) };
        return gson.fromJson(details, MainEntity::class.java);

    }

    override fun OnItemClick(mainEntity: MainEntity) {
        getDetails(mainEntity.memberId, mainEntity.parentId)
    }
}