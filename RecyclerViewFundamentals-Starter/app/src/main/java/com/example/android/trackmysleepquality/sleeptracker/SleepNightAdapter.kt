package com.example.android.trackmysleepquality.sleeptracker

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android.trackmysleepquality.*
import com.example.android.trackmysleepquality.database.SleepNight
import com.example.android.trackmysleepquality.databinding.ListItemSleepNightBinding

//class SleepNightAdapter : RecyclerView.Adapter<TextItemViewHolder>() {
//class SleepNightAdapter : RecyclerView.Adapter<SleepNightAdapter.ViewHolder>() {

class SleepNightAdapter : ListAdapter<SleepNight, SleepNightAdapter.ViewHolder>(SleepNightDiffCallback()) {

    //You don't need it anymore, because ListAdapter keeps track of the list for you.
    /*var data = listOf<SleepNight>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    //because the ListAdapter implements this method for you
    override fun getItemCount() = data.size
    */


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //val item = data[position]
        /* holder.textView.text = item.sleepQuality.toString()
         if (item.sleepQuality <= 1) {
             holder.textView.setTextColor(Color.RED)
         } else {
             //reset
             holder.textView.setTextColor(Color.BLACK)
         }*/

        /*val res = holder.itemView.context.resources
        holder.sleepLength.text = convertDurationToFormatted(item.startTimeMilli, item.endTimeMilli, res)
        holder.quality.text = convertNumericQualityToString(item.sleepQuality, res)
        holder.qualityImage.setImageResource(when (item.sleepQuality) {
            0 -> R.drawable.ic_sleep_0
            1 -> R.drawable.ic_sleep_1
            2 -> R.drawable.ic_sleep_2
            3 -> R.drawable.ic_sleep_3
            4 -> R.drawable.ic_sleep_4
            5 -> R.drawable.ic_sleep_5
            else -> R.drawable.ic_sleep_active
        })*/

        //select everything except the statement to declare the variable item.
        //Right-click, then select Refactor > Extract > Function.
        //Put the cursor on the word holder of the holder parameter of bind(). Press Alt+Enter (Option+Enter on a Mac) to open the intention menu. Select Convert parameter to receiver to convert this to an extension function that has the following signature:


        //the ListAdapter provides.
        val item = getItem(position)
        holder.bind(item)

    }


    //Change the signature of the ViewHolder class so that the constructor is private. Because from() is now a method that returns a new ViewHolder instance, there's no reason for anyone to call the constructor of ViewHolder anymore
    // class ViewHolder private constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
    class ViewHolder private constructor(val binding: ListItemSleepNightBinding) : RecyclerView.ViewHolder(binding.root) { //Prefix the constructor parameter binding with val to make it a property
        /*val sleepLength: TextView = itemView.findViewById(R.id.sleep_length)
        val quality: TextView = itemView.findViewById(R.id.quality_string)
        val qualityImage: ImageView = itemView.findViewById(R.id.quality_image)*/


       /* DataBinding will cache the lookups, so there is no need to declare these properties.
        val sleepLength: TextView = binding.sleepLength
        val quality: TextView = binding.qualityString
        val qualityImage: ImageView = binding.qualityImage*/

        fun bind(item: SleepNight) {
         /*   val res = itemView.resources
            binding.sleepLength.text = convertDurationToFormatted(item.startTimeMilli, item.endTimeMilli, res)
            binding.qualityString.text = convertNumericQualityToString(item.sleepQuality, res)
            binding.qualityImage.setImageResource(when (item.sleepQuality) {
                0 -> R.drawable.ic_sleep_0
                1 -> R.drawable.ic_sleep_1
                2 -> R.drawable.ic_sleep_2
                3 -> R.drawable.ic_sleep_3
                4 -> R.drawable.ic_sleep_4
                5 -> R.drawable.ic_sleep_5
                else -> R.drawable.ic_sleep_active
            })*/

            //use adapter binding.
            // need tell the binding object about your new SleepNight
            binding.sleep = item

            //This call is an optimization that asks data binding to execute any pending bindings right away. It's always a good idea to call executePendingBindings() when you use binding adapters in a RecyclerView, because it can slightly speed up sizing the views.
            binding.executePendingBindings()

        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                /* val view = layoutInflater
                         .inflate(R.layout.list_item_sleep_night, parent, false)*/
                //use databinding
                val binding = ListItemSleepNightBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }


    class SleepNightDiffCallback : DiffUtil.ItemCallback<SleepNight>() {
        override fun areItemsTheSame(oldItem: SleepNight, newItem: SleepNight): Boolean {
            return oldItem.nightId == newItem.nightId
        }

        override fun areContentsTheSame(oldItem: SleepNight, newItem: SleepNight): Boolean {
            return oldItem == newItem
        }
    }


}