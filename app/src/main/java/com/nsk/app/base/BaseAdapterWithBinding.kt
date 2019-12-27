package com.nsk.app.base

import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup

open abstract class BaseAdapterWithBinding<T>(listData:List<T>)  : RecyclerView.Adapter<BaseViewHolder>() {
    /**
     * 暂时不支持头布局
     */
    var listData:List<T>?=null
    init {
        this.listData = listData
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val dataBinding = DataBindingUtil.inflate<ViewDataBinding>(LayoutInflater.from(parent.context), getLayoutResource(viewType), parent, false)
        val holder = object : BaseViewHolder(dataBinding.root){}
        holder.binding = dataBinding
        return holder
    }


    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        //可以将一些简单的比如根据字段判断字体颜色什么的逻辑写在model里
        holder.binding!!.setVariable(setVariableID(),listData!!.get(position))
        holder.binding!!.executePendingBindings()
    }

    abstract fun getLayoutResource(viewType: Int):Int
    abstract fun setVariableID():Int
    fun refreshData(list:List<T>){
        listData = list
        notifyDataSetChanged()
    }


}