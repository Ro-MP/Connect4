package com.example.connect4

import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.*


fun nullRepresentByDecimal(): Long {
    return 110117108108L
}


fun getCurrentDateString(): String{
    val dateFormat = SimpleDateFormat("dd-MM-yyyy")
    dateFormat.timeZone = TimeZone.getTimeZone("America/Mexico_City")

    val date = dateFormat.format(Calendar.getInstance().time)

    return date
}



