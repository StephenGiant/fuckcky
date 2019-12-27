package com.nsk.app.widget

import com.nsk.app.base.BaseAdapterWithBinding
import com.nsk.app.base.BaseViewHolder
import com.nsk.app.caikangyu.R

class MainCateAdapterWithBinding(listData: List<Any>) : BaseAdapterWithBinding<Any>(listData) {
    var selectPosition = 0
    override fun getLayoutResource(viewType: Int): Int {
       return R.layout.item_health_topcate
    }

    override fun setVariableID(): Int {
       return 0
    }

    override fun getItemCount(): Int {
      return 0
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
    }
}