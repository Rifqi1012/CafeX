package com.example.cafeplatform.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cafeplatform.data.model.Cafe
import com.example.cafeplatform.data.repository.CafeRepository

class CafeViewModel : ViewModel() {

    private val cafeRepo = CafeRepository()

    private val _cafeData = MutableLiveData<Cafe?>()
    val cafeData: LiveData<Cafe?> = _cafeData

    fun getCafeByOwner(uid: String) {
        cafeRepo.getCafeByOwnerUid(uid) { cafe: Cafe? ->
            _cafeData.value = cafe
        }

    }
}

