package com.example.newtest

import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_results.*

class Results : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_results)

        Picasso
            .get()
            .load("https://www.clipartwiki.com/clipimg/detail/30-302225_smiley-faces-and-sad-faces-neutral-clipart.png")
            .into(graph)

    }
}

