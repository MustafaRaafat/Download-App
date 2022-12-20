package com.udacity.downloadapp


enum class ButtonState(val la: String) {

    Clicked("clicked"),
    Loading("loading"),
    Completed("completed");

    fun next() = when (this) {
        Clicked -> Loading
        Loading -> Completed
        Completed -> Clicked
    }

//    object Clicked : ButtonState()
//    object Loading : ButtonState()
//    object Completed : ButtonState()

}