package cz.vutbr.fit.meetmeal.fragment

import android.os.*
import android.view.*
import androidx.databinding.*
import androidx.fragment.app.*
import androidx.lifecycle.*
import androidx.navigation.*
import androidx.navigation.fragment.*
import cz.vutbr.fit.meetmeal.R
import cz.vutbr.fit.meetmeal.databinding.*
import cz.vutbr.fit.meetmeal.viewmodel.*

class UserDetailFragment: Fragment(), MenuItem.OnMenuItemClickListener {

  private lateinit var binding: FragmentUserDetailBinding
  private lateinit var viewModel: UserDetailViewModel

  companion object {
    fun newInstance() = UserDetailFragment()
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?): View? {
    binding = DataBindingUtil.inflate(inflater, R.layout.fragment_user_detail, container, false)
    binding.layoutNotLoggedIn.loginButton.setOnClickListener(
      Navigation.createNavigateOnClickListener(R.id.action_sign_in)
    )
    binding.layoutNotLoggedIn.registrationButton.setOnClickListener(
      Navigation.createNavigateOnClickListener(R.id.action_registration)
    )
    setHasOptionsMenu(true)
    return binding.root
  }

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)
    viewModel = ViewModelProviders.of(this).get(
      UserDetailViewModel::class.java)
    binding.viewModel = viewModel
    viewModel.onScreenShowed()
  }

  override fun onCreateOptionsMenu(menu: Menu, menuInflater: MenuInflater) {
    // Inflate the menu; this adds items to the action bar if it is present.
    if (viewModel.firebaseUser.get() != null) {
      menuInflater.inflate(R.menu.account_actions_menu, menu)
      menu.getItem(0).setOnMenuItemClickListener(this)
    }
  }

  override fun onMenuItemClick(menuItem: MenuItem): Boolean {
    when (menuItem.itemId) {
      R.id.action_logout -> {
        viewModel.onLogOut()
        return true
      }
    }
    return false
  }
}
