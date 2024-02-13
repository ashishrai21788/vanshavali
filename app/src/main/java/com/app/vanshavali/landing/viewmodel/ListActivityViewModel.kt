package com.app.vanshavali.landing.viewmodel

import androidx.lifecycle.*
import com.app.vanshavali.landing.entity.MainEntity
import com.app.vanshavali.landing.repo.MainRepository
import kotlinx.coroutines.launch

class ListActivityViewModel(private val repository: MainRepository): ViewModel() {


    val allList: LiveData<List<MainEntity>> = repository.getAllCategories.asLiveData()


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
}