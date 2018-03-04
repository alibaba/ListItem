package com.taobao.listitem.core.v2;

import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.taobao.listitem.core.v2.LogUtil;


/**
 * Created by fatian on 14/12/21.
 * codetag:alipay
 */
public abstract class RecyclerDataItem<T extends ItemViewHolder, D> implements RecycleItem<T> {
    protected D data;
    @Deprecated
    protected T viewHolder;
    @Deprecated
    protected boolean isForceOnbind = false;

    protected ItemAdapter adapter;

    public RecyclerDataItem(D d) {
        this.data = d;
    }


    public void loadData(T holder) {

        if (null == holder) {
            return;
        }

        long currentTime = System.currentTimeMillis();

//        if (this == holder.getBindItem() && viewHolder == holder && !isForceOnbind) {
//            return;
//        }
//        //将viewHolder和Item关联
//        holder.bindItem(this);
//
//        this.viewHolder = holder;
//        if (data != null) {
//            viewHolder.dataHash = data.hashCode();
//        }

        onBindViewHolder(holder);

        LogUtil.d("recyclerView", this + " onbind time cost:" + (System.currentTimeMillis() - currentTime));
    }

    public D getData() {
        return data;
    }

    public void updateData(D d) {
        this.data = d;
        refreshItem();
    }

    public void setAdapter(ItemAdapter adapter) {
        this.adapter = adapter;
    }

    public void refreshItem() {
        if (adapter != null) {
            adapter.refreshItem(this);
        }
    }


    public int getLayoutId() {
        return -1;
    }

    public View getLayoutView(ViewGroup parent) {
        return null;
    }

    @Override
    public View getView(View convertView, ViewGroup parent) {
        if (getLayoutId() != -1) {
            return LayoutInflater.from(parent.getContext()).inflate(getLayoutId(), parent, false);
        } else if (getLayoutView(parent) != null) {
            return getLayoutView(parent);
        }
        throw new RuntimeException("RecyclerDataItem getView() is Null");
    }
}
