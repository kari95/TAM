package ml.kari.justdoit.adapter

import android.databinding.*
import android.support.v7.util.*
import android.support.v7.widget.*
import android.view.*
import cz.vutbr.fit.meetmeal.databinding.*
import cz.vutbr.fit.meetmeal.model.*
import cz.vutbr.fit.meetmeal.viewmodel.*

class MealAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>() {

  var meals: List<Meal> = emptyList()
    set(value) {
      val result = DiffUtil.calculateDiff(DiffCallback(field, value))
      field = value

      result.dispatchUpdatesTo(this)
    }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
    val inflater = LayoutInflater.from(parent.context)
    return when (viewType) {
      MainViewModel.MealType.NOW.value ->
        NowMealViewHolder(ItemNowMealBinding.inflate(inflater, parent, false))
      else ->
        PlannedMealViewHolder(ItemPlannedMealBinding.inflate(inflater, parent, false))
    }
  }

  override fun getItemCount() = meals.size

  override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
    val task = meals[position]
    when (holder) {
      is NowMealViewHolder -> holder.bind(task)
      is PlannedMealViewHolder -> holder.bind(task)
    }

  }

  override fun getItemViewType(position: Int): Int = when (meals[position]) {
    is NowMeal -> MainViewModel.MealType.NOW.value
    else -> MainViewModel.MealType.PLANNED.value
  }
}

class NowMealViewHolder(val binding: ViewDataBinding): RecyclerView.ViewHolder(binding.root) {

  fun bind(meal: Meal) {
    if (binding is ItemNowMealBinding && meal is NowMeal)
      binding.meal = meal
  }
}

class PlannedMealViewHolder(val binding: ViewDataBinding): RecyclerView.ViewHolder(binding.root) {

  fun bind(meal: Meal) {
    if (binding is ItemPlannedMealBinding && meal is PlannedMeal)
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
