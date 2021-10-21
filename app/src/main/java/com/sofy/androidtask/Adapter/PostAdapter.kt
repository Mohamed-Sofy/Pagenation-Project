package com.sofy.androidtask.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sofy.androidtask.Model.post
import com.sofy.androidtask.R

class PostAdapter  ( list: ArrayList<post> ) : RecyclerView.Adapter<PostAdapter.post_holder>() {


    private var List = list
    lateinit var context: Context
    lateinit var view: View

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostAdapter.post_holder {
        view = LayoutInflater.from(parent.context).inflate(
            R.layout.post_item, parent, false
        )
        context = parent.context
        return PostAdapter.post_holder(view)
    }

    override fun onBindViewHolder(holder: PostAdapter.post_holder, position: Int) {

        holder.title.text = List[position].title
        holder.body.text = List[position].body
    }

    override fun getItemCount(): Int {
        return List.count()
    }

    class post_holder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var title : TextView = itemView.findViewById(R.id.title)
        var body : TextView = itemView.findViewById(R.id.body)
    }
}