package com.taobao.listitem.core.v2;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by fatian on 14/12/20.
 */
public class ItemAdapter extends RecyclerView.Adapter<ItemViewHolder> {
    public List<RecyclerDataItem> mData = new ArrayList<>();
    protected Context mContext;

    protected List<Class> typeClass = new ArrayList<Class>();

    public ItemAdapter(Context context) {
        mContext = context;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewtype) {
        if (viewtype >= typeClass.size()) {
            return null;
        }
        long currentTime = System.currentTimeMillis();
        Class clz = typeClass.get(viewtype);

        for (RecyclerDataItem item : mData) {
            if (clz.isInstance(item)) {
                Class typeClz = findBindViewHolderClz(clz);
                if (typeClz == null) {
                    throw new IllegalArgumentException("item must extended from DataItem or implement RecycleItem");
                }
                try {
                    Constructor constructor = typeClz.getConstructor(View.class);
                    ItemViewHolder viewHolder = (ItemViewHolder) constructor.newInstance(item.getView(null, viewGroup));
                    LogUtil.d("recyclerView",
                        viewHolder.toString() + " oncreate time cost:" + (System.currentTimeMillis() - currentTime));

                    return viewHolder;
                } catch (NoSuchMethodException e) {
                    LogUtil.e("CustomRecyclerAdapter", e);
                } catch (InvocationTargetException e) {
                    LogUtil.e("CustomRecyclerAdapter", e);
                } catch (InstantiationException e) {
                    LogUtil.e("CustomRecyclerAdapter", e);
                } catch (IllegalAccessException e) {
                    LogUtil.e("CustomRecyclerAdapter", e);
                }

                return null;
            }
        }

        return null;
    }

    /**
     * 根据class获得绑定的ViewHolder
     *
     * @param clz
     * @return
     */
    private Class findBindViewHolderClz(Class clz) {
        if (clz == null) {
            return null;
        }
        //自己的范型
        TypeVariable[] typeVariables = clz.getTypeParameters();
        for (TypeVariable typeVariable : typeVariables) {


            for (Type type : typeVariable.getBounds()) {
                if ((type instanceof Class) && RecyclerView.ViewHolder.class.isAssignableFrom((Class) type)) {
                    return (Class) type;
                }
            }
        }
        //先找父类实现的范行
        Type superClzType = clz.getGenericSuperclass();
        if (superClzType == null) {
            return null;
        }
        if (superClzType instanceof ParameterizedType) {
            Class targetClz = findBindViewHolderWithParameterizedType((ParameterizedType) superClzType);
            if (targetClz != null) {
                return targetClz;
            }
        }

        Type[] interfaceTypes = clz.getGenericInterfaces();
        if (interfaceTypes != null && interfaceTypes.length > 0) {
            for (Type type : interfaceTypes) {
                if (type instanceof ParameterizedType) {
                    Class targetClz = findBindViewHolderWithParameterizedType((ParameterizedType) type);
                    if (targetClz != null) {
                        return targetClz;
                    }
                }
            }
        }
        //都没有的话递归父类m
        return findBindViewHolderClz(clz.getSuperclass());


    }

    private Class findBindViewHolderWithParameterizedType(ParameterizedType parameterizedType) {
        Type[] argumentTypes = parameterizedType.getActualTypeArguments();

        for (Type type : argumentTypes) {
            if ((type instanceof Class) && RecyclerView.ViewHolder.class.isAssignableFrom((Class) type)) {
                return (Class) type;
            }
        }
        return null;
    }


    @Override
    public void onBindViewHolder(ItemViewHolder viewHolder, int position) {

        RecycleItem item = getItem(position);

        item.loadData(viewHolder);

        LogUtil.d("recyclerView", "onbind:" + viewHolder.toString());
    }


    public int indexOfItem(RecycleItem item) {
        return mData.indexOf(item);
    }

    public int indexOfItem(Class clazz) {

        for (int i = 0; i < mData.size(); i++) {
            if (clazz.isInstance(mData.get(i))) {
                return i;
            }
        }
        return -1;
    }

    /**
     * 查找第index个item的position
     *
     * @param index 找到对应index处的item的Position
     * @param clazz
     * @return 如果有index个item，则返回Position，如果没有，则返回-1
     */
    public int indexOfItem(Class clazz, int index) {

        int lastPosition = -1;
        for (int i = 0; i < mData.size(); i++) {
            if (clazz.isInstance(mData.get(i))) {
                lastPosition = i;
                if (index == 0) {
                    return lastPosition;
                }
                index--;
            }
        }
        return -1;
    }


    public int indexOfObjByClass(Object object) {
        if (object == null) {
            return -1;
        }
        return indexOfObjByClass(object, object.getClass());
    }

    public int indexOfObjByClass(Object object, Class clz) {
        if (object == null || clz == null) {
            return -1;
        }
        if (!clz.isInstance(object)) {
            return -1;
        }
        int index = -1;
        for (RecyclerDataItem item : mData) {
            if (clz.isInstance(item.data)) {
                index++;
            }
            if (object.equals(item.data)) {
                return index;
            }
        }
        return -1;

    }

    public void addItem(final RecyclerDataItem item) {
        mData.add(item);
        item.setAdapter(this);
        if (!typeClass.contains(item.getClass())) { //判断该item的类型是否已经登记
            typeClass.add(item.getClass());         //登记item的类型
        }
        notifyItemInserted(mData.size() -1);
    }

    public void addItem(int idx, final RecyclerDataItem item) {
        mData.add(idx, item);
        item.setAdapter(this);
        if (!typeClass.contains(item.getClass())) { //判断该item的类型是否已经登记
            typeClass.add(item.getClass());         //登记item的类型
        }
        notifyItemInserted(idx);
    }

    public void addItems(final List<RecyclerDataItem> items) {
        mData.addAll(items);
        for (RecyclerDataItem item : items) {
            item.setAdapter(this);
            if (!typeClass.contains(item.getClass())) { //判断该item的类型是否已经登记
                typeClass.add(item.getClass());         //登记item的类型
            }
        }
    }

    public void addItems(RecyclerDataItem... items) {
        for (RecyclerDataItem item : items) {
            mData.add(item);
            item.setAdapter(this);
            if (!typeClass.contains(item.getClass())) { //判断该item的类型是否已经登记
                typeClass.add(item.getClass());         //登记item的类型
            }
        }
    }

    public RecycleItem removeItem(int idx) {
        if (idx < mData.size()) {
            RecycleItem t = mData.remove(idx);
            return t;
        } else {
            return null;
        }
    }

    public void removeItem(RecyclerDataItem item) {
        if (item == null) {
            return;
        }
        mData.remove(item);
    }

    public void removeItems(RecyclerDataItem... items) {
        if (items == null || items.length == 0) {
            return;
        }

        for (int i = 0; i < items.length; i++) {
            Iterator<RecyclerDataItem> it = mData.iterator();
            while (it.hasNext()) {
                RecyclerDataItem recyclerDataItem = it.next();
                if (recyclerDataItem == items[i]) {
                    it.remove();
                    break;
                }
            }
        }
    }

    public void removeItem(Class clz) {
        Iterator iterator = mData.iterator();
        while (iterator.hasNext()) {
            if (clz.isInstance(iterator.next())) {
                iterator.remove();
            }
        }

    }

    public void clearItems() {
        mData.clear();
        //        typeClass.clear();
    }

    public <T extends RecyclerDataItem> List<T> findItems(Class<T> clz) {
        List<T> items = new ArrayList<T>();
        for (RecyclerDataItem item : mData) {
            if (item.getClass().equals(clz)) {
                items.add((T) item);
            }
        }

        return items;
    }

    //某一种item出现的个数，
    public int getCount(Class clz) {
        int num = 0;
        for (RecyclerDataItem item : mData) {
            if (item.getClass().equals(clz)) {
                num++;
            }
        }
        return num;
    }

    //某一种item出现的个数，
    public int getCountByBaseItem(Class clz) {
        int num = 0;
        for (RecyclerDataItem item : mData) {
            if (clz.isInstance(item)) {
                num++;
            }
        }
        return num;
    }

    //某一种item出现的个数，
    public int getCount(Class... clzs) {
        int num = 0;
        for (RecycleItem item : mData) {
            for (Class clz : clzs) {
                if (item.getClass().equals(clz)) {
                    num++;
                    break;
                }
            }

        }
        return num;
    }

    public RecycleItem getItem(int position) {
        if (position < 0 || position >= getItemCount()) {
            return null;
        }
        RecycleItem item = mData.get(position);
        return item;
    }


    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public int getItemViewType(int position) {
        final RecycleItem item = getItem(position);
        return typeClass.indexOf(item.getClass());
    }

    public void preAddType(Class clz) {
        if (!typeClass.contains(clz)) { //判断该item的类型是否已经登记
            typeClass.add(clz);         //登记item的类型
        }
    }

    public int getItemType(Class clz) {
        return typeClass.indexOf(clz);
    }

    public void refreshItem(RecyclerDataItem item) {
        this.notifyItemChanged(mData.indexOf(item));
    }

}
