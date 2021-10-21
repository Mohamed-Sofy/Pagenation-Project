package com.sofy.androidtask.ViewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.sofy.androidtask.Model.post
import com.sofy.androidtask.Room.Post_Database

public class DB_ViewModel(app :Application ) : AndroidViewModel(app){

    lateinit var posts_list:MutableLiveData<List<post>>

    init {
        posts_list = MutableLiveData()
    }

    public fun getAllPosts() : MutableLiveData<List<post>>{
        return posts_list
    }

    public fun InsertAllPosts(list : MutableList<post>){
        val dd = Post_Database.getInstance(getApplication())?.postDao()
        dd?.insertAllPosts(list)
    }

    //val postDao = Post_Database.getInstance((getApplication()))?.postDao()
    //val list = postDao?.getAllPosts()
    //posts_list.postValue(list)
}