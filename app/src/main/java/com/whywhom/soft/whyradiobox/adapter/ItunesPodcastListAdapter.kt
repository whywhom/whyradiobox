package com.whywhom.soft.whyradiobox.adapter

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import com.squareup.picasso.RequestCreator
import com.whywhom.soft.whyradiobox.R
import com.whywhom.soft.whyradiobox.model.Entry
import com.whywhom.soft.whyradiobox.model.ItunesPodcastSearcher
import java.lang.ref.WeakReference
import java.util.*


/**
 * Created by wuhaoyong on 2020-02-10.
 */
class ItunesPodcastListAdapter(mainActivity: Activity): BaseAdapter() {

    private var mainActivityRef: WeakReference<Activity>? = WeakReference<Activity>(mainActivity)
    private val data: MutableList<Entry> = ArrayList<Entry>()

    fun updateData(newData: List<Entry>) {
        data.clear()
        data.addAll(newData!!)
        notifyDataSetChanged()
    }

    override fun getCount(): Int {
        return data.size
    }

    override fun getItem(position: Int): Entry {
        return data[position]
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
        var convertView = convertView
        val holder: Holder
        if (convertView == null) {
            convertView =
                View.inflate(mainActivityRef!!.get(), R.layout.podcast_grid_item, null)
            holder = Holder()
            holder.imageView = convertView.findViewById(R.id.itemImage)
            convertView.tag = holder
        } else {
            holder = convertView.tag as Holder
        }
        val item: Entry = getItem(position)
        holder.imageView!!.contentDescription = item.title.toString()

        val request: RequestCreator =
            Picasso.get().load(item.imImage.get(item.imImage.size-1).label).placeholder(R.drawable.rss_64)
        request.fit()
            .centerCrop()
            .into(holder.imageView)
        return convertView
    }

    internal class Holder {
        var imageView: ImageView? = null
    }
}