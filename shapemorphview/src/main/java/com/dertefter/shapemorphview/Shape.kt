package com.dertefter.shapemorphview

import androidx.graphics.shapes.RoundedPolygon
import com.google.android.material.shape.MaterialShapes

enum class Shape
{
    CIRCLE,
    SQUARE,
    SLANTED_SQUARE,
    ARCH,
    FAN,
    ARROW,
    SEMI_CIRCLE,
    OVAL,
    PILL,
    TRIANGLE,
    DIAMOND,
    CLAM_SHELL,
    PENTAGON,
    GEM,
    SUNNY,
    VERY_SUNNY,
    COOKIE_4,
    COOKIE_6,
    COOKIE_7,
    COOKIE_9,
    COOKIE_12,
    GHOSTISH,
    CLOVER_4,
    CLOVER_8,
    BURST,
    SOFT_BURST,
    BOOM,
    SOFT_BOOM,
    FLOWER,
    PUFFY,
    PUFFY_DIAMOND,
    PIXEL_CIRCLE,
    PIXEL_TRIANGLE,
    BUN,
    HEART,
}

fun Shape.toMaterialShapeReflective(): RoundedPolygon {
    val field = MaterialShapes::class.java.getDeclaredField(this.name)
    return field.get(null) as RoundedPolygon
}