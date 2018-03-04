package com.taobao.listitem.core.v2;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.taobao.listitem.core.ListItem;

/**
 * Created by fatian on 17/3/17.
 */
public interface RecycleItem<T extends RecyclerView.ViewHolder> {
    void onBindViewHolder(T t);

    void loadData(T viewHolder);

    View getView(View convertView, ViewGroup parent);

}