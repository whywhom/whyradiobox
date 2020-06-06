package com.whywhom.soft.whyradiobox.adapter

import android.content.Context
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.squareup.picasso.RequestCreator
import com.whywhom.soft.whyradiobox.R
import com.whywhom.soft.whyradiobox.data.source.local.Podcast
import com.whywhom.soft.whyradiobox.model.PodcastSearchResult

class SubscriptionAdapter(val context: Context?, podcastList: ArrayList<Podcast>) :
RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    lateinit var itemListener: ItemClickListenter
    lateinit var items:ArrayList<Podcast>
    init{
        this.items = podcastList
    }
    fun setOnItemClick( listener: ItemClickListenter){
        itemListener = listener
    }

    fun submitList(list: ArrayList<Podcast>) {
        this.items = list
    }

    // Gets the number of animals in the list
    override fun getItemCount(): Int {
        return items.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.gridview, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(holder is ViewHolder) {
            var item = items.get(position)
            var profilePicURL: String? = item.coverurl
            if(TextUtils.isEmpty(profilePicURL)){
                val request: RequestCreator =
                    Picasso.get().load(R.drawable.rss_64).placeholder(R.drawable.rss_64)
                request.fit()
                    .centerCrop()
                    .into(holder.podcastCover)
            } else {
                val request: RequestCreator =
                    Picasso.get().load(profilePicURL).placeholder(R.drawable.rss_64)
                request.fit()
                    .centerCrop()
                    .into(holder.podcastCover)
            }
            var name = item.title
            holder.podcastName.text = name
            holder.podcastCover.setOnClickListener(View.OnClickListener {
                itemListener.onItemClicked(position)
            })
        }
    }

    class ViewHolder (view: View) : RecyclerView.ViewHolder(view) {
        var podcastCover : ImageView = view.findViewById(R.id.subscribe_cover)
        var podcastName : TextView = view.findViewById(R.id.subscribe_title)

    }
    interface ItemClickListenter {
        fun onItemClicked(position:Int)
    }
}