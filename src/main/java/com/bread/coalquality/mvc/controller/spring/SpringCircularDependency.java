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
 *  《2》 构造器循环依赖是无法解决的，你想让构造器注入支持循环依赖是不可能的。
 *  《3》 单例（singleton）bean是支持循环依赖的，不报错，但是原型（prototype）bean 是不支持循环依赖的，会报错。
 *  《4》 内部通过三级缓存解决循环依赖 （DefaultSingletonBeanRegistry）
 *              -》{
 *
 *                  1，singletonObjects 一级缓存 ： 缓存单例bean的名称和示例对象 ,存放已经经历了完整生命周期的bean对象
 *                  2，earlySingletonObjects 二级缓存： 存放早期暴露出来的bean对象，bean的生命周期未结束（属性还没有填充完）
 *                  3，singletonFactories 三级缓存： 存放可以生成的bean工厂
 *                  4，三级缓存bean的迁移过程-》{
 *
 *                      1，A创建的过程当中需要B，于是A将自己放入到singletonFactories 三级缓存当中，然后去实例化B
 *                      2，在实例化B的过程当中，发现需要A，于是B去先查询一级缓存，没有再去查询二级缓存，如果还是没有再去查询三级缓存
 *                           ，找到了A，然后把三级缓存当中的A放入到二级缓存当中，在删除三级缓存当的A
 *                      3，B顺利初始化完成后，会把自己放入到一级缓存当中（此时B当中的A是创建状态），然后会继续创建A，此时B已经是创建完成状态
 *                      所以创建A的时候直接在一级缓存当中获取的B，然后完成自己的创建，在把自己放入到一级缓存当中，然后删除二级缓存当中的自己。
 *                      4， 创建四大方法-》{
 *
 *                            1 getSingleton(); 获取单例bean
 *                            2 doCreateBean(); 创建单例bean
 *                            3 populateBean(); 给bean填充属性
 *                            4 addSingleton(); 添加bean到三级缓存
 *
 *
 *                      }
 *
 *
 *                  }
 *
 *
 *              }
 * </>
 */
public class SpringCircularDependency {


}
