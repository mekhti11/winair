package com.hititcs.dcs.view.settings

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import butterknife.BindView
import com.hititcs.dcs.R
import com.hititcs.dcs.view.BaseFragment
import com.hititcs.dcs.view.Presenter

class SettingsFragment : BaseFragment<SettingsFragment>() {

  @BindView(R.id.tv_manufacturer) lateinit var tvManufacturer: TextView
  @BindView(R.id.tv_model) lateinit var tvModel: TextView

  companion object {
    fun newInstance(): Fragment {
      val args = Bundle()
      val fragment = SettingsFragment()
      fragment.arguments = args
      return fragment
    }
  }

  override fun inject() {
  }

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    val fragmentView = inflater.inflate(R.layout.content_settings, container, false)
    bindView(fragmentView)
    return fragmentView
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    tvManufacturer.text = Build.MANUFACTURER
    tvModel.text = Build.MODEL
  }

  override fun getFragment(): SettingsFragment {
    return this
  }

  override fun getPresenter(): Presenter<*>? {
    return null
  }
}