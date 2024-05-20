package com.gabcode.compassabout.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gabcode.compassabout.databinding.ItemWordOccurrencesBinding

class WordOccurrencesAdapter(
    private val dataset: List<WordOccurrenceItem>
) : RecyclerView.Adapter<WordOccurrencesAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemWordOccurrencesBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = dataset.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(dataset[position])
    }

    class ViewHolder(private val binding: ItemWordOccurrencesBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: WordOccurrenceItem) {
            with(binding) {
                itemWordText.text = item.word
                itemWordQuantity.text = item.occurrences.toString()
            }
        }
    }
}
