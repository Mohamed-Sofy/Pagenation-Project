package com.sofy.androidtask.ViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sofy.androidtask.Model.post
import com.sofy.androidtask.Room.Post_Database
import com.sofy.androidtask.repositries.MainRepo

class MainViewModel:ViewModel() {
    private val mainRepo:MainRepo

    init {
        mainRepo= MainRepo()
    }

    fun getPostsList(page:Int): MutableLiveData<MutableList<post>>{
        return mainRepo.Gat_Posts(page)
    }




}