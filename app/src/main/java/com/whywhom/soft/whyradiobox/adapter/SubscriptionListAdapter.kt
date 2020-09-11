package com.whywhom.soft.whyradiobox.adapter

import android.content.Context
import android.content.Intent
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
import com.whywhom.soft.whyradiobox.services.MediaDownloadService
import com.whywhom.soft.whyradiobox.utils.Constants
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
            when(itemData.controlType) {
                CONTROL_TYPE_UNDOWNLOAD->{
                    println("UNDOWNLOAD")
                    beginDownload(itemData)
                }
                Constants.CONTROL_TYPE_DOWNLOAD ->println("DOWNLOAD")
                Constants.CONTROL_TYPE_DOWNLOADING ->println("DOWNLOADING")
                Constants.CONTROL_TYPE_DOWNLOADED ->println("DOWNLOADED")
                Constants.CONTROL_TYPE_PLAYING ->println("PLAYING")
                Constants.CONTROL_TYPE_PAUSE ->println("PAUSE")
            }
        })
    }

    private fun beginDownload(itemData: FeedItem) {
        val intent = Intent(mContext, MediaDownloadService::class.java)
        MediaDownloadService.setDownloadData(itemData)
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            mContext.startForegroundService(intent)
        } else{
            mContext.startService(intent)
        }

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