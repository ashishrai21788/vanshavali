package com.app.vanshavali.listdetail.viewmodel

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import com.app.vanshavali.landing.entity.MainEntity
import com.app.vanshavali.landing.repo.MainRepository

class ListActivityDetailsViewModel(private val repository: MainRepository): ViewModel() {

    val allList: LiveData<List<MainEntity>> = repository.getAllCategories.asLiveData()

    fun getDataByMemberId(memberID:String, parentId: String)=
        repository.getDetailsById(memberID,parentId ).asLiveData()

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun getChildCount(memberId: String) =
        repository.getChildCount(memberId)



class ListActivityDetailsViewModelFactory(private val repository: MainRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ListActivityDetailsViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return ListActivityDetailsViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}