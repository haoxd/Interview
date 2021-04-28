package com.bread.coalquality.mvc.controller.redis;

/**
 * @Description: 分布式锁->{
 *
 *     redis->{
 *
 *
 *        1 redisson ；
 *        2 setnx 命令-》{
 *              1- 必须加过期时间
 *              2- 自己删除自己的锁，不可以删除别人的锁
 *            lua脚本（jedis eval函数）
 *        }
 *
 *        会出现的问题-》{
 *
 *            1 redisson 解锁-》{
 *
 *                 进行判断是否还持有锁，且是否是当前线程持有这把锁
 *             }
 *
 *
 *        }
 *
 *     }
 *     mysql
 *     zookeeper
 *
 * }
 * @Author: haoxd
 * @Version: 1.0
 */
public class DistributedLock {




}
