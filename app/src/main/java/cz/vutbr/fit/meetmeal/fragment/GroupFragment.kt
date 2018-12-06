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
import kotlinx.android.synthetic.main.activity_main.*

class GroupFragment: Fragment(), MenuItem.OnMenuItemClickListener {

  private lateinit var binding: FragmentGroupBinding
  private lateinit var viewModel: GroupViewModel

  companion object {
    fun newInstance() = GroupFragment()
  }

  private val adapter = GroupAdapter { group -> viewModel.onGroupClick(group) }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?): View? {

    binding = DataBindingUtil.inflate(inflater, R.layout.fragment_group, container, false)
    setHasOptionsMenu(true)
    return binding.root
  }

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)
    viewModel = ViewModelProviders.of(this).get(GroupViewModel::class.java)
    binding.viewModel = viewModel

    setupView()
    setupListeners()
    viewModel.sharedPreferences = context?.getSharedPreferences(GroupViewModel.GROUP_TAG, 0)
  }

  private fun setupView() {
    val layoutManager = LinearLayoutManager(activity)
    val dividerItemDecoration = DividerItemDecoration(activity,
            layoutManager.orientation)

    binding.groupList.adapter = adapter
    binding.groupList.layoutManager = layoutManager
    binding.groupList.addItemDecoration(dividerItemDecoration)
  }

  override fun onCreateOptionsMenu(menu: Menu, menuInflater: MenuInflater) {
    // Inflate the menu; this adds items to the action bar if it is present.
      menuInflater.inflate(R.menu.group_actiions_menu, menu)
      menu.getItem(0).setOnMenuItemClickListener(this)
  }

  override fun onMenuItemClick(menuItem: MenuItem): Boolean {
    when (menuItem.itemId) {
      R.id.action_groups -> {
        NavHostFragment.findNavController(this).navigate(R.id.nav_groups)
        return true
      }
      R.id.action_done -> {
        viewModel.onSaveClick(adapter.checkedGroups)
        NavHostFragment.findNavController(this).navigateUp()
        return true
      }
    }
    return false
  }

  private fun setupListeners() {
    viewModel.groups.observe(this, Observer { groups ->
      if (groups == null)
        return@Observer
      adapter.groups = groups
    })
    viewModel.checkedGroups.addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {
      override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
        activity?.runOnUiThread {
          adapter.checkedGroups = viewModel.checkedGroups.get()
        }
      }
    })
  }
}
