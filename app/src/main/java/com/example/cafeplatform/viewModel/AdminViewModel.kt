package com.example.cafeplatform.ui.admin

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cafeplatform.data.model.User
import com.example.cafeplatform.data.repository.AuthRepository

class AdminViewModel : ViewModel() {

    private val authRepo = AuthRepository()
    private val _users = MutableLiveData<List<User>>()
    val users: LiveData<List<User>> = _users

    fun fetchAllUsers() {
        authRepo.getAllUsers { list ->
            _users.value = list
        }
    }
}