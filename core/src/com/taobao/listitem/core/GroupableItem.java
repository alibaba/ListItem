package com.taobao.listitem.core;

/**
 * Created by fatian on 14-10-11.
 */
public interface GroupableItem {
    //返回一个所在分组的Item。例如按字母分组的城市列表。对于城市item返回它所属的字母item，对于字母item返回自己。
    ListItem getGroupItem();
}
