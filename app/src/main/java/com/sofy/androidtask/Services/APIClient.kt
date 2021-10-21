package com.sofy.androidtask.Services

import androidx.lifecycle.MutableLiveData
import com.sofy.androidtask.Model.PostModel
import com.sofy.androidtask.Model.post
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface APIClient {

    @GET("posts")
    fun Get_Posts(
        @Query("_page") _page : Int,
        @Query("_limit") _limit:Int
    ) : Call<ArrayList<post>>
}