package com.practium.exifeditor

import androidx.exifinterface.media.ExifInterface
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide

class MainActivity : AppCompatActivity() {

    private lateinit var dateValueTextView: TextView
    private lateinit var coordsValueTextView: TextView
    private lateinit var cameraValueTextView: TextView
    private lateinit var imageSizeValue: TextView

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
        dateValueTextView = findViewById(R.id.dateValueTextView)
        coordsValueTextView = findViewById(R.id.coordsValueTextView)
        cameraValueTextView = findViewById(R.id.cameraValueTextView)
        imageSizeValue = findViewById(R.id.imageSizeValue)

        val pickImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                // URI выбранного изображения
                Glide.with(this).load(uri).into(mainImage)
                readExifData(uri)
            }
        }

        mainImage.setOnClickListener {
            pickImageLauncher.launch("image/*") // MIME-тип для изображений
        }

    }

    fun readExifData(uri: Uri) {
        val inputStream = contentResolver.openInputStream(uri)
        inputStream?.let { stream ->
            val exif = ExifInterface(stream)

            // Примеры метаданных
            val dateTime = exif.getAttribute(ExifInterface.TAG_DATETIME)
            val make = exif.getAttribute(ExifInterface.TAG_MAKE)
            val model = exif.getAttribute(ExifInterface.TAG_MODEL)
            val width = exif.getAttributeInt(ExifInterface.TAG_IMAGE_WIDTH, 0)
            val height = exif.getAttributeInt(ExifInterface.TAG_IMAGE_LENGTH, 0)

            // GPS-координаты (если есть)
            val latLong = FloatArray(2)
            if (exif.getLatLong(latLong)) {
                val latitude = latLong[0]
                val longitude = latLong[1]
                coordsValueTextView.text = "Координаты: $latitude, $longitude"
            }

            dateValueTextView.text = dateTime
            cameraValueTextView.text = "$make  $model"
            imageSizeValue.text = "$width x $height"
        }
    }

}