package com.nsk.app.bussiness.card

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.alibaba.android.arouter.facade.annotation.Route
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.nsk.app.base.BaseTitleActivity
import com.nsk.app.bean.CredBankCards
import com.nsk.app.config.ApiConfig
import com.nsk.app.caikangyu.R
import com.nsk.app.config.Routers
import com.nsk.app.utils.RxjavaUtils
import com.nsk.app.widget.MyDividerItemDecoration
import com.nsk.cky.ckylibrary.utils.ScreenTools
import com.zhy.adapter.recyclerview.CommonAdapter
import com.zhy.adapter.recyclerview.base.ViewHolder
import com.zhy.adapter.recyclerview.wrapper.EmptyWrapper
import io.reactivex.Flowable
import kotlinx.android.synthetic.main.activity_select_bank.*
import kotlinx.android.synthetic.main.search_view.*


/**
 * Description: 选择银行
 * Company    :
 * Author     : gene
 * Date       : 2018/7/29
 */
@Route(path = Routers.select_bank)
class BankSelectActivity : BaseTitleActivity() {
    private var inflater: LayoutInflater? = null
    private var mDatas: ArrayList<CredBankCards>? = null
    private var mTempDatas=ArrayList<CredBankCards>()
    private var searchDatas = ArrayList<CredBankCards>()
    private var mEmptyWrapper: EmptyWrapper<Any>? = null
    private var mAdapter1:  CommonAdapter<CredBankCards>? = null
    private var ccc:String = ""
    override fun setTitle(): Int {
        return R.string.select_bank
    }

    override fun getContentLayoutId(): Int {
        return R.layout.activity_select_bank
    }

    override fun initData() {
        val extras = intent.extras
        serviceApi.getForString(ApiConfig.getAllCards+"?containtCard=0").compose(RxjavaUtils.transformer()).compose(RxjavaUtils.handleResult())
                .subscribe({ response ->
                    val gson = Gson()
                    mDatas = gson.fromJson<ArrayList<CredBankCards>>(response, object : TypeToken<java.util.ArrayList<CredBankCards>>() {
                    }.type)
                    Flowable.fromIterable(mDatas).subscribe {
                        mTempDatas!!.add(it)
                    }
                    initCards()
                }, {})
    }

    override fun initView() {
        search_content.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(p0: Editable?) {
                if(p0.toString().length>0){
                    cancel.visibility= View.VISIBLE
                }else{
                    cancel.visibility= View.GONE

                }
                ccc=p0.toString()
                searchDatas.clear()
                if(ccc.length==0){
                    mTempDatas!!.clear()
                    Flowable.fromIterable(mDatas).subscribe {
                        mTempDatas!!.add(it)
                    }
                }else{
                    Flowable.fromIterable(mDatas).subscribe {
                        if (it.n_bank_name.contains(ccc)) {
                            searchDatas.add(it)
                        }
                    }
                    mTempDatas!!.clear()
                    mTempDatas!!.addAll(searchDatas)
                }
                mAdapter1!!.notifyDataSetChanged()
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

        })
        cancel.setOnClickListener {
            ccc=""
            cancel.visibility= View.GONE
        }

    }

    private fun initCards() {
        rv_view.layoutManager = LinearLayoutManager(this)
         mAdapter1 = object : CommonAdapter<CredBankCards>(this, R.layout.item_linera_bank, mTempDatas) {
            override fun convert(holder: ViewHolder?, t: CredBankCards?, position: Int) {
                val iv = holder?.getView<ImageView>(R.id.iv_bank)
                val tv_bank = holder?.getView<TextView>(R.id.tv_bank)
                val ll_1 = holder?.getView<LinearLayout>(R.id.ll_1)
                Glide.with(this@BankSelectActivity).load(t!!.n_bank_logo).into(iv)
                tv_bank!!.text = t.n_bank_name
                ll_1!!.setOnClickListener {
                    val bundle = Bundle()
                    intent.putExtra("cardId",t.n_bank_id.toString())
                    intent.putExtra("cardName",t.n_bank_name)
                    setResult(-1,intent)
                    finish()
                }
            }
        }
        val decoration = MyDividerItemDecoration(LinearLayoutManager.VERTICAL)
        decoration.dleft = ScreenTools(this).dp2px(55f)
        decoration.setDrawable(resources.getDrawable(R.drawable.divider))
        rv_view.addItemDecoration(decoration)
        mEmptyWrapper = EmptyWrapper<Any>(mAdapter1)
        mEmptyWrapper!!.setEmptyView(LayoutInflater.from(this).inflate(R.layout.empty_view, rv_view, false))
        rv_view.adapter = mEmptyWrapper
    }

}
