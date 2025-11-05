package com.dertefter.shapemorphviewsample

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.dertefter.shapemorphviewsample.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val ids = listOf(
        R.drawable.img_1, R.drawable.img_2, R.drawable.img_3,
        R.drawable.img_4, R.drawable.img_5
    )

    private var currentImageId: Int? = null

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

        binding.nextShapeButton.setOnClickListener {
            binding.smv.morphToShape()
        }


        binding.nextImageButton.setOnClickListener {
            val nextId = ids.filter { it != currentImageId }.random()
            currentImageId = nextId
            binding.smv.setDrawableResId(
                resId = nextId,
                newShape = if (binding.withShapeMorph.isChecked) {binding.smv.getRandomShape()} else {null},
                animate = binding.animateCheckbox.isChecked
            )
        }

        binding.removeImageButton.setOnClickListener {
            binding.smv.setDrawableResId(null)
        }

        binding.animationDuration.addOnChangeListener { _, value, _ ->
            binding.smv.animationDuration = value.toInt()
        }
    }
}

