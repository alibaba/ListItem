package com.taobao.listitem.sample.items;

import android.view.View;

import com.taobao.listitem.core.v2.ItemViewHolder;
import com.taobao.listitem.core.v2.RecyclerDataItem;

/**
 * Created by fatian on 17/12/12.
 */

public class Item extends RecyclerDataItem<Item.ViewHolder,String> {

    public Item(String s) {
        super(s);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder) {

    }

    public static class ViewHolder extends ItemViewHolder{

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
