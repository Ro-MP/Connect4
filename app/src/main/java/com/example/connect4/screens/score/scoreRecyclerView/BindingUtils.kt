package com.example.connect4.screens.score.scoreRecyclerView

import android.graphics.Color
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.example.connect4.database.Score


//    @BindingAdapter("timeLength")
//    fun TextView.setDateFormatted(item: Score) {
//        //text = ""
//    }

@BindingAdapter("getWinnerAdapter")
fun TextView.setTextWinnerColor(item: Score){
    text = item.winner
    when (item.winner) {
        "blue" -> setTextColor(Color.BLUE)
        "red" -> setTextColor(Color.RED)
        else -> setTextColor(Color.BLACK)
    }
}



/*
    Ejemplo de adapter con imagenes
 */
//    @BindingAdapter("sleepImage")
//    fun ImageView.setSleepImage(item: SleepNight) {
//        setImageResource(when (item.sleepQuality) {
//            0 -> R.drawable.ic_sleep_0
//            1 -> R.drawable.ic_sleep_1
//            2 -> R.drawable.ic_sleep_2
//            3 -> R.drawable.ic_sleep_3
//            4 -> R.drawable.ic_sleep_4
//            5 -> R.drawable.ic_sleep_5
//            else -> R.drawable.ic_sleep_active
//        })
//    }