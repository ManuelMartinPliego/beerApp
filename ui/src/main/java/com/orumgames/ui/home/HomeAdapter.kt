package com.orumgames.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.orumgames.domain.models.Beer
import com.orumgames.ui.databinding.ItemViewBeerBinding
import com.orumgames.ui.utils.loadImageUrl

class HomeAdapter : ListAdapter<Beer, HomeAdapter.ViewHolder>(DiffCallback()), Filterable  {

    private var beerList : MutableList<Beer> = mutableListOf()
    private var beerListFilter : MutableList<Beer> = mutableListOf()
    var onClick: (beer: Beer) -> Unit = {}

    inner class ViewHolder(val binding: ItemViewBeerBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(beer: Beer) {
            with(binding) {
                tvNameBeer.text = beer.name
                imgBeer.loadImageUrl(beer.imageUrl)
                cvBeer.setOnClickListener { onClick(beer) }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ItemViewBeerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(beerListFilter[position])
    }

    override fun getItemCount(): Int = beerListFilter.size

    fun setData(beer: List<Beer>) {
        this.beerList = beer.toMutableList()
        this.beerListFilter = beer.toMutableList()
        notifyDataSetChanged()
    }

    override fun getFilter(): Filter = customFilter

    private val customFilter = object : Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val filteredList = mutableListOf<Beer>()
            if (constraint == null || constraint.isEmpty()) {
                filteredList.addAll(beerList)
            } else {
                for (item in beerList) {
                    if (item.name?.lowercase()?.contains(constraint.toString().lowercase()) == true) {
                        filteredList.add(item)
                    }
                }
            }
            val results = FilterResults()
            results.values = filteredList
            return results
        }

        override fun publishResults(constraint: CharSequence?, filterResults: FilterResults?) {
            beerListFilter = filterResults?.values as MutableList<Beer>
            notifyDataSetChanged()
        }
    }
}