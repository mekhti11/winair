package com.hititcs.dcs.view.baggagetracking.view.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.OnClick
import com.google.android.material.textfield.TextInputLayout
import com.hititcs.dcs.R
import com.hititcs.dcs.util.FontUtils
import com.hititcs.dcs.util.StringUtils
import com.hititcs.dcs.view.BaseFragment
import com.hititcs.dcs.view.Presenter
import com.hititcs.dcs.view.baggagetracking.domain.model.ScannedTag
import com.hititcs.dcs.view.baggagetracking.domain.model.TrackBaggageLocationDto
import com.hititcs.dcs.view.baggagetracking.view.main.BaggageTrackMainContract.BaggageTrackMainPresenter
import com.hititcs.dcs.view.baggagetracking.view.main.BaggageTrackMainContract.BaggageTrackMainView
import com.hititcs.dcs.widget.AutoCompleteDropDown
import com.hititcs.dcs.widget.GeneralTextWatcher
import javax.inject.Inject

class BaggageTrackMainFragment : BaseFragment<BaggageTrackMainFragment>(),
  BaggageTrackMainView {

  @BindView(R.id.dropdown_bag_area_location)
  lateinit var dropdownBagAreaLocation: AutoCompleteDropDown

  @BindView(R.id.til_dropdown_bag_area_location)
  lateinit var tilBagAreaLocation: TextInputLayout

  @BindView(R.id.til_bagtag_no)
  lateinit var tilBagTag: TextInputLayout

  @BindView(R.id.dropdown_bag_area_location_code)
  lateinit var dropdownBagAreaCode: AutoCompleteDropDown

  @BindView(R.id.til_dropdown_bag_area_location_code)
  lateinit var tilBagAreaCode: TextInputLayout
  @BindView(R.id.edt_bagtag_no) lateinit var edtBagTagNo: EditText
  @BindView(R.id.btn_baggage_track_send) lateinit var btnSend: AppCompatButton
  @BindView(R.id.rcv_last_three_items) lateinit var rcvLastThree: RecyclerView

  @Inject lateinit var presenter: BaggageTrackMainPresenter

  private lateinit var locationAndNameCodes: List<TrackBaggageLocationDto>

  private var selectedLocationNameIndex = -1
  private var selectedLocationCodeIndex = -1
  private lateinit var bagAreaLocationAdapter: BagAreaLocationAdapter
  private lateinit var bagAreaCodeAdapter: BagAreaCodeAdapter
  private var scannedTagList = mutableListOf<ScannedTag>()
  private lateinit var lastThreeBagAdapter: LastThreeBagAdapter

  companion object {
    fun newInstance(): Fragment {
      val args = Bundle()
      val fragment = BaggageTrackMainFragment()
      fragment.arguments = args
      return fragment
    }
  }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    val fragmentView = inflater.inflate(R.layout.content_baggage_track_main, container, false)
    bindView(fragmentView)
    return fragmentView
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    setupTextInputLayoutsTitles()
    setupBagTagNoListener();
    setupLastThreeItems();
    updateSendButtonState();
    presenter.getLocationNamesAndCodes()
  }

  private fun setupLastThreeItems() {
    rcvLastThree.layoutManager = LinearLayoutManager(context)
    lastThreeBagAdapter = LastThreeBagAdapter()
    lastThreeBagAdapter.itemList = scannedTagList
    rcvLastThree.adapter = lastThreeBagAdapter
  }

  private fun setupTextInputLayoutsTitles() {
    FontUtils.setTextInputLayoutHintBold(
      context,
      R.font.poppins_bold,
      tilBagAreaLocation,
      tilBagAreaCode,
      tilBagTag
    )
  }

  private fun setupBagTagNoListener() {
    edtBagTagNo.addTextChangedListener(GeneralTextWatcher(edtBagTagNo) { updateSendButtonState() })
  }

  private fun updateSendButtonState() {
    if (isValidToSend()) {
      btnSend.alpha = 1.0f
      btnSend.isEnabled = true
    } else {
      btnSend.alpha = 0.5f
      btnSend.isEnabled = false
    }
  }

  private fun isValidToSend(): Boolean {
    if (StringUtils.isEmpty(edtBagTagNo.text.toString()) || edtBagTagNo.text.toString().length < 6 || selectedLocationNameIndex == -1 || selectedLocationCodeIndex == -1) {
      return false
    }
    return true
  }

  override fun getFragment(): BaggageTrackMainFragment {
    return this
  }

  override fun getPresenter(): Presenter<*> {
    return presenter
  }

  @OnClick(R.id.btn_baggage_track_send)
  fun onPressedBaggageTrackSend() {
    presenter.scanSingleBaggageTag(
      edtBagTagNo.text.toString(),
      locationAndNameCodes[selectedLocationNameIndex].locationNameCodes[selectedLocationCodeIndex].locationCode,
      locationAndNameCodes[selectedLocationNameIndex].locationNameCodes[0].locationName
    )
  }

  override fun setLocationNamesAndCodes(locationAndNameCodes: List<TrackBaggageLocationDto>) {
    this.locationAndNameCodes = locationAndNameCodes
    setupBagAreaLocationDropdown()
  }

  private fun setupBagAreaLocationDropdown() {
    bagAreaLocationAdapter = BagAreaLocationAdapter(
      context!!, R.layout.item_dropdown_general,
      locationAndNameCodes
    )
    dropdownBagAreaLocation.threshold = 1
    dropdownBagAreaLocation.setAdapter(bagAreaLocationAdapter)
    dropdownBagAreaLocation.setOnItemClickListener { adapterView, view, selectedIndex, l ->
      bagAreaLocationAdapter.setSelectedItemPosition(selectedIndex)
      selectedLocationNameIndex = selectedIndex
      selectedLocationCodeIndex = -1
      dropdownBagAreaCode.setTextAdapter("")
      updateSendButtonState()
      setupBagAreaCodeDropdown()
      false
    }
  }

  private fun setupBagAreaCodeDropdown() {
    bagAreaCodeAdapter = BagAreaCodeAdapter(
      context!!, R.layout.item_dropdown_general,
      locationAndNameCodes[selectedLocationNameIndex].locationNameCodes
    )
    dropdownBagAreaCode.setThreshold(1)
    dropdownBagAreaCode.setAdapter(bagAreaCodeAdapter)
    dropdownBagAreaCode.setOnItemClickListener { adapterView, view, selectedIndex, l ->
      bagAreaCodeAdapter.setSelectedItemPosition(selectedIndex)
      selectedLocationCodeIndex = selectedIndex
      updateSendButtonState()
      false
    }
  }

  override fun scannedBaggageTag(tagNo: String, isSuccess: Boolean) {
    val scannedTag = ScannedTag()
    scannedTag.success = isSuccess
    scannedTag.tagNo = tagNo
    scannedTagList.add(scannedTag)
    lastThreeBagAdapter.notifyDataSetChanged()
  }

  override fun showError(message: String?) {
  }

  override fun showError(messageId: Int) {
  }
}