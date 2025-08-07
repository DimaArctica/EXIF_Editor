package com.practium.exifeditor

import android.annotation.SuppressLint
import androidx.exifinterface.media.ExifInterface
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
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
    private lateinit var mainImage: ImageView

    val pickImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                // URI выбранного изображения
                Glide.with(this).load(uri).into(mainImage)
                readExifData(uri)
                viewInViewMode()
            }
        }

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

        mainImage = findViewById<ImageView>(R.id.mainImage)
//        val editButton = findViewById<Button>(R.id.editButton)
//        val saveButton = findViewById<Button>(R.id.saveButton)
//        val clearAllButton = findViewById<Button>(R.id.clearAllButton)
        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        mainImage.isClickable = true

        recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        exifDataList = mutableListOf()

//        for(i in 1..20){
//           exifDataList.add(ExifData("name $i", "value $i"))
//        }

        exifDataAdapter = ExifDataAdapder(exifDataList)
        exifDataEditAdapter = ExifDataEditAdapder(exifDataList)
        recyclerView.adapter = exifDataAdapter

//

        mainImage.setOnClickListener {
            //pickImageLauncher.launch("image/*") // MIME-тип для изображений
            openImage()

            //exifDataAdapter.notifyDataSetChanged()
        }

//        editButton.setOnClickListener {
//            viewInEditMode()
//        }
//
//        saveButton.setOnClickListener {
//            viewInViewMode()
//        }
//
//        clearAllButton.setOnClickListener {
//            clearData()
//            exifDataAdapter.notifyDataSetChanged()
//            exifDataEditAdapter.notifyDataSetChanged()
//        }
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.exit -> {
                finish()
                true
            }
            R.id.action_edit_data -> {
                viewInEditMode()
                true
            }
            R.id.open_item -> {
                openImage()
                true
            }
            R.id.action_save -> {
                viewInViewMode()
                true
            }
            R.id.action_clear_all -> {
                clearData()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
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
            val aperture = exif.getAttribute(ExifInterface.TAG_APERTURE_VALUE)
            val lensMake = exif.getAttribute(ExifInterface.TAG_LENS_MAKE)
            val lensModel = exif.getAttribute(ExifInterface.TAG_LENS_MODEL)
            val exposure = exif.getAttribute(ExifInterface.TAG_EXPOSURE_TIME)
            val apertureMax = exif.getAttribute(ExifInterface.TAG_MAX_APERTURE_VALUE)
            val artist = exif.getAttribute(ExifInterface.TAG_ARTIST)
            val serialNumber = exif.getAttribute(ExifInterface.TAG_BODY_SERIAL_NUMBER)

            exifDataList.clear()
            exifDataAdapter.notifyDataSetChanged()

            exifDataList.add(ExifData("Координаты", checkStringForNull(lat) + checkStringForNull(lon)))
            exifDataList.add(ExifData("Дата", checkStringForNull(dateTime)))
            exifDataList.add(ExifData("Дата 2", checkStringForNull(dateTimeDigitized)))
            exifDataList.add(ExifData("Камера", checkStringForNull(make)))
            exifDataList.add(ExifData("Модель", checkStringForNull(model)))
            exifDataList.add(ExifData("Размер изображения", checkStringForNull(width.toString()) + " x " + checkStringForNull(height.toString())))
            exifDataList.add(ExifData("Фокусное расстояние", checkStringForNull(focalLength) + "mm"))
            exifDataList.add(ExifData("ISO", checkStringForNull(iso)))
            exifDataList.add(ExifData("Баланс белого", checkStringForNull(whiteBalance.toString())))
            exifDataList.add(ExifData("Диафрагма", checkStringForNull(aperture)))
            exifDataList.add(ExifData("Диафрагма Max", checkStringForNull(apertureMax)))
            exifDataList.add(ExifData("Объектив", checkStringForNull(lensMake)))
            exifDataList.add(ExifData("Объектив 2", checkStringForNull(lensModel)))
            exifDataList.add(ExifData("Выдержка", formatExposureTime(checkStringForNull(exposure))))
            exifDataList.add(ExifData("Artist", checkStringForNull(artist)))
            exifDataList.add(ExifData("Serial number", checkStringForNull(serialNumber)))
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun openImage() {
        pickImageLauncher.launch("image/*") // MIME-тип для изображений
        exifDataAdapter.notifyDataSetChanged()
        mainImage.isClickable = false
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

    private fun checkStringForNull(str: String?): String {
        return str ?: ""
    }

    private fun formatExposureTime(expTime: String): String {
        if (expTime == "") return expTime
        val time = expTime.toDouble()
        if (time < 1) {
            return ("1/" + (1 / time).toString())
        } else return expTime
    }

}