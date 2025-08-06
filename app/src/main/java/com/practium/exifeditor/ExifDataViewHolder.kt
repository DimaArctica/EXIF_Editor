package com.practium.exifeditor

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ExifDataViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    private val dataName: TextView = itemView.findViewById(R.id.nameOfData)
    private val dataValue: TextView = itemView.findViewById(R.id.valueOfData)

    fun bind(data: ExifData){
        dataName.text = data.dataName
        dataValue.text = data.dataValue
    }
}