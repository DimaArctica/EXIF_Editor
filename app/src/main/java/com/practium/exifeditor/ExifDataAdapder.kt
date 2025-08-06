package com.practium.exifeditor

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class ExifDataAdapder(
    private val dataList: MutableList<ExifData>
) : RecyclerView.Adapter<ExifDataViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExifDataViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.exif_data_item, parent, false)
            return ExifDataViewHolder(view)
        }

        override fun onBindViewHolder(holder: ExifDataViewHolder, position: Int) {
            holder.bind(dataList[position])
        }

        override fun getItemCount(): Int {
            return dataList.size
        }

}