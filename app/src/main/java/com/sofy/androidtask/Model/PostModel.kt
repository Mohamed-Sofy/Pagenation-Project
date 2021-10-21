package com.sofy.androidtask.Model

data class PostModel(var data :ArrayList<post>)

data class post(val userId :Int ,val id:Int , val title:String , val body:String )