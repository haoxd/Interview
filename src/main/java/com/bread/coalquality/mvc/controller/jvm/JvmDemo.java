package com.bread.coalquality.mvc.controller.jvm;

import java.util.concurrent.TimeUnit;

/**
 * @Description: jvm知识点
 *<p>常见的垃圾回收算法
 * <1>引用计数:在双端循环，互相引用的时候，容易报错，目前很少使用这种方式了
 * <2>复制:复制算法在年轻代的时候，进行使用，复制时候有交换,优点：没有产生内存碎片
 * <3>标记清除:先标记，后清除，缺点是会产生内存碎片，用于老年代多一些
 * <4>标记清除整理:没有产生碎片化，但是需要付出代价，因为移动对象需要成本
 *</>
 * <p>如何查看运行java程序的jvm参数是否开启
 *（1）-》jps：查看java的后台进程 : jps -l ,得到进程号
 * (2)-》我们使用jinfo -flag 然后查看是否开启PrintGCDetails这个参数，jinfo -flag PrintGCDetails 12608
 * (3)->jinfo -flags 10980 查看所有jvm配置信息
 * </>
 * <p>查看JVM默认参数
 *（1）-XX:+PrintFlagsInitial，主要是查看初始默认值，java -XX:+PrintFlagsInitial（重要参数）
 * (2) -XX:+PrintFlagsFinal：表示修改以后，最终的值​ 如果有 := 表示修改过的， = 表示没有修改过的
 * (3)使用 -XX:+PrintCommandLineFlags 打印出JVM的默认的简单初始化参数 (可以看垃圾回收器是那个)
 * </>
 * <p>如何设置
 * 1-》-Xms（这两个参数，还是属于XX参数，因为取了别名） 等价于 -XX:InitialHeapSize ：初始化堆内存（默认只会用最大物理内存的64分1）
 * 2-》-Xmx（这两个参数，还是属于XX参数，因为取了别名）等价于 -XX:MaxHeapSize ：最大堆内存（默认只会用最大物理内存的4分1）
 *   注意（-Xms 和 -Xmx最好调整一致，防止JVM频繁进行收集和回收）
 * 3-》-XX:MetaspaceSize=1024m 设置元空间大小
 * </>
 * <p>常用参数
 * 1-》-Xms（这两个参数，还是属于XX参数，因为取了别名） 等价于 -XX:InitialHeapSize ：初始化堆内存（默认只会用最大物理内存的64分1）
 * 2-》-Xmx（这两个参数，还是属于XX参数，因为取了别名）等价于 -XX:MaxHeapSize ：最大堆内存（默认只会用最大物理内存的4分1）
 *   注意（-Xms 和 -Xmx最好调整一致，防止JVM频繁进行收集和回收）
 * 3-》-XX:MetaspaceSize=1024m 设置元空间大小
 * 4-》-Xss：设计单个线程栈的大小，一般默认为512K~1024K，等价于 -XX:ThreadStackSize
 * 5-》-Xmn：设置年轻代大小
 * </>
 * @Author: haoxd
 * @Version: 1.0
 */
public class JvmDemo {


    public static void main(String[] args) {

        try {
            TimeUnit.HOURS.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
