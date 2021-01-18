package com.limbo.dragdemo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.limbo.drag.DragImageView
import java.util.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val drag_imag_view = findViewById<DragImageView>(R.id.drag_image_view)

        drag_imag_view.setData(ArrayList<Int>().apply {
            add(R.mipmap.img1)
            add(R.mipmap.img2)
            add(R.mipmap.img3)
            add(R.mipmap.img4)
            add(R.mipmap.img5)
        })
    }
}