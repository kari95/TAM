package cz.vutbr.fit.meetmeal.adapter

import android.view.*
import androidx.databinding.*
import androidx.recyclerview.widget.*
import cz.vutbr.fit.meetmeal.databinding.*
import cz.vutbr.fit.meetmeal.model.*

class GroupAdapter(val listener: (Group) -> Unit): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

  var checkedGroups: MutableSet<String>? = mutableSetOf()
    set(value) {
      val result = DiffUtil.calculateDiff(DiffCallbackCheckedGroup(
        field ?: emptySet(),
        value ?: emptySet(),
        groups
      ))
      field = value
      notifyDataSetChanged()
      //result.dispatchUpdatesTo(this)
    }

  var groups: List<Group> = emptyList()
    set(value) {
      val result = DiffUtil.calculateDiff(DiffCallbackGroup(field, value))
      field = value

      result.dispatchUpdatesTo(this)
    }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
    val inflater = LayoutInflater.from(parent.context)
    return GroupViewHolder(ItemGroupBinding.inflate(inflater, parent, false))
  }

  override fun getItemCount() = groups.size

  override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
    val task = groups[position]
    when (holder) {
      is GroupViewHolder -> holder.bind(checkedGroups, task, listener)
    }
  }
}

class GroupViewHolder(val binding: ViewDataBinding): RecyclerView.ViewHolder(binding.root) {

  fun bind(checkedGroups: MutableSet<String>?, group: Group,
    listener: (Group) -> Unit) = with(
    binding.root) {
    if (binding is ItemGroupBinding) {
      binding.group = group
      binding.isPickedCheckBox.setOnCheckedChangeListener(null)
      if (checkedGroups != null) {
          binding.isPickedCheckBox.isChecked = checkedGroups.contains(group.id)
      }
      binding.groupContainer.setOnClickListener {
        listener(group)
      }
      binding.isPickedCheckBox.setOnCheckedChangeListener { _, _ ->
        listener(group)
      }
    }
  }
}

private class DiffCallbackGroup(val oldGroups: List<Group>, val newGroups: List<Group>)
  : DiffUtil.Callback() {

  override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
    val oldItem = oldGroups[oldItemPosition]
    val newItem = newGroups[newItemPosition]

    return oldItem == newItem
  }

  override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
    // Not necessary compare content.
    return true
  }

  override fun getOldListSize() = oldGroups.size

  override fun getNewListSize() = newGroups.size
}

private class DiffCallbackCheckedGroup(
  val oldCheckedGroups: Set<String>,
  val newCheckedGroups: Set<String>,
  val groups: List<Group>

): DiffUtil.Callback() {

  override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
    return true
  }

  override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
    val group = groups[oldItemPosition]
    return oldCheckedGroups.contains(group.id) != newCheckedGroups.contains(group.id)
  }

  override fun getOldListSize() = groups.size

  override fun getNewListSize() = groups.size
}