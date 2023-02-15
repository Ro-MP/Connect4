package com.example.connect4.screens.score.scoreRecyclerView

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.connect4.R
import com.example.connect4.database.Score



class ScoreAdapter : RecyclerView.Adapter<ScoreAdapter.ViewHolder>() {

    var data = listOf<Score>()
        // Let the recyclerView know when data has changed
        set(value) {
            field = value
            notifyDataSetChanged()
        }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }


    override fun getItemCount(): Int {
        return data.size
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]
        holder.bind(item)

    }




    class ViewHolder private constructor(itemView: View): RecyclerView.ViewHolder(itemView){
        val date: TextView = itemView.findViewById(R.id.tv_date)
        val winner: TextView = itemView.findViewById(R.id.tv_winner)
        val duration: TextView = itemView.findViewById(R.id.tv_duration)

        fun bind(item: Score) {
            date.text = item.date
            winner.text = item.winner
            duration.text = item.timeLength

            // Ensures that sets or resets any customization
            when (item.winner) {
                "blue" -> winner.setTextColor(Color.BLUE)
                "red" -> winner.setTextColor(Color.RED)
                else -> winner.setTextColor(Color.BLACK)
            }
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater.inflate(R.layout.score_item_view, parent, false)

                return ViewHolder(view)
            }
        }

    }




}