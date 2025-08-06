package com.practium.exifeditor

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class ExifDataEditAdapder(
    private val dataList: MutableList<ExifData>
) : RecyclerView.Adapter<ExifDataViewEditHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExifDataViewEditHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.exif_data_edit_item, parent, false)
            return ExifDataViewEditHolder(view)
        }

        override fun onBindViewHolder(holder: ExifDataViewEditHolder, position: Int) {
            holder.bind(dataList[position])
        }

        override fun getItemCount(): Int {
            return dataList.size
        }

}