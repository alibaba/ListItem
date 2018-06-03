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




