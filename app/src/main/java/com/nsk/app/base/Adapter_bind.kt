package com.nsk.app.base

import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import java.lang.reflect.TypeVariable

/**
 * @Package com.nsk.app.base
 * @author qianpeng
 * @date 2018/8/21.
 * @describe 使用了databinding的基类adapter
 */
open abstract class Adapter_bind <T>(val list: List<T>):RecyclerView.Adapter<BaseViewHolder>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
//        LayoutInflater.from(parent.context).inflate(getLayoutResource(),parent,false)
       val bind = DataBindingUtil.inflate(LayoutInflater.from(parent.context),getLayoutResource(),parent,false) as ViewDataBinding
        val viewHolder = BaseViewHolder(bind.root)
        viewHolder.setViewBinding(bind)
        return viewHolder
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        bindData(holder,getVariableId(),list.get(position))
    }
    fun bindData(holder: BaseViewHolder,variableId:Int,item:T){
        holder.binding!!.setVariable(variableId,item)
    }
    abstract fun getVariableId():Int

    abstract fun getLayoutResource():Int;
}