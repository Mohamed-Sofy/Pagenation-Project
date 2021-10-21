package com.sofy.androidtask

import android.content.Context
import android.graphics.Color
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.animation.AnimationUtils
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.google.android.material.snackbar.Snackbar
import com.kaopiz.kprogresshud.KProgressHUD
import com.sofy.androidtask.Adapter.PostAdapter
import com.sofy.androidtask.Model.post
import com.sofy.androidtask.Room.PostDao
import com.sofy.androidtask.Room.Post_Database
import com.sofy.androidtask.Room.Posts
import com.sofy.androidtask.ViewModel.MainViewModel
import kotlin.concurrent.thread

class MainActivity() : AppCompatActivity() {

    var mainviewmodel : MainViewModel? = null


    lateinit var postAdapter: PostAdapter
    lateinit var recycler :RecyclerView
    lateinit var main_page : RelativeLayout
    lateinit var layoutmanager : RecyclerView.LayoutManager

    var pageId = 1
    lateinit var List :MutableList<post>
    var total :Int =0
    var loading :Boolean = false
    var pre :Int= 0
    var visableItemCount : Int = 0
    var PastVisableItemCount : Int =0

    lateinit var dialog : KProgressHUD



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mainviewmodel = ViewModelProvider(this)[MainViewModel::class.java]

        dialog =   KProgressHUD.create(this)
            .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
        dialog.setBackgroundColor(Color.parseColor("#00ff96"))


        recycler = findViewById(R.id.recycler)
        List = mutableListOf()
        layoutmanager = LinearLayoutManager(this)
        recycler.layoutManager = LinearLayoutManager(this@MainActivity)




        if(InternerConnection()){
            dialog.show()
            LoadPostsByPage()
        }else{
            Get_Data_ROOM()
        }

    }

    private fun Insert_ROOM(list :ArrayList<post>){

        thread {
            val post_list :ArrayList<Posts> = arrayListOf()
            val db = Room.databaseBuilder(
                applicationContext,
                Post_Database::class.java, "database-name"
            ).build()
            for( i in list ){
                post_list.add(Posts(i.id,i.userId,i.title,i.body))
            }
            db.postDao().deleteAllPosts()
            db.postDao().insertAllPosts(post_list)
        }

    }

    private fun Get_Data_ROOM(){

        thread {
            val post_list :ArrayList<post> = arrayListOf()
            val db = Room.databaseBuilder(
                applicationContext,
                Post_Database::class.java, "database-name"
            ).build()
            val data :List<Posts> = db.postDao().getAllPosts()
            Log.e("testg" , data[0].body)

            for (i in data){
                post_list.add(post(i.userId,i.id,i.title,i.body))
            }
            postAdapter = PostAdapter(post_list)
            recycler.adapter = postAdapter
        }

    }

    private fun LoadPostsByPage(){
        mainviewmodel!!.getPostsList(pageId).observe(this){ post ->
            if(post != null){
                loading = true
                setUpAdapter(post)
            }

        }
    }


 private fun setUpAdapter(data: MutableList<post>) {
        if(List.size == 0){

            List = data
            postAdapter = PostAdapter(List as ArrayList<post>)
            recycler.adapter = postAdapter
            val context = recycler.getContext()
            val controller = AnimationUtils.loadLayoutAnimation(context, R.anim.item_animation)
            recycler.setLayoutAnimation(controller)
            recycler.scheduleLayoutAnimation()
            dialog.dismiss()

            // Room DB
            Insert_ROOM(data as ArrayList<post>)
        }else{

            List.addAll(data)
            postAdapter.notifyDataSetChanged()
        }
        recycler.addOnScrollListener(object : RecyclerView.OnScrollListener(){

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {

                if(dy >0){
                    visableItemCount = layoutmanager.childCount
                    total = layoutmanager.itemCount
                    PastVisableItemCount = (recycler.layoutManager as LinearLayoutManager)
                        .findLastVisibleItemPosition()
                    if(loading){
                        if( visableItemCount + PastVisableItemCount >= total  ){
                            loading = false
                            pageId++
                            LoadPostsByPage()
                        }
                    }
                }

            }

        })
    }

    fun InternerConnection(): Boolean {

        main_page = findViewById(R.id.main_page)
        val conn = this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = conn.activeNetworkInfo
        if(network != null && network.isConnected){
            //  internet.visibility = View.GONE
            return true
        }else{
            //  internet.visibility = View.VISIBLE
            Snackbar.make(main_page, "No Internet Connection", Snackbar.LENGTH_LONG)
                .setAction("Action", null)
                .setActionTextColor(Color.RED).show()
            return false
        }
    }
}