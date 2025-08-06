package com.practium.exifeditor

import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ExifDataViewEditHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    private val dataName: TextView = itemView.findViewById(R.id.nameOfDataEditView)
    private val dataValue: EditText = itemView.findViewById(R.id.valueOfDataEdit)

    fun bind(data: ExifData){
        dataName.text = data.dataName
        dataValue.setText(data.dataValue)
    }
}