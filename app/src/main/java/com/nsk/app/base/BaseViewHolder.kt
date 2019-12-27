package com.nsk.app.base


import android.databinding.ViewDataBinding
import android.support.v7.widget.RecyclerView
import android.view.View

open  class BaseViewHolder (val view: View): RecyclerView.ViewHolder(view) {

    var binding:ViewDataBinding?=null

    fun setViewBinding(binding: ViewDataBinding){
        this.binding = binding
    }


    fun getViewBinding():ViewDataBinding?{
        return binding
    }
}