package com.gabcode.compassabout.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gabcode.compassabout.databinding.ItemTenthCharacterBinding
import java.util.Locale

class TenthCharactersAdapter(
    private val dataset: List<TenthCharacterItem>
) : RecyclerView.Adapter<TenthCharactersAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemTenthCharacterBinding.inflate(
            LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = dataset.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(dataset[position])
    }

    class ViewHolder(private val binding: ItemTenthCharacterBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: TenthCharacterItem) {
            with(binding){
                itemTenthPosition.text = String.format(Locale.getDefault(), "%sth", item.position)
                itemTenthCharacter.text = item.character.toString()
            }
        }
    }
}
