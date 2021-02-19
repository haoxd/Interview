package com.bread.coalquality.mvc.controller;

import java.lang.ref.PhantomReference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;

/**
 * @Description: 强引用、软引用、弱引用、幻象引用有什么区别？
 * @Author: haoxd
 * @Version: 1.0
 */
public class JavaReferenceController {



    public static void main(String[] args) {
        StrongReference();

    }


    /***
     * 强引用
     * 特点：我们平常典型编码Object obj = new Object()中的obj就是强引用。
     * 通过关键字new创建的对象所关联的引用就是强引用。
     * 当JVM内存空间不足，JVM宁愿抛出OutOfMemoryError运行时错误（OOM），使程序异常终止，也不会靠随意回收具有强引用的“存活”对象来解决内存不足的问题。
     * 对于一个普通的对象，如果没有其他的引用关系，只要超过了引用的作用域或者显式地将相应（强）引用赋值为 null，就是可以被垃圾收集的了，具体回收时机还是要看垃圾收集策略。
     * ***/
    private static void StrongReference() {
        String str = new String("权威指南");
        //强制进行垃圾回收
        System.gc();
        System.runFinalization();
        //再次取出所引用的对象
        System.out.println(str);
    }

    /***
     *
     弱引用通过WeakReference类实现。
     弱引用的生命周期比软引用短。
     在垃圾回收器线程扫描它所管辖的内存区域的过程中，一旦发现了具有弱引用的对象，不管当前内存空间足够与否，都会回收它的内存。由于垃圾回收器是一个优先级很低的线程，因此不一定会很快回收弱引用的对象。
     弱引用可以和一个引用队列（ReferenceQueue）联合使用，如果弱引用所引用的对象被垃圾回收，Java虚拟机就会把这个弱引用加入到与之关联的引用队列中。
     应用场景：弱应用同样可用于内存敏感的缓存。
     *
     * ****/
    private static void WeakReference() {
        String str = new String("权威指南");
        //创建一个弱引用，让这个弱引用引用到“权威指南”字符串
        WeakReference weakReference = new WeakReference(str);
        //切断str引用和“Struts2权威指南”字符串之间的引用
        str = null;
        //取出弱引用所引用的对象
        System.out.println(weakReference.get());
        //强制进行垃圾回收
        System.gc();
        System.runFinalization();
        //再次取出弱引用所引用的对象
        System.out.println(weakReference.get());
    }

    /***
     *
     特点：软引用通过SoftReference类实现。 软引用的生命周期比强引用短一些。
     只有当 JVM 认为内存不足时，才会去试图回收软引用指向的对象：即JVM 会确保在抛出 OutOfMemoryError 之前，
     清理软引用指向的对象。软引用可以和一个引用队列（ReferenceQueue）联合使用，如果软引用所引用的对象被垃圾回收器回收，Java虚拟机就会把这个软引用加入到与之关联的引用队列中。
     后续，我们可以调用ReferenceQueue的poll()方法来检查是否有它所关心的对象被回收。如果队列为空，将返回一个null,否则该方法返回队列中前面的一个Reference对象。
     应用场景：软引用通常用来实现内存敏感的缓存。如果还有空闲内存，就可以暂时保留缓存，当内存不足时清理掉，这样就保证了使用缓存的同时，不会耗尽内存。
     *
     * ***/
    private static void SoftReference() {
        String str = new String("权威指南");
        //创建一个软引用，让这个弱引用引用到“权威指南”字符串
        SoftReference softReference = new SoftReference(str);
        //切断str引用和“Struts2权威指南”字符串之间的引用
        str = null;
        //取出软引用所引用的对象
        System.out.println(softReference.get());
        //强制进行垃圾回收
        System.gc();
        System.runFinalization();
        //再次取出软引用所引用的对象
        System.out.println(softReference.get());
    }

    /***
     * 特点：虚引用也叫幻象引用，通过 PhantomReference 类来实现。
     * 无法通过虚引用访问对象的任何属性或函数。幻象引用仅仅是提供了一种确保对象被 finalize 以后，做某些事情的机制。
     * 如果一个对象仅持有虚引用，那么它就和没有任何引用一样，在任何时候都可能被垃圾回收器回收。
     * 虚引用必须和引用队列 （ReferenceQueue）联合使用。当垃圾回收器准备回收一个对象时，如果发现它还有虚引用，就会在回收对象的内存之前，把这个虚引用加入到与之关联的引用队列中。
     ReferenceQueue queue = new ReferenceQueue ();
     PhantomReference pr = new PhantomReference (object, queue);
     程序可以通过判断引用队列中是否已经加入了虚引用，来了解被引用的对象是否将要被垃圾回收。如果程序发现某个虚引用已经被加入到引用队列，那么就可以在所引用的对象的内存被回收之前采取一些程序行动。
     应用场景：可用来跟踪对象被垃圾回收器回收的活动，当一个虚引用关联的对象被垃圾收集器回收之前会收到一条系统通知。
     *
     *
     * ****/
    private static void PhantomReference() {
        //创建一个字符串引用
        String str = new String("Struts2权威指南");
        //创建一个引用队列
        ReferenceQueue referenceQueue = new ReferenceQueue();
        //创建一个虚引用 让次虚引用引用到“Struts2权威指南”字符串
        PhantomReference phantomReference = new PhantomReference(str, referenceQueue);
        //切断str和“Struts2权威指南”之间的引用关系
        str = null;
        //取出虚引用所引用的对象，并不能通过虚引用访问被引用的对象，

        //所以此处输出的应该是null
        System.out.println(phantomReference.get());
        //强制进行垃圾回收
        System.gc();
        System.runFinalization();
        //取出引用队列最先进入队列中的引用于phantomReference进行比较
        System.out.println(referenceQueue);

        System.out.println(referenceQueue.poll() == phantomReference);
    }

}
