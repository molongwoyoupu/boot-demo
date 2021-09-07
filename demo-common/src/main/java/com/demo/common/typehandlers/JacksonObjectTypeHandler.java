package com.demo.common.typehandlers;

import com.baomidou.mybatisplus.extension.handlers.AbstractJsonTypeHandler;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.io.IOException;
import java.util.List;

/**
 * @author molong
 * @date 2021/9/6
 */
@MappedTypes({List.class})
@MappedJdbcTypes({JdbcType.VARCHAR})
public class JacksonObjectTypeHandler extends AbstractJsonTypeHandler<Object> {
    private static final Logger log = LoggerFactory.getLogger(JacksonObjectTypeHandler.class);
    protected static ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
    private final Class<?> type;

    public JacksonObjectTypeHandler(Class<?> type) {
        if (log.isTraceEnabled()) {
            log.trace("JacksonObjectTypeHandler(" + type + ")");
        }

        Assert.notNull(type, "Type argument cannot be null");
        this.type = type;
    }

    @Override
    protected Object parse(String json) {
        try {
            return objectMapper.readValue(json, this.type);
        } catch (IOException var3) {
            throw new RuntimeException(var3);
        }
    }

    @Override
    protected String toJson(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException var3) {
            throw new RuntimeException(var3);
        }
    }

    public static void setObjectMapper(ObjectMapper objectMapper) {
        Assert.notNull(objectMapper, "ObjectMapper should not be null");
        JacksonObjectTypeHandler.objectMapper = objectMapper;
    }
}
