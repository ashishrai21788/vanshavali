package com.app.vanshavali.home.viewholder

import androidx.lifecycle.*
import com.app.vanshavali.home.entity.CatEntity
import com.app.vanshavali.home.repo.RoomRepository
import kotlinx.coroutines.launch

class LandingActivityViewModel(private val repository: RoomRepository): ViewModel() {


    val allCategories: LiveData<List<CatEntity>> = repository.getAllCategories.asLiveData()

    fun delete() = viewModelScope.launch {
        repository.delete()
    }
    fun insert(catEntity: CatEntity) = viewModelScope.launch {
        repository.insert(catEntity)
        
    }

    class LandingActivityViewModelFactory(private val repository: RoomRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(LandingActivityViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return LandingActivityViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}