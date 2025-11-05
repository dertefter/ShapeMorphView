# ShapeMorphView
[![lang](https://img.shields.io/badge/lang-English-blue?style=for-the-badge)](README.md)
[![lang](https://img.shields.io/badge/lang-Русский-red?style=for-the-badge)](README_RU.md)

[![Maven Central](https://img.shields.io/maven-central/v/io.github.dertefter/shapemorphview.svg?label=Maven%20Central&style=for-the-badge)](https://central.sonatype.com/artifact/io.github.dertefter/shapemorphview)
[![License: MIT](https://img.shields.io/badge/License-MIT-blue.svg?style=for-the-badge)](LICENSE)
[![GitHub stars](https://img.shields.io/github/stars/dertefter/ShapeMorphView?style=for-the-badge)](https://github.com/dertefter/ShapeMorphView/stargazers)

![banner.png](art/banner.png)

**ShapeMorphView** — это простая библиотека для плавного морфинга между разными формами (Shape) из Material 3 Expressive.

---

## Возможности

- Плавный морфинг между разными формами
- Изменение изображения с анимацией
- Настраиваемая длительность анимации


## Sample App

В проекте есть [простой пример](app/src/main/java/com/dertefter/shapemorphviewsample) использования библиотеки. Возможно он будет вам полезен.

## Подключение

Добавьте зависимость в `build.gradle.kts`:

```kotlin
dependencies {
    implementation("io.github.dertefter:shapemorphview:0.0.4")
}
```

## Использовние

Добавье ShapeMorphView в лайаут:

```xml
<com.dertefter.shapemorphview.ShapeMorphView
    android:id="@+id/smv"
    app:animationDuration="500"
    app:bgColor="?attr/colorPrimaryContainer"
    app:imageResource="@drawable/your_drawable"
    app:shape="SLANTED_SQUARE" />
```
- `animationDuration` - длительность анимации морфинга между формами в миллисекундах
- `bgColor` - цвет заливки фона фигуры
- `imageResource` - позволяет установить изображение
- `shape` - позволяет установить форму

## Формы

![shapes](art/gif2.gif)

Библиотка поддерживает 35 форм из Material 3 Expressive:

`CIRCLE`, `SQUARE`, `SLANTED_SQUARE`, `ARCH`, `FAN`, `ARROW`, `SEMI_CIRCLE`, `OVAL`, `PILL`, `TRIANGLE`, `DIAMOND`, `CLAM_SHELL`, `PENTAGON`, `GEM`, `SUNNY`, `VERY_SUNNY`, `COOKIE_4`, `COOKIE_6`, `COOKIE_7`, `COOKIE_9`, `COOKIE_12`, `GHOSTISH`, `CLOVER_4`, `CLOVER_8`, `BURST`, `SOFT_BURST`, `BOOM`, `SOFT_BOOM`, `FLOWER`, `PUFFY`, `PUFFY_DIAMOND`, `PIXEL_CIRCLE`, `PIXEL_TRIANGLE`, `BUN`, `HEART`

## Морфинг форм

Сменить форму можно с помощью `morphToShape`. 

Установить конкретную форму можно следующим образом:
```kotlin
shapeMorphView.morphToShape(
    newShape = Shape.ARCH,
    animate = true // c анимацией или без, по умолчанию true
)
```

Форму можно выбрать из `com.dertefter.shapemorphview.Shape`

Установить случайную форму можно следующим образом:
```kotlin
shapeMorphView.morphToShape(
    newShape = shapeMorphView.getRandomShape(), 
    animate = true
)
```

## Скорость анимации морфинга

Вы можете задавать скорость анимации программно:
```kotlin
shapeMorphView.animationDuration = 500
```

## Изображения

![gif1.gif](art/gif1.gif)

Вы можете выполнить смену изображения с морфингом до фигуры таким образом:
```kotlin
shapeMorphView.setDrawableResId(
    resId = R.drawable.your_drawable,
    newShape = shapeMorphView.getRandomShape(),
    animate = true
)
```

## Лицензия

Эта библиотека распространяется под лицензией **MIT**.  
Подробнее см. [LICENSE](LICENSE).