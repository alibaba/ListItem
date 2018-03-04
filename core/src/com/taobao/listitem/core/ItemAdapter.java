package com.taobao.listitem.core;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.taobao.listitem.core.ListItem;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by fatian on 14-7-23.
 */
public class ItemAdapter extends BaseAdapter {

    protected List<ListItem> mData = new ArrayList<ListItem>();

    protected Context mContext;

    protected List<Class> typeClass = new ArrayList<Class>();
    //-----------------------------------
    private boolean mHasReturnedViewTypeCount = false;//是否已经调用getViewTypeCount

    private final int MAX_TYPE_COUNT = 10;

    private static final Boolean DEBUG = true;

    private static final String TAG = "ItemAdapter";
    //------------------------------------

    private int mTypeCount = MAX_TYPE_COUNT;

    public ItemAdapter(Context context) {
        mContext = context;
    }

    public void setTypeCount(int num) {
        if (mHasReturnedViewTypeCount) {
            throw new IllegalArgumentException("must call setTypeCount before setAdapter");
        }
        mTypeCount = num;
    }

    public int indexOfItem(ListItem item) {
        return mData.indexOf(item);
    }

    @Deprecated
    public void addItem(final ListItem item) {
        mData.add(item);
        if (!typeClass.contains(item.getClass())) { //判断该item的类型是否已经登记
            typeClass.add(item.getClass());         //登记item的类型
        }
    }

    public void addItem(int idx, final ListItem item) {
        mData.add(idx, item);
        if (!typeClass.contains(item.getClass())) { //判断该item的类型是否已经登记
            typeClass.add(item.getClass());         //登记item的类型
        }
    }

    public void addItems(final List<ListItem> items) {
        mData.addAll(items);
        for (ListItem item : items) {
            if (!typeClass.contains(item.getClass())) { //判断该item的类型是否已经登记
                typeClass.add(item.getClass());         //登记item的类型
            }
        }
    }

    public void addItems(ListItem... items) {
        for (ListItem item : items) {
            mData.add(item);
            if (!typeClass.contains(item.getClass())) { //判断该item的类型是否已经登记
                typeClass.add(item.getClass());         //登记item的类型
            }
        }
    }

    public ListItem removeItem(int idx) {
        if (idx < mData.size()) {
            ListItem t = mData.remove(idx);
            return t;
        } else {
            return null;
        }
    }

    public void removeItem(Class clz) {
        Iterator iterator = mData.iterator();
        boolean find = false;
        while (iterator.hasNext()) {
            if (clz.isInstance(iterator.next())) {
                find = true;
                iterator.remove();
            }
        }

    }

    public void clearItems() {
        mData.clear();
    }

    @Override
    public int getItemViewType(int position) {
        if (!mHasReturnedViewTypeCount) {
            mHasReturnedViewTypeCount = true;
        }

        final ListItem item = this.getItem(position);
        return typeClass.indexOf(item.getClass());

    }

    @Override
    public int getViewTypeCount() {
        if (!mHasReturnedViewTypeCount) {
            mHasReturnedViewTypeCount = true;
        }
        //此处会造成内存额外开销，如需避免，需要在SimpleListFragment.java执行setAdapter之前添加所有item
        return mTypeCount;
//        return Math.max(1, mItemLayouts.size());
    }

    //某一种item出现的个数，
    public int getCount(Class clz) {
        int num = 0;
        for (ListItem item : mData) {
            if (item.getClass().equals(clz)) {
                num++;
            }
        }
        return num;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public ListItem getItem(int position) {
        if (position < 0 || position >= getCount()) {
            return null;
        }
        ListItem item = mData.get(position);
        return item;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //TODO  if Error return?
        final ListItem item = this.getItem(position);
        if (item == null) {
            Log.e(TAG, "ERROR item is NULL");
        }
        return item.getView(convertView, parent);
    }


}
