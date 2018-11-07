package cz.vutbr.fit.meetmeal.adapter

import android.view.*
import android.widget.CompoundButton
import androidx.databinding.*
import androidx.recyclerview.widget.*
import cz.vutbr.fit.meetmeal.databinding.*
import cz.vutbr.fit.meetmeal.model.*

class GroupAdapter(val listener: (Group) -> Unit): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var groups: List<Group> = emptyList()
        set(value) {
            val result = DiffUtil.calculateDiff(DiffCallbackgroup(field, value))
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
            is GroupViewHolder -> holder.bind(task, listener)
        }
    }
}

class GroupViewHolder(val binding: ViewDataBinding): RecyclerView.ViewHolder(binding.root) {

    fun bind(group: Group, listener: (Group) -> Unit) = with(binding.root) {
        if (binding is ItemGroupBinding) {
            binding.group = group
            binding.isPickedCheckBox.setOnCheckedChangeListener { buttonView, isChecked -> listener(group) }
        }
    }
}

private class DiffCallbackgroup(val oldGroups: List<Group>, val newGroups: List<Group>)
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
