package com.sofy.androidtask

import android.content.Context
import android.graphics.Color
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.RelativeLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.kaopiz.kprogresshud.KProgressHUD
import com.sofy.androidtask.Adapter.PostAdapter
import com.sofy.androidtask.Model.post
import com.sofy.androidtask.Room.PostDao
import com.sofy.androidtask.Room.Post_Database
import com.sofy.androidtask.ViewModel.DB_ViewModel
import com.sofy.androidtask.ViewModel.MainViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity() : AppCompatActivity() {

    var mainviewmodel : MainViewModel? = null
    lateinit var dbViewModel:DB_ViewModel
    ///


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
    private lateinit var postDao: PostDao
    lateinit var db :Post_Database


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mainviewmodel = ViewModelProvider(this)[MainViewModel::class.java]
        dbViewModel = ViewModelProviders.of(this).get(DB_ViewModel::class.java)

        dialog =   KProgressHUD.create(this)
            .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
        dialog.setBackgroundColor(Color.parseColor("#00ff96"))


        recycler = findViewById(R.id.recycler)
        List = mutableListOf()
        layoutmanager = LinearLayoutManager(this)
        recycler.layoutManager = LinearLayoutManager(this@MainActivity)

        //Create_Room_DB()


        dialog.show()

        if(InternerConnection()){
            LoadPostsByPage()
        }else{
          //  Get_Posts_From_DB()
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

    private fun Get_Posts_From_DB(){
        dbViewModel.getAllPosts().observe(this , Observer { posts->
            postAdapter = PostAdapter(posts as ArrayList<post>)
            recycler.adapter = postAdapter
        })
    }

    private fun Set_Posts_To_DB(list :MutableList<post>){
            //ViewModelProvider(this)[DB_ViewModel::class.java]
        dbViewModel.InsertAllPosts(list)
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
            //Set_Posts_To_DB(data)
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

   /* private fun Create_Room_DB(){
         db = Room.databaseBuilder(
            this, Post_Database, "post_database"
        ).build()
        postDao = db.postDao()

    }*/




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