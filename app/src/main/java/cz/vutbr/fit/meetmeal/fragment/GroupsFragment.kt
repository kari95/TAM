package cz.vutbr.fit.meetmeal.fragment

import android.os.*
import android.view.*
import androidx.databinding.*
import androidx.fragment.app.*
import androidx.lifecycle.*
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import cz.vutbr.fit.meetmeal.R
import cz.vutbr.fit.meetmeal.adapter.*
import cz.vutbr.fit.meetmeal.databinding.*
import cz.vutbr.fit.meetmeal.viewmodel.*

class GroupsFragment: Fragment(), MenuItem.OnMenuItemClickListener {

  override fun onMenuItemClick(menuItem: MenuItem): Boolean {
    when (menuItem.itemId) {
      R.id.action_groups -> {
        NavHostFragment.findNavController(this).navigate(R.id.nav_groups)
        return true
      }
    }
    return false
  }

  private lateinit var binding: FragmentGroupsBinding
  private lateinit var viewModel: GroupsViewModel

  companion object {
    fun newInstance() = GroupsFragment()
  }

  private val adapter = GroupAdapter { group -> viewModel.onGroupClick(group) }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?): View? {

    binding = DataBindingUtil.inflate(inflater, R.layout.fragment_groups, container, false)
    return binding.root
  }

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)
    viewModel = ViewModelProviders.of(this).get(GroupsViewModel::class.java)
    binding.viewModel = viewModel
    // TODO: Use the ViewModel

    setupView()
    setupListeners()
  }

  private fun setupView() {
    val layoutManager = LinearLayoutManager(activity)
    val dividerItemDecoration = DividerItemDecoration(activity,
            layoutManager.orientation)

    binding.groupList.adapter = adapter
    binding.groupList.layoutManager = layoutManager
    binding.groupList.addItemDecoration(dividerItemDecoration)
  }

  private fun setupListeners() {

    viewModel.groups.observe(this, Observer { groups ->
      if (groups == null)
        return@Observer
      adapter.groups = groups
    })
  }
}
