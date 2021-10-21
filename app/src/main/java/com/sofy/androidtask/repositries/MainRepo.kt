package com.sofy.androidtask.repositries

import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.sofy.androidtask.Model.post
import com.sofy.androidtask.Services.APIClient
import com.sofy.androidtask.Services.ServiceGenerator
import retrofit2.Call
import retrofit2.Response
import javax.security.auth.callback.Callback

class MainRepo {

    companion object{
        private const val TAG = "Main Repo"
    }


    fun Gat_Posts(page_Num:Int) :MutableLiveData<MutableList<post>> {

        val data :MutableLiveData<MutableList<post>> = MutableLiveData<MutableList<post>>()

        val client = ServiceGenerator.createService(APIClient::class.java)
        val call = client.Get_Posts(page_Num , 30)
        call.enqueue(object : Callback, retrofit2.Callback<ArrayList<post>> {
            override fun onFailure(call: Call<ArrayList<post>>, t: Throwable) {
            }
            override fun onResponse(call: Call<ArrayList<post>>, response: Response<ArrayList<post>>) {
                if (response.isSuccessful) {
                    if(response.code() == 200){
                        if(!response.body()!!.isEmpty()){
                            data.value = response!!.body()
                        }else{
                            data.value = null
                        }

                    }else{
                        data.value = null
                    }

                }
            }
        })
        return data
    }

}