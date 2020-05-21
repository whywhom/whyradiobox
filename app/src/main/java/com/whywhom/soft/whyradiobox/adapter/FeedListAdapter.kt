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
import com.whywhom.soft.whyradiobox.interfaces.OnPlayListener
import com.whywhom.soft.whyradiobox.rss.RSSItem
import java.text.SimpleDateFormat

/**
 * Created by wuhaoyong on 2020-02-10.
 */
class FeedListAdapter(val mContext: Context, val rssList: ArrayList<RSSItem>) : RecyclerView.Adapter<FeedListAdapter.PodcastViewHolder>() {
    private lateinit var playListerer: OnPlayListener
    private var isPlaying = false
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PodcastViewHolder {
        return PodcastViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.rss_cardview_item,parent,false))
    }

    override fun getItemCount(): Int {
        return rssList.count()
    }

    fun setOnPlayListener(listener:OnPlayListener){
        playListerer = listener
    }
    override fun onBindViewHolder(holder: PodcastViewHolder, position: Int) {
        val itemData = rssList[position]
        holder.titleView.setText(itemData.title)
        val formatter = SimpleDateFormat("yyyy-MM-dd")
        val dateString = formatter.format(itemData.pubDate)
        holder.dateView.text = dateString
        holder.detailView.text = itemData.description
        holder.mediaPlay.setOnClickListener(View.OnClickListener {
            if(isPlaying){
                playListerer.pause(position)
                isPlaying = false
            } else {
                playListerer.play(position)
                isPlaying = true
            }
        })
    }

    inner class PodcastViewHolder(view: View) : RecyclerView.ViewHolder(view){
        var cardView: CardView
        val titleView: TextView
        val dateView: TextView
        val detailView: TextView
        val mediaPlay: ImageView

        init {
            cardView = view.findViewById(R.id.card_view) as CardView
            titleView = view.findViewById(R.id.item_title) as TextView
            dateView = view.findViewById(R.id.item_date)
            detailView = view.findViewById(R.id.item_detail) as TextView
            mediaPlay = view.findViewById(R.id.media_play) as ImageView
        }
    }

}