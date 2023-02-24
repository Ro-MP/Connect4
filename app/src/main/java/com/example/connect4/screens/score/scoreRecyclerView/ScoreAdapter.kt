package com.example.connect4.screens.score.scoreRecyclerView


import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.connect4.R
import com.example.connect4.database.Score
import com.example.connect4.databinding.ScoreItemViewBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val ITEM_VIEW_TYPE_HEADER = 0
private const val ITEM_VIEW_TYPE_ITEM = 1

class ScoreAdapter(val clickListener: ScoreListener) : ListAdapter<DataItem, RecyclerView.ViewHolder>(ScoreDiffCallback()) {

    private val adapterScope = CoroutineScope(Dispatchers.Default)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ITEM_VIEW_TYPE_HEADER -> TextViewHolder.from(parent)
            ITEM_VIEW_TYPE_ITEM -> ViewHolder.from(parent)
            else -> throw ClassCastException("Unknown viewType $viewType")
        }
    }

    fun addHeaderAndSubmitList(list: List<Score>?) {
        adapterScope.launch {
            val items = when (list) {
                null -> listOf(DataItem.Header)
                else -> listOf(DataItem.Header) + list.map { DataItem.ScoreItem(it) }
            }
            withContext(Dispatchers.Main){
                submitList(items)
            }

        }

    }


    override fun getItemCount(): Int {

        return currentList.size
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ViewHolder -> {
                val scoreItem = getItem(position) as DataItem.ScoreItem
                holder.bind(scoreItem.score, clickListener)
            }
        }

    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is DataItem.Header -> ITEM_VIEW_TYPE_HEADER
            is DataItem.ScoreItem -> ITEM_VIEW_TYPE_ITEM
        }
    }


    class TextViewHolder(view: View): RecyclerView.ViewHolder(view) {
        companion object {
            fun from(parent: ViewGroup): TextViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater.inflate(R.layout.header, parent, false)

                return TextViewHolder(view)
            }
        }
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




class ScoreDiffCallback : DiffUtil.ItemCallback<DataItem>() {
    override fun areItemsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
        return oldItem.id == newItem.id
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
        return oldItem == newItem
    }

}

class ScoreListener(val clickListener: (scoreId: Long) -> Unit ){
    fun onClick(score: Score) = clickListener(score.scoreId)
}

sealed class DataItem {
    abstract val id: Long
    data class ScoreItem(val score: Score): DataItem() {
        override val id = score.scoreId
    }

    object Header: DataItem(){
        override val id = Long.MIN_VALUE //Avoid conflict with any ID
    }

}