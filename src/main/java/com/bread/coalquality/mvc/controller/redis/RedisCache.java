package com.bread.coalquality.mvc.controller.redis;

/**
 * @Description: redis的缓存过期淘汰策略
 *
 *
 * <p>
 *
 *      1 redis 默认内存是多少？-》{默认是没有配置的，在64位的操作系统是不限制大小的，32为最大3G}
 *      2 怎么看redis的内存配置是多少？-》{
 *
 *
 *           1-在redis的config文件当中 大约在859 行附近 ，有个max-memory 字节类型配置（默认是没有配置的，在64位的操作系统是不限制大小的，32为最大3G）
 *           2- config get maxmemory 命令
 *
 *           }
 *      3 生产上一般怎么配置？-》{一般推荐配置为最大物理内存的四分之三  （maxmemory *0.75 ）}
 *      4 如何设置redis的内存大小？-》{
 *
 *          1- 修改配置文件 - 指定maxmemory 的字节数即可
 *          2- 命令修改 -   config set maxmemory
 *      }
 *      5 如何查看redis的内存使用情况? -》{  info memory }
 *
 *      6 redis内存满了怎么办？ -> { 进行set 的时候会报错 （oom 错误，提示内存溢出了，使用的内存大于设置的内存了）}
 * </>
 *
 * @Author: haoxd
 * @Version: 1.0
 */
public class RedisCache {
}
