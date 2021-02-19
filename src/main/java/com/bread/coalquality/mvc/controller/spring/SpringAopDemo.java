package com.bread.coalquality.mvc.controller.spring;

/**
 * @Description: spring aop
 *
 * <p>常用注解
 * @before ：前置通知，目标方法执行执行
 * @after : 后置通知，目标方法执行之后，（始终执行）`
 * @afterReturning : 返回后通知。执行方法结束前执行，（异常不执行）
 * @afterThrowing : 异常通知，出现异常执行
 * @around ： 环绕通知，环绕目标方法执行
 * </>
 *
 * <>spring4， spring boot 1.0+ 版本
 * 1-》正常逻辑-》{
     * 1 先直接环绕通知之前
     * 2 然后执行 before
     * 3 然后执行业务逻辑
     * 4 然后执行环绕通知之后
     * 5 然后执行after
     * 6 然后执行afterReturning
 * }
 * 2-》出异常逻辑-》{
     * 1 先直接环绕通知之前
     * 2 然后执行 before
     * 3 然后执行after
     * 4 然后执行afterThrowing
 * }
 *
 *
 *
 * </>
 * <>spring5 ， spring boot 2.0+ 版本
 * 1-》正常逻辑-》{
     * 1 先直接环绕通知之前
     * 2 然后执行 before
     * 3 然后执行业务逻辑
     * 4 然后执行afterReturning
     * 5 然后执行after
     * 6 然后执行环绕通知之后


 * }
 * 2-》出异常逻辑-》{
     * 1 先直接环绕通知之前
     * 2 然后执行 before
     * 3 然后执行afterThrowing
     * 4 然后执行after

 * }
 *
 *
 *
 * </>
 * @Author: haoxd
 * @Version: 1.0
 */
public class SpringAopDemo {
}
