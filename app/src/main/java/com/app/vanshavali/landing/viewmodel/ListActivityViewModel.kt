package com.app.vanshavali.landing.viewmodel

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.*
import com.app.vanshavali.home.entity.CatEntity
import com.app.vanshavali.home.viewholder.LandingActivityViewModel
import com.app.vanshavali.landing.entity.MainEntity
import com.app.vanshavali.landing.repo.MainRepository
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import kotlinx.coroutines.launch

class ListActivityViewModel(private val repository: MainRepository): ViewModel() {

    private val TAG = LandingActivityViewModel::class.java.simpleName
    val allList: LiveData<List<MainEntity>> = repository.getAllCategories.asLiveData()

    val isAllDataSaved: MutableLiveData<Boolean> = MutableLiveData<Boolean>()

    fun isAllDataSavedLiveObserver(): MutableLiveData<Boolean> {
        return isAllDataSaved
    }
    fun hasChild() = viewModelScope.launch {
        repository.hasChild()
    }
    fun delete() = viewModelScope.launch {
        repository.delete()
    }
    fun insert(mainEntity: MainEntity) = viewModelScope.launch {
        repository.insert(mainEntity)
    }

    class ListActivityViewModelFactory(private val repository: MainRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ListActivityViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return ListActivityViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }

    fun getMainListFromFirebase(firebaseDatabase: DatabaseReference) {
        firebaseDatabase.addChildEventListener(childEventListener)
        isAllDataSaved.postValue(false)
    }

    val childEventListener = object : ChildEventListener {
        override fun onChildAdded(dataSnapshot: DataSnapshot, previousChildName: String?) {
            Log.d(TAG, "onChildAdded:" + dataSnapshot.key!!)

            // A new comment has been added, add it to the displayed list
            // val comment = dataSnapshot.getValue<MainEntity>()

            // ...
            val data = MainEntity(
                "", "", "",
                "", "", "",
                "", "", "",
                "", "", "",
                "", "", "",
                "", "", "", ""
                )
            for (messageSnapshot in dataSnapshot.children) {
                when (messageSnapshot.key) {
                    "member_id" -> {
                        data.memberId = messageSnapshot.value.toString()
                    }

                    "parent_id" -> {
                        data.parentId = messageSnapshot.value.toString()
                    }

                    "name_english" -> {
                        data.nameEnglish = messageSnapshot.value.toString()
                    }

                    "name_hindi" -> {
                        data.nameHindi = messageSnapshot.value.toString()
                    }
                    "is_alive" -> {
                        data.isAlive = messageSnapshot.value.toString()
                    }
                    "no_of_children" -> {
                        data.noOfChildren = messageSnapshot.value.toString()
                    }
                    "wife_id" -> {
                        data.wifeId = messageSnapshot.value.toString()
                    }
                    "no_of_marriage" -> {
                        data.noOfMarriage = messageSnapshot.value.toString()
                    }
                    "page_no" -> {
                        data.pageNo = messageSnapshot.value.toString()
                    }
                    "category_id" -> {
                        data.categoryId = messageSnapshot.value.toString()
                    }
                    "dob" -> {
                        data.dob = messageSnapshot.value.toString()
                    }
                    "doe" -> {
                        data.doe = messageSnapshot.value.toString()
                    }

                    "profile_image" -> {
                        data.profileImage = messageSnapshot.value.toString()
                    }
                    "address" -> {
                        data.address = messageSnapshot.value.toString()
                    }
                    "mobile_number" -> {
                        data.mobileNumber = messageSnapshot.value.toString()
                    }
                    "occupation" -> {
                        data.occupation = messageSnapshot.value.toString()
                    }
                    "position_hold" -> {
                        data.positionHold = messageSnapshot.value.toString()
                    }
                    "from_to_year" -> {
                        data.fromToYear = messageSnapshot.value.toString()
                    }
                    "remarks" -> {
                        data.remarks = messageSnapshot.value.toString()
                    }



                }
            }
            insert(data)
            isAllDataSaved.postValue(true)


        }

        override fun onChildChanged(dataSnapshot: DataSnapshot, previousChildName: String?) {
            Log.d(TAG, "onChildChanged: ${dataSnapshot.key}")

            // A comment has changed, use the key to determine if we are displaying this
            // comment and if so displayed the changed comment.
//            val newComment = dataSnapshot.getValue<Comment>()
//            val commentKey = dataSnapshot.key

            // ...
        }

        override fun onChildRemoved(dataSnapshot: DataSnapshot) {
            Log.d(TAG, "onChildRemoved:" + dataSnapshot.key!!)

            // A comment has changed, use the key to determine if we are displaying this
            // comment and if so remove it.
            val commentKey = dataSnapshot.key

            // ...
        }

        override fun onChildMoved(dataSnapshot: DataSnapshot, previousChildName: String?) {
            Log.d(TAG, "onChildMoved:" + dataSnapshot.key!!)

            // A comment has changed position, use the key to determine if we are
            // displaying this comment and if so move it.
//            val movedComment = dataSnapshot.getValue<Comment>()
//            val commentKey = dataSnapshot.key

            // ...
        }

        override fun onCancelled(databaseError: DatabaseError) {
            isAllDataSaved.postValue(false)
            Log.w(TAG, "postComments:onCancelled", databaseError.toException())
        }
    }

}