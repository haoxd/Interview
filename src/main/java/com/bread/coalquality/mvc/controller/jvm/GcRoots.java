package com.bread.coalquality.mvc.controller.jvm;

/**
 * JVM垃圾回收的时候如何确定垃圾？是否知道什么是GC Roots
 * 1 什么是垃圾：简单来说就是内存中已经不再被使用的空间就是垃圾
 * 2-》{
 *     如何判断一个对象是否可以被回收？
 *     1 -》引用计数法{
 *          通过引用计数来判断一个对象是否可以回收。简单说，给对象中添加一个引用计数器。
 *          每当有一个地方引用它，计数器值加1，每当有一个引用失效，计数器值减1
 *          任何时刻计数器值为零的对象就是不可能再被使用的，那么这个对象就是可回收对象。
 *          那么为什么主流的Java虚拟机里面都没有选用这个方法呢？其中最主要的原因是它很难解决对象之间相互循环引用的问题。
 *          该算法存在但目前无人用了，解决不了循环引用的问题，了解即可。
 *     }
 *
 *     2 -》枚举根节点做可达性分析{
 *          所谓 GC Roots 或者说 Tracing Roots的“根集合” 就是一组必须活跃的引用.
 *          基本思路就是通过一系列名为 GC Roots的对象作为起始点，从这个被称为GC Roots的对象开始向下搜索，
 *          如果一个对象到GC Roots没有任何引用链相连，则说明此对象不可用。
 *          也即给定一个集合的引用作为根出发，通过引用关系遍历对象图，能被遍历到的（可到达的）对象就被判定为存活，没有被遍历到的对象就被判定为死亡.
 *      * 那些对象可以当做GC Roots
 *          虚拟机栈（栈帧中的局部变量区，也叫做局部变量表）中的引用对象
 *          方法区中的类静态属性引用的对象 static
 *          方法区中常量引用的对象 final
 *          本地方法栈中的JNI（Native方法）的引用对象 native修饰的方法修饰的对象
 *     }
 *
 * }
 * */
public class GcRoots {
}
