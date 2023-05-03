package ai.app.notifair.news.adapter

import ai.app.notifair.R
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView

class NewsChannelsListAdapter(private val sourceArray: Array<String>) :
    RecyclerView.Adapter<NewsChannelsListAdapter.ViewHolder>() {

    // Create a ViewHolder class for your card item
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageIcon:ImageView = itemView.findViewById<ImageView>(R.id.iv_news_channel_icons)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // Inflate your card item layout and return a new ViewHolder object
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.news_channels_list_items, parent, false)

        Log.d("Ankur 1221 : ", "  ::  "+sourceArray.size)
        Log.d("Ankur 1221 0 : ", "  ::  "+sourceArray.get(0))
        Log.d("Ankur 1221 5: ", "  ::  "+sourceArray.get(5))


        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // Bind your data to your views here
        val item:String = sourceArray[position]
        Log.d("Ankur : ", "  :: "+position +" ::::  "+ item)

        when(item){
            "google-news-in" -> holder.imageIcon.setBackgroundResource(R.drawable.ic_googlenews)
            "bbc-news" -> holder.imageIcon.setBackgroundResource(R.drawable.ic_bbcnews)
            "the-hindu" -> holder.imageIcon.setBackgroundResource(R.drawable.ic_thehindu)
            "the-times-of-india" -> holder.imageIcon.setBackgroundResource(R.drawable.ic_timesofindia)
            "buzzfeed" -> holder.imageIcon.setBackgroundResource(R.drawable.ic_buzzfeednews)
            "mashable" -> holder.imageIcon.setBackgroundResource(R.drawable.ic_mashablenews)
            "mtv-news" -> holder.imageIcon.setBackgroundResource(R.drawable.ic_mtvnews)
            "bbc-sport" -> holder.imageIcon.setBackgroundResource(R.drawable.ic_bbcsports)
            "espn-cric-info" -> holder.imageIcon.setBackgroundResource(R.drawable.ic_espncricinfo)
            "talksport" -> holder.imageIcon.setBackgroundResource(R.drawable.ic_talksport)
            "medical-news-today" -> holder.imageIcon.setBackgroundResource(R.drawable.ic_medicalnewstoday)
            "national-geographic" -> holder.imageIcon.setBackgroundResource(R.drawable.ic_nationalgeographic)
            "crypto-coins-news" -> holder.imageIcon.setBackgroundResource(R.drawable.ic_ccnnews)
            "engadget" -> holder.imageIcon.setBackgroundResource(R.drawable.ic_engadget)
            "the-next-web" -> holder.imageIcon.setBackgroundResource(R.drawable.ic_thenextweb)
            "the-verge" -> holder.imageIcon.setBackgroundResource(R.drawable.ic_theverge)
            "techcrunch" -> holder.imageIcon.setBackgroundResource(R.drawable.ic_techcrunch)
            "techradar" -> holder.imageIcon.setBackgroundResource(R.drawable.ic_techradar)
            "ign" -> holder.imageIcon.setBackgroundResource(R.drawable.ic_ignnews)
            "polygon" -> holder.imageIcon.setBackgroundResource(R.drawable.ic_polygonnews)

        }


//        val textView = holder.itemView.findViewById<TextView>(R.id.text_view)
//        textView.text = item
    }

    override fun getItemCount(): Int {
        // Return the number of items in your data set
        return sourceArray.size
    }
}