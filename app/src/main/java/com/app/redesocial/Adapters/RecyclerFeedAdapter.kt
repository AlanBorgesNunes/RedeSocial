package com.app.redesocial.Adapters

import android.media.Image
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.redesocial.PostData
import com.app.redesocial.R

class RecyclerFeedAdapter(private val userList: List<PostData>) :
    RecyclerView.Adapter<RecyclerFeedAdapter.ViewHolder>() {

    inner class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val itemTitle = ItemView.findViewById<TextView>(R.id.title_item)
        val itemDescription = ItemView.findViewById<TextView>(R.id.description_item)
        val itemImage = ItemView.findViewById<ImageView>(R.id.image_item)
        val ItemMenu = ItemView.findViewById<ImageView>(R.id.menu_post_feed)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v =
            LayoutInflater.from(parent.context).inflate(R.layout.item_profile_post_feed, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currenteItem = userList[position]

        holder.itemTitle.text = currenteItem.title
        holder.itemDescription.text = currenteItem.descricao

    }

    override fun getItemCount(): Int {
        return userList.size
    }

}