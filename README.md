# ShapeMorphView

[![Maven Central](https://img.shields.io/maven-central/v/io.github.dertefter/shapemorphview.svg?label=Maven%20Central&style=for-the-badge)](https://central.sonatype.com/artifact/io.github.dertefter/shapemorphview)
[![License: MIT](https://img.shields.io/badge/License-MIT-blue.svg?style=for-the-badge)](LICENSE)
[![GitHub stars](https://img.shields.io/github/stars/dertefter/ShapeMorphView?style=for-the-badge)](https://github.com/dertefter/ShapeMorphView/stargazers)

![banner.png](art/banner.png)

**ShapeMorphView** — ShapeMorphView is a simple library for smooth morphing between different Material 3 Expressive Shapes.

---

## Features

- Smooth morphing between different shapes
- Image change with morph animation
- Customizable animation duration

## Sample App

The project includes a [simple app](app/src/main/java/com/dertefter/shapemorphviewsample). You might find it useful.

## Installation

Add the dependency to your `build.gradle.kts`:

```kotlin
dependencies {
    implementation("io.github.dertefter:shapemorphview:0.0.3")
}
```

## Usage

Add ShapeMorphView to your layout:

```xml
<com.dertefter.shapemorphview.ShapeMorphView
    android:id="@+id/smv"
    app:animationDuration="500"
    app:bgColor="?attr/colorPrimaryContainer"
    app:imageResource="@drawable/your_drawable"
    app:shape="SLANTED_SQUARE" />
```
- `animationDuration` - duration of the shape morphing animation in milliseconds
- `bgColor` - background fill color of the shape
- `imageResource` - allows setting an image
- `shape` - allows setting the shape

## Shapes

![shapes](art/gif2.gif)

The library supports 35 Material 3 Expressive shapes:

`CIRCLE`, `SQUARE`, `SLANTED_SQUARE`, `ARCH`, `FAN`, `ARROW`, `SEMI_CIRCLE`, `OVAL`, `PILL`, `TRIANGLE`, `DIAMOND`, `CLAM_SHELL`, `PENTAGON`, `GEM`, `SUNNY`, `VERY_SUNNY`, `COOKIE_4`, `COOKIE_6`, `COOKIE_7`, `COOKIE_9`, `COOKIE_12`, `GHOSTISH`, `CLOVER_4`, `CLOVER_8`, `BURST`, `SOFT_BURST`, `BOOM`, `SOFT_BOOM`, `FLOWER`, `PUFFY`, `PUFFY_DIAMOND`, `PIXEL_CIRCLE`, `PIXEL_TRIANGLE`, `BUN`, `HEART`

## Morphing Shapes

You can change the shape using `morphToShape`.

To set a specific shape, use the following:
```kotlin
shapeMorphView.morphToShape(
    newShape = Shape.ARCH,
    animate = true // with or without animation, defaults to true
)
```

The shape can be selected from `com.dertefter.shapemorphview.Shape`

To set a random shape, use the following:
```kotlin
shapeMorphView.morphToShape(
    newShape = shapeMorphView.getRandomShape(), 
    animate = true
)
```

## Morphing Animation Speed

You can set the animation speed programmatically:
```kotlin
shapeMorphView.animationDuration = 500
```

## Images

![gif1.gif](art/gif1.gif)

You can change the image and morph to a shape like this:
```kotlin
shapeMorphView.setDrawableResId(
    resId = R.drawable.your_drawable,
    newShape = shapeMorphView.getRandomShape(),
    animate = true
)
```

## License

This library is distributed under the MIT license.  
See [LICENSE](LICENSE) for details.