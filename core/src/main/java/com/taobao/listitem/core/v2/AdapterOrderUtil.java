package com.taobao.listitem.core.v2;

/**
 * Created by fatian on 16/6/12.
 * 很多复杂页面中item按照一定次序排列,但item产生次序不可控(例如由于通过不同请求获得的item).
 * 这样在增加item时需要频繁判断其他item的位置.此类自动处理这个问题,以后考虑到合并到adapter中去.
 * <p>
 * codetag : alipay
 */
public class AdapterOrderUtil {

    private ItemAdapter adapter;


    private Class[] classArray;


    /**
     * 某次操作插入的item的范围
     */
    private int insertStarIdx = -1;
    private int insertEndIdx = -1;


    private AdapterOrderUtil() {

    }

    public static AdapterOrderUtil createInstance(ItemAdapter adapter, Class... order) {
        AdapterOrderUtil adapterUtil = new AdapterOrderUtil();
        adapterUtil.adapter = adapter;
        adapterUtil.classArray = order;

        return adapterUtil;
    }



    public void addItem(final RecyclerDataItem item) {

        int itemTypeIdx = -1;
        for (int i = 0; i < classArray.length; i++) {
            if (classArray[i].isInstance(item)) {
                itemTypeIdx = i;
                break;
            }
        }
        //没有预设位置或则本来就在最后,则默认添加到最后
        if (itemTypeIdx == -1) {
            adapter.addItems(item);
            return;
        }

        //以下都是要插入到其他item中间的

        Class[] preClz = new Class[itemTypeIdx + 1];
        for (int i = 0; i < itemTypeIdx + 1; i++) {
            preClz[i] = classArray[i];
        }
        int index = adapter.getCount(preClz);

        adapter.addItem(index, item);


        if (insertStarIdx > index) {
            insertStarIdx = index;
        }

        if (insertEndIdx < index) {
            insertEndIdx = index;
        }

    }

    //这个考虑的不是很成熟
    @Deprecated
    public void notifyDataChange() {
        if (insertStarIdx != -1 && insertEndIdx != -1) {
            int count = insertEndIdx - insertStarIdx + 1;
            adapter.notifyItemRangeInserted(insertStarIdx, count);

            adapter.notifyItemRangeChanged(insertStarIdx, count);
        }

    }

}
