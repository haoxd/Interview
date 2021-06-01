package com.bread.coalquality.mvc.controller.redis;

/**
 * @Description: redis的缓存过期淘汰策略
 *
 *
 * <p>
 *
 *      1 redis 默认内存是多少？-》{默认是没有配置的，在64位的操作系统是不限制大小的，32为最大3G}
 *
 *      2 怎么看redis的内存配置是多少？-》{
 *
 *
 *           1-在redis的config文件当中 大约在859 行附近 ，有个max-memory 字节类型配置（默认是没有配置的，在64位的操作系统是不限制大小的，32为最大3G）
 *           2- config get maxmemory 命令
 *
 *           }
 *      3 生产上一般怎么配置？-》{一般推荐配置为最大物理内存的四分之三  （maxmemory *0.75 ）}
 *
 *      4 如何设置redis的内存大小？-》{
 *
 *          1- 修改配置文件 - 指定maxmemory 的字节数即可
 *          2- 命令修改 -   config set maxmemory
 *      }
 *
 *      5 如何查看redis的内存使用情况? -》{  info memory }
 *
 *      6 redis内存满了怎么办？ -> { 进行set 的时候会报错 （oom 错误，提示内存溢出了，使用的内存大于设置的内存了）}
 *
 *      7 redis 缓存淘汰策略？-》{
 *
 *          1：6.0.8的版本 支持8中淘汰策略，默认使用的是缓存淘汰策略是：不进行淘汰
 *          2：如果一个键是是有生效时间的，哪到了过期时间后是不是马上就被在内存当中删除呢？
 *               答：不是的，
 *               2.1：哪过期后是什么时候进行删除呢？是一个什么样的操作呢？
 *                  答：redis 底层有三种不同的删除策略-》{
 *                      1 定时删除-》redis不会时时刻刻进行监测设置了过期时间的key，因为这样给cpu的额外工作压力增加（时间换空间）
 *                      2 惰性删除-》key到期后不会立即删除，而是等待下次数据访问，如果到期删除，没到期返回，如果一直没有在次访问就会一直在内存常驻，对内存不友好（空间换时间）
 *                      3 定期删除-》每隔一段时间执行一次删除过期key 的操作，注意进行的是随机筛选过期的key进行删除，所以也会出现有的key删除不掉
 *                  }
 *
 *      }
 *
 *      8 redis内存淘汰策略 说明下？-》{
 *
 *          1 volatile-lru：设置了过期时间的key使用LRU算法淘汰；
 *          2 allkeys-lru：所有key使用LRU算法淘汰；
 *          3 volatile-lfu：设置了过期时间的key使用LFU算法淘汰；
 *          4 allkeys-lfu：所有key使用LFU算法淘汰；
 *          5 volatile-random：设置了过期时间的key使用随机淘汰；
 *          6 allkeys-random：所有key使用随机淘汰；
 *          7 volatile-ttl：设置了过期时间的key根据过期时间淘汰，越早过期越早淘汰；
 *          8 noeviction：默认策略，当内存达到设置的最大值时，所有申请内存的操作都会报错(如set,lpush等)，只读操作如get命令可以正常执行；
 *
 *      }
 *
 *      9 生产环境怎么配置？-》{
 *
 *              1 命令 ：config set maxmemory- policy  allkeys- lru
 *              2 配置文件 ：891 行 maxmemory- policy allkeys- lru
 *
 *       }
 *
 *      10 lru和lfu 算法了解吗？-》{
 *
 *
 *          1 lru： 表示最近最少使用
 *          2 lfu： 表示最不经常使用
 *
 *      }
 *
 *      11 lru算法说一下？-》{
 *
 *
 *
 *
 *
 *      }
 * </>
 *
 * @Author: haoxd
 * @Version: 1.0
 */
public class RedisCache {
}
