package com.demo.common.service.impl;

import com.demo.common.service.RedisService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.RedisStreamCommands;
import org.springframework.data.redis.connection.stream.ByteRecord;
import org.springframework.data.redis.connection.stream.RecordId;
import org.springframework.data.redis.connection.stream.StreamRecords;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;


import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * redis操作实现类
 *
 * @author molong
 * @date 2021/9/6
 */
@Slf4j
public class RedisServiceImpl implements RedisService {

    /**
     * stream key 的统一前缀
     */
    public static final String STREAM_KEY_PREFIX = "STREAM:";
    /**
     * stream 流一个 streamKey上最多允许发送的消息量最大为10万
     */
    public static final Long SEND_MAX_LEN = 10 * 10000L;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    @Resource
    private ObjectMapper objectMapper;

    @Override
    public void set(String key, Object value, long time) {
        redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
    }

    @Override
    public void set(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    @Override
    public Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    @Override
    public Boolean del(String key) {
        return redisTemplate.delete(key);
    }

    @Override
    public Long del(List<String> keys) {
        return redisTemplate.delete(keys);
    }

    @Override
    public Boolean expire(String key, long time) {
        return redisTemplate.expire(key, time, TimeUnit.SECONDS);
    }

    @Override
    public Boolean expire(String key, long time, TimeUnit timeUnit) {
        return redisTemplate.expire(key, time, timeUnit);
    }

    @Override
    public Long getExpire(String key) {
        return redisTemplate.getExpire(key, TimeUnit.SECONDS);
    }

    @Override
    public Boolean hasKey(String key) {
        return redisTemplate.hasKey(key);
    }

    @Override
    public Long incr(String key, long delta) {
        return redisTemplate.opsForValue().increment(key, delta);
    }

    @Override
    public Long incr(String key, long delta, long time) {
        Boolean aBoolean = redisTemplate.hasKey(key);
        if (aBoolean == null || !aBoolean) {
            redisTemplate.opsForValue().set(key, delta, time, TimeUnit.SECONDS);
            return delta;
        } else {
            return incr(key, delta);
        }
    }

    @Override
    public Long decr(String key, long delta) {
        return redisTemplate.opsForValue().increment(key, -delta);
    }

    @Override
    public Object hGet(String key, String hashKey) {
        return redisTemplate.opsForHash().get(key, hashKey);
    }

    @Override
    public Boolean hSet(String key, String hashKey, Object value, long time) {
        redisTemplate.opsForHash().put(key, hashKey, value);
        return expire(key, time);
    }

    @Override
    public void hSet(String key, String hashKey, Object value) {
        redisTemplate.opsForHash().put(key, hashKey, value);
    }

    @Override
    public Map<Object, Object> hGetAll(String key) {
        return redisTemplate.opsForHash().entries(key);
    }

    @Override
    public Boolean hSetAll(String key, Map<String, Object> map, long time) {
        redisTemplate.opsForHash().putAll(key, map);
        return expire(key, time);
    }

    @Override
    public void hSetAll(String key, Map<String, ?> map) {
        redisTemplate.opsForHash().putAll(key, map);
    }

    @Override
    public void hDel(String key, Object... hashKey) {
        redisTemplate.opsForHash().delete(key, hashKey);
    }

    @Override
    public Boolean hHasKey(String key, String hashKey) {
        return redisTemplate.opsForHash().hasKey(key, hashKey);
    }

    @Override
    public Long hIncr(String key, String hashKey, Long delta) {
        return redisTemplate.opsForHash().increment(key, hashKey, delta);
    }

    @Override
    public Long hDecr(String key, String hashKey, Long delta) {
        return redisTemplate.opsForHash().increment(key, hashKey, -delta);
    }

    @Override
    public Set<Object> sMembers(String key) {
        return redisTemplate.opsForSet().members(key);
    }

    @Override
    public Long sAdd(String key, Object... values) {
        return redisTemplate.opsForSet().add(key, values);
    }

    @Override
    public Long sAdd(String key, long time, Object... values) {
        Long count = redisTemplate.opsForSet().add(key, values);
        expire(key, time);
        return count;
    }

    @Override
    public Boolean sIsMember(String key, Object value) {
        return redisTemplate.opsForSet().isMember(key, value);
    }

    @Override
    public Long sSize(String key) {
        return redisTemplate.opsForSet().size(key);
    }

    @Override
    public Long sRemove(String key, Object... values) {
        return redisTemplate.opsForSet().remove(key, values);
    }

    @Override
    public List<Object> lRange(String key, long start, long end) {
        return redisTemplate.opsForList().range(key, start, end);
    }

    @Override
    public Long lSize(String key) {
        return redisTemplate.opsForList().size(key);
    }

    @Override
    public Object lIndex(String key, long index) {
        return redisTemplate.opsForList().index(key, index);
    }

    @Override
    public Long lPush(String key, Object value) {
        return redisTemplate.opsForList().rightPush(key, value);
    }

    @Override
    public Long lPush(String key, Object value, long time) {
        Long index = redisTemplate.opsForList().rightPush(key, value);
        expire(key, time);
        return index;
    }

    @Override
    public Long lPushAll(String key, Object... values) {
        return redisTemplate.opsForList().rightPushAll(key, values);
    }

    @Override
    public Long lPushAll(String key, Long time, Object... values) {
        Long count = redisTemplate.opsForList().rightPushAll(key, values);
        expire(key, time);
        return count;
    }

    @Override
    public Long lRemove(String key, long count, Object value) {
        return redisTemplate.opsForList().remove(key, count, value);
    }

    /**
     * 使用redis SETNX判断任务是否已经执行
     *
     * @param key     redis key
     * @param value   零时值
     * @param timeout 超时时间，小于job运行间隔
     * @return 是否存在key
     */
    @Override
    public synchronized boolean existingKey(String key, String value, Long timeout) {
        Boolean existing = redisTemplate.opsForValue().setIfAbsent(key, value);
        redisTemplate.expire(key, timeout, TimeUnit.SECONDS);
        return Objects.nonNull(existing)&&existing;
    }

    @Override
    public void publish(String channel, Object msg) {
        try {
            redisTemplate.convertAndSend(channel, objectMapper.writeValueAsString(msg));
        } catch (JsonProcessingException e) {
            log.error("redis publish 消息序列化失败:", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public RecordId sendMsgForStream(String streamKey, Object msg) {
        try {
            //消息内容统一处理为json
            String jsonMsg = objectMapper.writeValueAsString(msg);
            Map<byte[], byte[]> payload = new HashMap<>(1);
            payload.put("payload".getBytes(StandardCharsets.UTF_8), jsonMsg.getBytes(StandardCharsets.UTF_8));
            ByteRecord byteRecord = StreamRecords.rawBytes(payload).withStreamKey((STREAM_KEY_PREFIX+streamKey).getBytes(StandardCharsets.UTF_8));
            RedisCallback<RecordId> callback = connection -> {
                //设置这个key上的最大消息数，避免消息太多导致 redis 服务器的内存爆炸
                return connection.streamCommands().xAdd(byteRecord, RedisStreamCommands.XAddOptions.maxlen(SEND_MAX_LEN));
            };
            return redisTemplate.execute(callback, true);
        } catch (JsonProcessingException e) {
            log.error("redis stream add 消息序列化失败:", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public Boolean lock(String lockKey) {
        // 默认过期1000毫秒
        return lock(lockKey, 1000L);
    }

    @Override
    public Boolean lock(String lockKey, Long releaseTime) {
        // 利用lambda表达式
        return redisTemplate.execute((RedisCallback<Boolean>) connection -> {
            long expireAt = System.currentTimeMillis() + releaseTime + 1;
            Boolean acquire = connection.setNX(lockKey.getBytes(), String.valueOf(expireAt).getBytes());
            if (Objects.nonNull(acquire)&&acquire) {
                return true;
            } else {
                byte[] value = connection.get(lockKey.getBytes());
                if (Objects.nonNull(value) && value.length > 0) {
                    long expireTime = Long.parseLong(new String(value));
                    if (expireTime < System.currentTimeMillis()) {
                        // 如果锁已经过期
                        byte[] oldValue = connection.getSet(lockKey.getBytes(),
                                String.valueOf(System.currentTimeMillis() + expireAt + 1).getBytes());
                        // 防止死锁
                        return Long.parseLong(new String(oldValue)) < System.currentTimeMillis();
                    }
                }
            }
            return false;
        });
    }

    @Override
    public void unlock(String lockKey) {
        redisTemplate.delete(lockKey);
    }
}

