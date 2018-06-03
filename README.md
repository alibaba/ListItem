## 什么是ListItem
ListItem是简化列表（ListView、RecyclerView）使用的组件。有了ListItem：
1. 不再需要编写一个呆板的无趣的Adapter。
2. 把精力专注于编写一个个高内聚、低耦合的Item。
3. 你可以像搭积木一样，在不同页面将这些Item组合起来。

## 为什么要ListItem
#### Adapter的问题
1. ListItem源于Adapter的"呆板"。在传统的方式中，无论ListView 还是 RecyclerView都需要一个Adapter，而每个Adapter
大多大同小异，编写Adapter的过程也是个"机械"、"繁复"、"低技术含量"、"低成就感"的过程。
2. Adapter 往往成为整个列表页面的逻辑节点，各种业务代码在这里交汇、纠缠。在一定程度上，Adapter成为整个页面的耦合点。
在页面需要变动时，我们往往需要在Adapter的多处进行修改。
#### ListItem的解决方案。
1. ListItem提供了一个通用的Adapter：ItemAdapter。
2. ListItem提供了页面元素的统一实现方案：RecyclerDataItem。任何页面元素继承自RecyclerDataItem，成为一个Item。RecyclerDataItem将一个页面元素的布局、viewholder、数据聚合在一起。
3. ItemAdapter.addItem(new RecyclerDataItem())



## 如何使用
####我们以一个部门分组的人员列表为例：

1. 使用ItemAdapter
```java
recyclerView = (RecyclerView) layoutView.findViewById(R.id.combolist);

LinearLayoutManager llm = new LinearLayoutManager(getActivity());

adapter = new ItemAdapter(getContent());

recyclerView.setLayoutManager(llm);
recyclerView.setAdapter(adapter);


```

2. 构建Item
``` java
/**
 *   展示一个文案的简单item，通过范性指定item的ViewHolder和数据（String）
 */
public class TitleItem extends RecyclerExtDataItem<TitleItem.ViewHolder, String> {


    public TitleItem(String data) {
        super(data, null);
    }

    /**
     * onBindViewHolder 里实现ViewHolder和数据（String）的绑定过程，也是传统RecyclerView
     * 的复用过程
     */
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder) {
        viewHolder.title.setText(data);
    }

    /**
     * 指定布局文件，也可以通过View getLayoutView(ViewGroup parent)指定视觉布局。
     */
    @Override
    public int getLayoutId() {
        return R.layout.title_item;
    }

    /**
     * 当前item的ViewHolder。推荐写成类似这样的内部类。代码内聚度更高。
     */
    public static class ViewHolder extends CustomRecyclerViewHolder {
        TextView title;

        public ViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
        }
    }
}

public class PeopleItem extends RecyclerExtDataItem<TitleItem.ViewHolder, People> {
    public TitleItem(People data) {
        super(data, null);
    }

    /**
     * onBindViewHolder 里实现ViewHolder和数据（String）的绑定过程，也是传统RecyclerView
     * 的复用过程
     */
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder) {
        viewHolder.name.setText(data.name);
        viewHolder.age.setText(data.age);
        viewHolder.address.setText(data.address);

    }
    @Override
    public int getLayoutId() {
        return R.layout.people_item;
    }

    /**
     * 当前item的ViewHolder。推荐写成类似这样的内部类。代码内聚度更高。
     */
    public static class ViewHolder extends CustomRecyclerViewHolder {
        TextView name;
        TextView age;
        TextView address;
        public ViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.name);

            age = (TextView) itemView.findViewById(R.id.age);

            address = (TextView) itemView.findViewById(R.id.address);
        }
    }
}

```

3. 添加Item

```
//添加部门1的人员
List<People> depart1 = ....
adapter.addItem(new TitleItem("部门1"),true);
for(People people : depart1){
    adapter.addItem(new PeopleItem(people),true);
}

//添加部门2的人员
List<People> depart2 = ....
adapter.addItem(new TitleItem("部门2"),true);
for(People people : depart2){
    adapter.addItem(new PeopleItem(people),true);
}

```
