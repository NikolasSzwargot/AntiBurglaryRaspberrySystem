package edu.put.rekin3

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class RecordingsAdapter(private val items: List<MediaItem>, private val clickListener: (MediaItem) -> Unit) : RecyclerView.Adapter<RecordingsAdapter.ViewHolder>() {

    // Provide a reference to the views for each data item
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView = view.findViewById(R.id.dateTextView)
        val photo: ImageView = view.findViewById(R.id.recordingImage)

        fun bind(item: MediaItem, clickListener: (MediaItem) -> Unit) {
            textView.text = item.date
            Picasso.get()
                .load(R.drawable.doda)
                .into(photo)
            itemView.setOnClickListener { clickListener(item) }
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_view_layout, parent, false)
        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        holder.bind(items[position], clickListener)
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = items.size
}