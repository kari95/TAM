package cz.vutbr.fit.meetmeal.adapter

import android.databinding.*
import android.support.v7.util.*
import android.support.v7.widget.*
import android.view.*
import cz.vutbr.fit.meetmeal.databinding.*
import cz.vutbr.fit.meetmeal.model.*

class MealAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>() {

  var meals: List<Meal> = emptyList()
    set(value) {
      val result = DiffUtil.calculateDiff(DiffCallback(field, value))
      field = value

      result.dispatchUpdatesTo(this)
    }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
    val inflater = LayoutInflater.from(parent.context)
    return MealViewHolder(ItemMealBinding.inflate(inflater, parent, false))
  }

  override fun getItemCount() = meals.size

  override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
    val task = meals[position]
    when (holder) {
      is MealViewHolder -> holder.bind(task)
    }

  }
}

class MealViewHolder(val binding: ViewDataBinding): RecyclerView.ViewHolder(binding.root) {

  fun bind(meal: Meal) {
    if (binding is ItemMealBinding)
      binding.meal = meal
  }
}

private class DiffCallback(val oldMeals: List<Meal>, val newMeals: List<Meal>)
  : DiffUtil.Callback() {

  override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
    val oldItem = oldMeals[oldItemPosition]
    val newItem = newMeals[newItemPosition]

    return oldItem == newItem
  }

  override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
    // Not necessary compare content.
    return true
  }

  override fun getOldListSize() = oldMeals.size

  override fun getNewListSize() = newMeals.size

}
