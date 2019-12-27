package com.nsk.app.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.bumptech.glide.Glide;
import com.nsk.app.base.BaseViewHolder;
import com.nsk.app.caikangyu.R;
import com.nsk.app.config.GlideImageLoader;
import com.nsk.app.config.Routers;
import com.nsk.cky.ckylibrary.bean.BankCardBean;
import com.nsk.cky.ckylibrary.widget.GridViewAdapter;
import com.nsk.cky.ckylibrary.widget.RecyclerItemCLickListenner;
import com.scwang.smartrefresh.layout.util.DensityUtil;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * @author qianpeng
 * @Package com.nsk.app.adapter
 * @date 2018/10/11.
 * @describe
 */
public class CkyBanksAdapter extends PagerAdapter {
    private Context mContext;
    private List<List<BankCardBean.NskHostBankListBean>> datas;

    public CkyBanksAdapter(Context context, List<List<BankCardBean.NskHostBankListBean>> datas){
        mContext =context;
        this.datas = datas;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
        object=null;
//        super.destroyItem(container, position, object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {

        if(datas.size()==1&&datas.get(0).size()<5){
            //100高度
            ViewGroup.LayoutParams layoutParams = container.getLayoutParams();
            layoutParams.height = DensityUtil.dp2px(110f);
            container.setLayoutParams(layoutParams);
        }else {
            ViewGroup.LayoutParams layoutParams = container.getLayoutParams();
            layoutParams.height = DensityUtil.dp2px(220f);
            container.setLayoutParams(layoutParams);
        }
        RecyclerView recyclerView = new RecyclerView(mContext);
        recyclerView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT));
        GridLayoutManager layoutManager = new GridLayoutManager(mContext, 4, GridLayoutManager.HORIZONTAL, false){
            @Override
            public boolean canScrollVertically() {
                return false;
            }

            @Override
            public boolean canScrollHorizontally() {
                return false;
            }
        };
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.addOnItemTouchListener(new RecyclerItemCLickListenner(mContext) {
            @Override
            public void onItemClick(@NotNull View view, int p) {
                ARouter.getInstance().build(Routers.recommendCard).withString("id", datas.get(position).get(p).getN_bank_id()+"").navigation();
            }
        });
        recyclerView.setAdapter(new RecyclerView.Adapter() {
            @NonNull
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_gridview,parent,false);

                return new BaseViewHolder(view);
            }

            @Override
            public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int pos) {
                BaseViewHolder viewHolder = (BaseViewHolder) holder;
                ImageView imageView = viewHolder.itemView.findViewById(R.id.imageView);
                TextView textView = viewHolder.itemView.findViewById(R.id.textView);
                Glide.with(mContext).load(datas.get(position).get(pos).getN_bank_logo()).into(imageView);
                textView.setText(datas.get(position).get(pos).getN_bank_name());
            }

            @Override
            public int getItemCount() {
                return datas.get(position).size();
            }
        });
        container.addView(recyclerView);
        return recyclerView;
    }

    @Override
    public int getCount() {
        //决定有多少页
        if(datas!=null) {
            return datas.size();
        }else {
            return 0;
        }
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view==object;
    }
}
