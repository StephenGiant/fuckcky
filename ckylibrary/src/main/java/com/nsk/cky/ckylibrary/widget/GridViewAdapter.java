package com.nsk.cky.ckylibrary.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.nsk.cky.ckylibrary.R;
import com.nsk.cky.ckylibrary.bean.BankCardBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Description: 银行item
 * Company    :
 * Author     : gene
 * Date       : 2018/8/26
 */
public class GridViewAdapter extends BaseAdapter {
    private List<BankCardBean.NskHostBankListBean> mDatas;
    private LayoutInflater inflater;
    /**
     * 页数下标,从0开始(当前是第几页)
     */
    private int curIndex;
    /**
     * 每一页显示的个数
     */
    private int pageSize;

    public GridViewAdapter(Context context, ArrayList<BankCardBean.NskHostBankListBean> mDatas, int curIndex, int pageSize) {
        this.mDatas = mDatas;
        this.pageSize = pageSize;
        this.curIndex = curIndex;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mDatas.size() > (curIndex + 1) * pageSize ? pageSize : (mDatas.size() - curIndex * pageSize);
    }

    @Override
    public Object getItem(int position) {
        return mDatas.get(position + curIndex * pageSize);
    }

    @Override
    public long getItemId(int position) {
        return position + curIndex * pageSize;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_gridview, null);
            vh = new ViewHolder();
            vh.iv = (ImageView) convertView.findViewById(R.id.imageView);
            vh.tv = (TextView) convertView.findViewById(R.id.textView);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        //计算一下位置
        int pos = position + curIndex*pageSize;
        Glide.with(convertView.getContext()).load(mDatas.get(pos).getN_bank_logo()).into(vh.iv);
        vh.tv.setText(mDatas.get(pos).getN_bank_name());
        return convertView;
    }

    class ViewHolder {
        public TextView tv;
        public ImageView iv;
    }
}