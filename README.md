# MusicAlbum

An example app illustrating Android development best practices with Android Jetpack using MVVM.

Introduction
------------
This app is illustrating android architecture components and android coroutines.

Getting Started
---------------
This project uses the Gradle build system. To build this project, use the
`gradlew build` command or use "Import Project" in Android Studio.

There is one Gradle task for testing the project:
* `test` - for running unit tests

Live Data and Coroutines
----------------
Kotlin Coroutines are asynchronous or non-blocking programming is the new reality. Whether we're creating server-side, desktop or mobile applications, it's important that we provide an experience that is not only fluid from the user's perspective, but scalable when needed.

LiveData is lifecycle-aware, meaning it respects the lifecycle of other app components, such as activities, fragments, or services. This awareness ensures LiveData only updates app component observers that are in an active lifecycle state.

Live data along with ViewModel is Activity LifeCycle aware.

Libraries used in the project
------------------------------------
* [Architecture][0] - A collection of libraries that help you design robust, testable, and
  maintainable apps. Start with classes for managing your UI component lifecycle and handling data
  persistence.
  * [AppCompat][1] - Degrade gracefully on older versions of Android.
  * [Android KTX][2] - Write more concise, idiomatic Kotlin code.
  * [Test][3] - An Android testing framework for unit and runtime UI tests.
  * [Lifecycles][4] - Create a UI that automatically responds to lifecycle events.
  * [LiveData][5] - Build data objects that notify views when the underlying database changes.
  * [ViewModel][6] - Store UI-related data that isn't destroyed on app rotations. Easily schedule
     asynchronous tasks for optimal execution.
  * Third Party
    * [Retrofit 2][7] - For Network operations
    * [Glide][8] for image loading

[0]: https://developer.android.com/jetpack/arch/
[1]: https://developer.android.com/topic/libraries/support-library/packages#v7-appcompat
[2]: https://developer.android.com/kotlin/ktx
[3]: https://developer.android.com/training/testing/
[4]: https://developer.android.com/topic/libraries/architecture/lifecycle
[5]: https://developer.android.com/topic/libraries/architecture/livedata
[6]: https://developer.android.com/topic/libraries/architecture/viewmodel
[7]: http://square.github.io/retrofit/
[8]: https://bumptech.github.io/glide/

Further Development
-------------------
ExoPlayer can be embedded to play videos in app.

Android Studio IDE setup
------------------------
For development, the latest version of Android Studio is required. The latest version can be
downloaded from [here](https://developer.android.com/studio/).