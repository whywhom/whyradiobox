package com.whywhom.soft.whyradiobox.adapter

import android.content.Context
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
import com.whywhom.soft.whyradiobox.model.PodcastSearchResult

class PodcastListAdapter(podcastList: ArrayList<PodcastSearchResult>, val context: Context?) :
RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    lateinit var itemListener: ItemClickListenter
    lateinit var items:ArrayList<PodcastSearchResult>
    init{
        this.items = podcastList
    }
    fun setOnItemClick( listener: ItemClickListenter){
        itemListener = listener
    }

    fun submitList(list: ArrayList<PodcastSearchResult>) {
        this.items = list
    }

    // Gets the number of animals in the list
    override fun getItemCount(): Int {
        return items.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.podcast_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(holder is ViewHolder) {
            var item = items.get(position)
            var profilePicURL: String? = item.imageUrl
            val request: RequestCreator =
                Picasso.with(context).load(profilePicURL).placeholder(R.drawable.rss_64)
            request.fit()
                .centerCrop()
                .into(holder.podcastThumbnail)
            var name = item.title
            holder.podcastName.text = name
            holder.podcastAuthor.text = if(item.author.isNullOrEmpty()) "" else item.author
            holder.podcastItem.setOnClickListener(View.OnClickListener {
                itemListener.onItemClicked(position)
            })
            holder.podcastThumbnail.setOnClickListener(View.OnClickListener {
                itemListener.onItemClicked(position)
            })
        }
    }

    class ViewHolder (view: View) : RecyclerView.ViewHolder(view) {
        var podcastThumbnail : ImageView = view.findViewById(R.id.itemImage)
        var podcastName : TextView = view.findViewById(R.id.itemName)
        var podcastAuthor : TextView = view.findViewById(R.id.itemAuthor)
        var podcastItem : LinearLayout = view.findViewById(R.id.itemContent)
    }
    interface ItemClickListenter {
        fun onItemClicked(position:Int)
    }
}