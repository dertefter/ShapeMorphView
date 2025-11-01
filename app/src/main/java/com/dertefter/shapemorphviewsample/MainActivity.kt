package com.dertefter.shapemorphviewsample

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.dertefter.shapemorphviewsample.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        val ids = listOf(
            R.drawable.img_1, R.drawable.img_2, R.drawable.img_3, R.drawable.img_4, R.drawable.img_5
        )

        val idsIcons = listOf(
            R.drawable.ic_1,
            R.drawable.ic_2,
            R.drawable.ic_3,
            R.drawable.ic_4,
            R.drawable.ic_5,
            R.drawable.ic_6,
            R.drawable.ic_7,
            R.drawable.ic_8,
            R.drawable.ic_9,
        )

        binding.nextImageButton.setOnClickListener {
            val nextId = ids.random()
            binding.smv.setDrawableResId(
                resId = nextId, newShape = null, animate = binding.animateCheckbox.isChecked
            )
        }

        binding.nextIconButton.setOnClickListener {
            val nextId = idsIcons.random()
            binding.smv.setDrawableResId(
                resId = nextId, newShape = null, animate = binding.animateCheckbox.isChecked
            )
        }

        binding.animationDuration.addOnChangeListener { slider, value, fromUser ->
            binding.smv.animationDuration = value.toInt()
        }


    }
}
