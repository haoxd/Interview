package com.bread.coalquality.mvc.controller.spring;

/**
 * @Description: spring 循环依赖
 * @Author: haoxd
 * @Version: 1.0
 *
 *
 * <> 什么是循环依赖
 *  多个spring bean 互相引用，形成一个闭环，例如 a依赖b，b依赖c，c依赖a
 * </>
 * <> 循环依赖
 *  抛出 BeanCurrentlyInCreationException
 *  如果主要使用构造函数注入，则可能会创建无法解决的循环依赖方案。
 *  （一种可能的解决方案是编辑某些类的源代码，这些类的源代码由设置者而不是构造函数来配置。
 *  或者，避免构造函数注入，而仅使用setter注入。
 *  换句话说，尽管不建议这样做，但是您可以使用setter注入配置循环依赖项。）
 *
 *
 *  《1》 ab 循环依赖问题 ，主要A注入B的方式是setter方式，且bean是单例的，就不会有循环依赖问题。
 * </>
 */
public class SpringCircularDependency {
}
