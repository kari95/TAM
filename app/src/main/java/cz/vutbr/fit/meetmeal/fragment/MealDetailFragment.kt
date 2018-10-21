package cz.vutbr.fit.meetmeal.fragment

import android.os.*
import android.support.v4.app.*
import android.view.*
import cz.vutbr.fit.meetmeal.*

class MealDetailFragment: Fragment() {

  companion object {

    fun newInstance(): MealDetailFragment {
      return MealDetailFragment()
    }
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?): View? {

    val view: View = inflater.inflate(R.layout.fragment_meal_detail, container,
    false)

    return view
  }

  override fun onAttachFragment(childFragment: Fragment?) {
    super.onAttachFragment(childFragment)
    val resources = context!!.resources


  }



}