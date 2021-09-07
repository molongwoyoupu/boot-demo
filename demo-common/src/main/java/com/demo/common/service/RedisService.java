package com.demo.common.service;

import org.springframework.data.redis.connection.stream.RecordId;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * redis操作服务
 *
 * @author molong
 * @date 2021/9/6
 */
public interface RedisService {

    /**
     * 保存属性
     *
     * @param key   字段key
     * @param value 字段值
     * @param time  过期时间
     */
    void set(String key, Object value, long time);

    /**
     * 保存属性
     *
     * @param key   字段key
     * @param value 字段值
     */
    void set(String key, Object value);

    /**
     * 获取属性
     *
     * @param key 字段key
     * @return 字段值
     */
    Object get(String key);

    /**
     * 删除属性
     *
     * @param key 字段key
     * @return 删除结果
     */
    Boolean del(String key);

    /**
     * 批量删除属性
     *
     * @param keys 字段key集合
     * @return 删除结果
     */
    Long del(List<String> keys);

    /**
     * 设置过期时间
     *
     * @param key  字段key
     * @param time 过期时间
     * @return 设置结果
     */
    Boolean expire(String key, long time);

    /**
     * 设置过期时间，自定义时间单位
     *
     * @param key      字段key
     * @param time     过期时间
     * @param timeUnit 过期时间单位
     * @return 设置结果
     */
    Boolean expire(String key, long time, TimeUnit timeUnit);

    /**
     * 获取过期时间
     *
     * @param key 字段key
     * @return 过期时间
     */
    Long getExpire(String key);

    /**
     * 判断是否有该属性
     *
     * @param key 字段key
     * @return 是否存在
     */
    Boolean hasKey(String key);

    /**
     * 按delta递增
     *
     * @param key   字段key
     * @param delta 递增值
     * @return 递增结果
     */
    Long incr(String key, long delta);

    /**
     * 按delta递增
     *
     * @param key   字段key
     * @param delta 递增值
     * @param time  过期时间
     * @return 递增结果
     */
    Long incr(String key, long delta, long time);

    /**
     * 按delta递减
     *
     * @param key   字段key
     * @param delta 递减值
     * @return 递减结果
     */
    Long decr(String key, long delta);

    /**
     * 获取Hash结构中的属性
     *
     * @param key     字段key
     * @param hashKey hash内字段key
     * @return 属性值
     */
    Object hGet(String key, String hashKey);

    /**
     * 向Hash结构中放入一个属性
     *
     * @param key     字段key
     * @param hashKey hash内字段key
     * @param value   hash内字段值
     * @param time    过期时间
     * @return 属性值
     */
    Boolean hSet(String key, String hashKey, Object value, long time);

    /**
     * 向Hash结构中放入一个属性
     *
     * @param key     字段key
     * @param hashKey hash内字段key
     * @param value   hash内字段值
     */
    void hSet(String key, String hashKey, Object value);

    /**
     * 直接获取整个Hash结构
     *
     * @param key 字段key
     * @return Hash结构数据
     */
    Map<Object, Object> hGetAll(String key);

    /**
     * 直接设置整个Hash结构
     *
     * @param key  字段key
     * @param map  Hash结构
     * @param time 过期时间
     * @return 设置结果
     */
    Boolean hSetAll(String key, Map<String, Object> map, long time);

    /**
     * 直接设置整个Hash结构
     *
     * @param key 字段key
     * @param map Hash结构
     */
    void hSetAll(String key, Map<String, ?> map);

    /**
     * 删除Hash结构中的属性
     *
     * @param key     字段key
     * @param hashKey hash内字段key
     */
    void hDel(String key, Object... hashKey);

    /**
     * 判断Hash结构中是否有该属性
     *
     * @param key     字段key
     * @param hashKey hash内字段key
     * @return 是否存在
     */
    Boolean hHasKey(String key, String hashKey);

    /**
     * Hash结构中属性递增
     *
     * @param key     字段key
     * @param hashKey hash内字段key
     * @param delta   递增值
     * @return 递增结果
     */
    Long hIncr(String key, String hashKey, Long delta);

    /**
     * Hash结构中属性递减
     *
     * @param key     字段key
     * @param hashKey hash内字段key
     * @param delta   递减值
     * @return 递减结果
     */
    Long hDecr(String key, String hashKey, Long delta);

    /**
     * 获取Set结构
     *
     * @param key 字段key
     * @return Set结构
     */
    Set<Object> sMembers(String key);

    /**
     * 向Set结构中添加属性
     *
     * @param key    字段key
     * @param values set属性值
     * @return 添加结果
     */
    Long sAdd(String key, Object... values);

    /**
     * 向Set结构中添加属性
     *
     * @param key    字段key
     * @param time   过期时间
     * @param values set属性值
     * @return 添加结果
     */
    Long sAdd(String key, long time, Object... values);

    /**
     * 是否为Set中的属性
     *
     * @param key   字段key
     * @param value set属性值
     * @return 是否是
     */
    Boolean sIsMember(String key, Object value);

    /**
     * 获取Set结构的长度
     *
     * @param key 字段key
     * @return 长度
     */
    Long sSize(String key);

    /**
     * 删除Set结构中的属性
     *
     * @param key    字段key
     * @param values set属性值
     * @return 删除结果
     */
    Long sRemove(String key, Object... values);

    /**
     * 获取List结构中的属性
     *
     * @param key   字段key
     * @param start 开始索引
     * @param end   结束索引
     * @return 集合
     */
    List<Object> lRange(String key, long start, long end);

    /**
     * 获取List结构的长度
     *
     * @param key 字段key
     * @return 长度
     */
    Long lSize(String key);

    /**
     * 根据索引获取List中的属性
     *
     * @param key   字段key
     * @param index 索引
     * @return 值
     */
    Object lIndex(String key, long index);

    /**
     * 向List结构中添加属性
     *
     * @param key   字段key
     * @param value 属性值
     * @return 添加结果
     */
    Long lPush(String key, Object value);

    /**
     * 向List结构中添加属性
     *
     * @param key   字段key
     * @param value 属性值
     * @param time  过期时间
     * @return 添加结果
     */
    Long lPush(String key, Object value, long time);

    /**
     * 向List结构中批量添加属性
     *
     * @param key    字段key
     * @param values 属性值
     * @return 添加结果
     */
    Long lPushAll(String key, Object... values);

    /**
     * 向List结构中批量添加属性
     *
     * @param key    字段key
     * @param time   过期时间
     * @param values 属性值
     * @return 添加结果
     */
    Long lPushAll(String key, Long time, Object... values);

    /**
     * 从List结构中移除删除第{@code count} 次出现后的第一个value值。
     *
     * @param key   字段key
     * @param count 第几个
     * @param value 属性值
     * @return 移除结果
     */
    Long lRemove(String key, long count, Object value);

    /**
     * 是否存在key，存在不操作,不存在设置值
     *
     * @param key     字段key
     * @param value   字段值
     * @param timeout 过期时间
     * @return 是否做处理
     */
    boolean existingKey(String key, String value, Long timeout);

    /**
     * 向指定的消息通道推送消息，适用于 pus/sub模式
     *
     * @param channel 消息通道
     * @param msg     需要推送的消息对象
     */
    void publish(String channel, Object msg);

    /**
     * 使用 Stream 模式下的消息发送
     * <p> 由于 RedisTemplate 中的方法不能指定最大消息数量，因此自己封装一个 </p>
     *
     * @param streamKey 需要发动到哪个消息通道上去
     * @param msg       消息内容
     * @return 返回消息id
     */
    RecordId sendMsgForStream(String streamKey, Object msg);

    /**
     * redis锁，默认1000毫米释放
     *
     * @param lockKey lockKey
     * @return Boolean
     */
    Boolean lock(String lockKey);

    /**
     * redis锁
     *
     * @param lockKey     lockKey
     * @param releaseTime 释放时间，单位毫米
     * @return Boolean
     */
    Boolean lock(String lockKey, Long releaseTime);

    /**
     * 手动释放锁
     *
     * @param lockKey lockKey
     */
    void unlock(String lockKey);
}

