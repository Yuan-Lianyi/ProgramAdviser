package com.example.programadviser.model

/**
 * Model 层的数据源：问题分类与问答语料。
 *
 * 这里只有数据，不含界面代码；扩充分类只需在本文件追加条目。
 * 界面目前只使用分类名，问答内容作为数据保留。
 */
object DefaultAdviserCorpus {

    val categories: List<AdviserCategory> = listOf(
        AdviserCategory(
            name = "Java 基础",
            items = listOf(
                AdviserItem(
                    question = "Java 中基本数据类型有哪些？",
                    keywords = listOf("基本数据类型", "数据类型", "八种"),
                    answer = "Java 有 8 种基本数据类型：byte、short、int、long、float、double、char、boolean。"
                ),
                AdviserItem(
                    question = "== 与 equals() 有什么区别？",
                    keywords = listOf("==", "equals", "相等"),
                    answer = "== 比较的是引用（地址）或基本类型的值；equals() 比较的是内容。"
                ),
                AdviserItem(
                    question = "String、StringBuilder、StringBuffer 怎么选？",
                    keywords = listOf("StringBuilder", "StringBuffer", "字符串拼接"),
                    answer = "String 不可变；StringBuilder 可变、非线程安全、最快；StringBuffer 可变且线程安全。"
                ),
                AdviserItem(
                    question = "重载和重写的区别是什么？",
                    keywords = listOf("重载", "重写"),
                    answer = "重载在同一类中，方法名相同、参数不同；重写在父子类之间，方法签名相同。"
                )
            )
        ),
        AdviserCategory(
            name = "面向对象",
            items = listOf(
                AdviserItem(
                    question = "封装、继承、多态分别是什么？",
                    keywords = listOf("封装", "继承", "多态"),
                    answer = "封装：把数据与操作包装在类里；继承：子类复用父类成员；多态：同一引用表现出不同行为。"
                ),
                AdviserItem(
                    question = "接口和抽象类怎么选？",
                    keywords = listOf("接口", "抽象类"),
                    answer = "抽象类描述“是什么”，单继承；接口描述“能做什么”，可实现多个。"
                )
            )
        ),
        AdviserCategory(
            name = "Android 基础",
            items = listOf(
                AdviserItem(
                    question = "Activity 的生命周期有哪些回调？",
                    keywords = listOf("Activity", "生命周期"),
                    answer = "onCreate → onStart → onResume → onPause → onStop → onDestroy。"
                ),
                AdviserItem(
                    question = "ViewBinding 和 findViewById 有什么区别？",
                    keywords = listOf("ViewBinding", "findViewById"),
                    answer = "ViewBinding 由编译器生成绑定类，类型安全、空安全，比 findViewById 更可靠。"
                ),
                AdviserItem(
                    question = "ScrollView 里为什么只能放一个直接子 View？",
                    keywords = listOf("ScrollView", "滚动"),
                    answer = "ScrollView 继承自 FrameLayout，只支持一个直接子 View，多个控件要嵌套一层 LinearLayout。"
                )
            )
        ),
        AdviserCategory(
            name = "数据结构与算法",
            items = listOf(
                AdviserItem(
                    question = "数组和链表的区别？",
                    keywords = listOf("数组", "链表"),
                    answer = "数组内存连续、支持随机访问；链表靠指针相连、插入删除快。"
                ),
                AdviserItem(
                    question = "冒泡排序和快速排序的时间复杂度？",
                    keywords = listOf("冒泡排序", "快速排序", "排序"),
                    answer = "冒泡排序平均 O(n²)；快速排序平均 O(n log n)，最坏 O(n²)。"
                ),
                AdviserItem(
                    question = "栈和队列有什么区别？",
                    keywords = listOf("栈", "队列"),
                    answer = "栈是后进先出（LIFO）；队列是先进先出（FIFO）。"
                )
            )
        )
    )
}
