package com.example.connect4.screens.score.scoreRecyclerView


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.connect4.database.Score
import com.example.connect4.databinding.ScoreItemViewBinding


class ScoreAdapter(val clickListener: ScoreListener) : ListAdapter<Score, ScoreAdapter.ViewHolder>(ScoreDiffCallback()) {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }


    override fun getItemCount(): Int {

        return currentList.size
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, clickListener)

    }




    class ViewHolder private constructor(val binding: ScoreItemViewBinding): RecyclerView.ViewHolder(binding.root){
    // constructor(val binding: ScoreItemViewGridBinding): RecyclerView.ViewHolder(binding.root){
//        val date: TextView = binding.tvDate
//        val winner: TextView = binding.tvWinner
//        val duration: TextView = binding.tvDuration


        fun bind(item: Score, clickListener: ScoreListener) {
//            date.text = item.date
//            winner.text = item.winner
//            duration.text = item.timeLength
//
//            // Ensures that sets or resets any customization
//            when (item.winner) {
//                "blue" -> winner.setTextColor(Color.BLUE)
//                "red" -> winner.setTextColor(Color.RED)
//                else -> winner.setTextColor(Color.BLACK)
//            }

            binding.score = item
            binding.executePendingBindings()
            binding.clickListener = clickListener
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                //val view = layoutInflater.inflate(R.layout.score_item_view, parent, false)
                //val binding = ScoreItemViewGridBinding.inflate(layoutInflater, parent, false)
                val binding = ScoreItemViewBinding.inflate(layoutInflater, parent, false)

                return ViewHolder(binding)
            }
        }

    }

}




class ScoreDiffCallback : DiffUtil.ItemCallback<Score>() {
    override fun areItemsTheSame(oldItem: Score, newItem: Score): Boolean {
        return oldItem.scoreId == newItem.scoreId
    }

    override fun areContentsTheSame(oldItem: Score, newItem: Score): Boolean {
        return oldItem == newItem
    }

}

class ScoreListener(val clickListener: (scoreId: Long) -> Unit ){
    fun onClick(score: Score) = clickListener(score.scoreId)
}