package com.taobao.listitem.core;

import android.view.View;
import android.view.ViewGroup;

/**
 * Created by fatian on 14-11-6.
 */
public class DataItem<T> implements ListItem {
    protected  T data;
    public DataItem(T data){
        this.data = data;
    }
    @Override
    public View getView(View convertView, ViewGroup parent) {
        return null;
    }
}
