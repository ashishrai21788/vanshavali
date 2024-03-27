package com.app.vanshavali.home.viewholder

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.*
import com.app.vanshavali.home.entity.CatEntity
import com.app.vanshavali.home.repo.RoomRepository
import com.app.vanshavali.landing.view.ListActivity
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import kotlinx.coroutines.launch

class LandingActivityViewModel(private val repository: RoomRepository) : ViewModel() {

    private val TAG = LandingActivityViewModel::class.java.simpleName
    val allCategories: LiveData<List<CatEntity>> = repository.getAllCategories.asLiveData()
    val isLoading: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    val isAllDataSaved: MutableLiveData<Boolean> = MutableLiveData<Boolean>()

    fun isAllDataSavedLiveObserver(): MutableLiveData<Boolean> {
        return isAllDataSaved
    }

    fun delete() = viewModelScope.launch {
        repository.delete()
    }

    fun insert(catEntity: CatEntity) = viewModelScope.launch {
        repository.insert(catEntity)

    }

    class LandingActivityViewModelFactory(private val repository: RoomRepository) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(LandingActivityViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return LandingActivityViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }

    fun getCategoryListFromFirebase(firebaseDatabase: DatabaseReference) {
        firebaseDatabase.addChildEventListener(childEventListener)
        isAllDataSaved.postValue(false)
    }

    val childEventListener = object : ChildEventListener {
        override fun onChildAdded(dataSnapshot: DataSnapshot, previousChildName: String?) {
            //Log.d(TAG, "onChildAdded:" + dataSnapshot.key!!)

            // A new comment has been added, add it to the displayed list
            // val comment = dataSnapshot.getValue<MainEntity>()

            // ...
            val data = CatEntity("", "", "", "")
            for (messageSnapshot in dataSnapshot.children) {
                when (messageSnapshot.key) {
                    "category_id" -> {
                        data.categoryId = messageSnapshot.value.toString()
                    }

                    "category_name" -> {
                        data.categoryName = messageSnapshot.value.toString()
                    }

                    "category_image" -> {
                        data.categoryImage = messageSnapshot.value.toString()
                    }

                    "category_detail" -> {
                        data.categoryDetail = messageSnapshot.value.toString()
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