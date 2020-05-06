package com.whywhom.soft.whyradiobox.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.whywhom.soft.whyradiobox.R
import com.whywhom.soft.whyradiobox.data.source.local.FeedItem
import com.whywhom.soft.whyradiobox.interfaces.OnItemClickListener
import com.whywhom.soft.whyradiobox.interfaces.OnPlayListener
import com.whywhom.soft.whyradiobox.utils.Constants
import com.whywhom.soft.whyradiobox.utils.Constants.CONTROL_TYPE_DOWNLOAD
import com.whywhom.soft.whyradiobox.utils.Constants.CONTROL_TYPE_DOWNLOADED
import com.whywhom.soft.whyradiobox.utils.Constants.CONTROL_TYPE_DOWNLOADING
import com.whywhom.soft.whyradiobox.utils.Constants.CONTROL_TYPE_PAUSE
import com.whywhom.soft.whyradiobox.utils.Constants.CONTROL_TYPE_PLAYING
import com.whywhom.soft.whyradiobox.utils.Constants.CONTROL_TYPE_UNDOWNLOAD

/**
 * Created by wuhaoyong on 2020-02-10.
 */
class SubscriptionListAdapter(val mContext: Context, val rssList: ArrayList<FeedItem>) : RecyclerView.Adapter<SubscriptionListAdapter.PodcastViewHolder>() {
    private lateinit var itemClickListerer: OnItemClickListener
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PodcastViewHolder {
        return PodcastViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.rss_cardview_item_subscription,parent,false))
    }

    override fun getItemCount(): Int {
        return rssList.count()
    }

    override fun onBindViewHolder(holder: PodcastViewHolder, position: Int) {
        val itemData = rssList[position]
        holder.titleView.setText(itemData.title)
//        val formatter = SimpleDateFormat("yyyy-MM-dd")
//        val dateString = formatter.format(itemData.pubDate)
        holder.dateView.text = itemData.pubData
        holder.itemSize.text = Constants.getFileSize(itemData.length.toLong())
        holder.itemControl.setOnClickListener(View.OnClickListener {
            itemClickListerer.onItemClick(position)
        })
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.itemClickListerer = listener
    }


    inner class PodcastViewHolder(view: View) : RecyclerView.ViewHolder(view){
        var cardView: CardView
        val titleView: TextView
        val dateView: TextView
        val itemSize: TextView
        val itemControl: ImageView
        val itemListener: ImageView
        init {
            cardView = view.findViewById(R.id.card_view) as CardView
            titleView = view.findViewById(R.id.item_title) as TextView
            dateView = view.findViewById(R.id.item_date)
            itemControl = view.findViewById(R.id.item_control) as ImageView
            itemSize = view.findViewById(R.id.item_size) as TextView
            itemListener = view.findViewById(R.id.item_listener) as ImageView
        }
    }

}