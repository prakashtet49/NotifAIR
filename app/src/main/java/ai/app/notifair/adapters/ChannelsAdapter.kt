package ai.app.notifair.adapters

import ai.app.notifair.R
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.maps.SupportMapFragment

class ChannelsAdapter: RecyclerView.Adapter<ChannelsAdapter.ViewHolder>() {

    private val dataList = listOf(
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        ""
    )

    // Create a ViewHolder class for your card item
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // Inflate your card item layout and return a new ViewHolder object
        val view = LayoutInflater.from(parent.context).inflate(R.layout.channels_list_item, parent, false)
//        val mapFragment = supportFragmentManager
//            .findFragmentById(R.id.map) as SupportMapFragment
//        mapFragment.getMapAsync(this)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // Bind your data to your views here
        val item = dataList[position]
//        val textView = holder.itemView.findViewById<TextView>(R.id.text_view)
//        textView.text = item
    }

    override fun getItemCount(): Int {
        // Return the number of items in your data set
        return dataList.size
    }
}