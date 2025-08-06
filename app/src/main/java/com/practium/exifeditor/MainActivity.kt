package com.practium.exifeditor

import android.annotation.SuppressLint
import androidx.exifinterface.media.ExifInterface
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var exifDataList: MutableList<ExifData>
    private lateinit var exifDataAdapter: ExifDataAdapder
    private lateinit var exifDataEditAdapter: ExifDataEditAdapder

    @SuppressLint("MissingInflatedId", "NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val mainImage = findViewById<ImageView>(R.id.mainImage)
        val editButton = findViewById<Button>(R.id.editButton)
        val saveButton = findViewById<Button>(R.id.saveButton)
        val clearAllButton = findViewById<Button>(R.id.clearAllButton)

        recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        exifDataList = mutableListOf()

        for(i in 1..20){
           exifDataList.add(ExifData("name $i", "value $i"))
        }

        exifDataAdapter = ExifDataAdapder(exifDataList)
        exifDataEditAdapter = ExifDataEditAdapder(exifDataList)
        recyclerView.adapter = exifDataAdapter

        val pickImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                // URI выбранного изображения
                Glide.with(this).load(uri).into(mainImage)
                readExifData(uri)
                viewInViewMode()
            }
        }

        mainImage.setOnClickListener {
            pickImageLauncher.launch("image/*") // MIME-тип для изображений
            exifDataAdapter.notifyDataSetChanged()
        }

        editButton.setOnClickListener {
            viewInEditMode()
        }

        saveButton.setOnClickListener {
            viewInViewMode()
        }

        clearAllButton.setOnClickListener {
            clearData()
            exifDataAdapter.notifyDataSetChanged()
            exifDataEditAdapter.notifyDataSetChanged()
        }

    }

    @SuppressLint("NotifyDataSetChanged")
    private fun readExifData(uri: Uri) {
        val inputStream = contentResolver.openInputStream(uri)
        inputStream?.let { stream ->
            val exif = ExifInterface(stream)

            val dateTime = exif.getAttribute(ExifInterface.TAG_DATETIME)
            val dateTimeDigitized = exif.getAttribute(ExifInterface.TAG_DATETIME_DIGITIZED)
            val make = exif.getAttribute(ExifInterface.TAG_MAKE)
            val model = exif.getAttribute(ExifInterface.TAG_MODEL)
            val width = exif.getAttributeInt(ExifInterface.TAG_IMAGE_WIDTH, 0)
            val height = exif.getAttributeInt(ExifInterface.TAG_IMAGE_LENGTH, 0)
            val focalLength = exif.getAttribute(ExifInterface.TAG_FOCAL_LENGTH_IN_35MM_FILM)
            val whiteBalance = exif.getAttributeInt(ExifInterface.TAG_WHITE_BALANCE, 0)
            val lat = exif.getAttribute(ExifInterface.TAG_GPS_LATITUDE)
            val lon = exif.getAttribute(ExifInterface.TAG_GPS_LONGITUDE)
            val iso = exif.getAttribute(ExifInterface.TAG_ISO_SPEED_RATINGS)

            exifDataList.clear()
            exifDataAdapter.notifyDataSetChanged()

            exifDataList.add(ExifData("Координаты", "$lat   $lon"))
            exifDataList.add(ExifData("Дата", "$dateTime"))
            exifDataList.add(ExifData("Дата 2", "$dateTimeDigitized"))
            exifDataList.add(ExifData("Камера", "$make"))
            exifDataList.add(ExifData("Модель", "$model"))
            exifDataList.add(ExifData("Размер изображения", "$width x $height"))
            exifDataList.add(ExifData("Фокусное расстояние", "$focalLength mm"))
            exifDataList.add(ExifData("ISO", "$iso"))
            exifDataList.add(ExifData("Баланс белого", "$whiteBalance"))

        }
    }

    private fun clearData() {
        val tempList: MutableList<ExifData> = mutableListOf()
        for (item in exifDataList) {
            tempList.add(ExifData(item.dataName, ""))
        }
        exifDataList.clear()
        exifDataList.addAll(tempList)
    }

    private fun viewInEditMode() {
        recyclerView.adapter = exifDataEditAdapter
    }

    private fun viewInViewMode() {
        recyclerView.adapter = exifDataAdapter
    }

}