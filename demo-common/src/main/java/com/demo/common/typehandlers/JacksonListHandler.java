package com.demo.common.typehandlers;

import com.baomidou.mybatisplus.extension.handlers.AbstractJsonTypeHandler;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

/**
 * 字段为实体集合类型时使用
 * 使用方法:新Handler继承JacksonListHandler,泛型指定实体类型
 *
 * @author molong
 * @date 2021/9/6
 */
public class JacksonListHandler<T>  extends AbstractJsonTypeHandler<List<T>> {

    protected static ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    /**
     * 指定的实体类型
     */
    private final Class<T> tClass;

    /**
     * 构造方法 设置指定的实体类型
     */
    @SuppressWarnings("unchecked")
    public JacksonListHandler() {
        this.tClass = (Class<T>) ((ParameterizedType)getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }


    @Override
    protected List<T> parse(String json) {
        try {
            JsonNode jsonNode = objectMapper.readTree(json);
            ArrayNode arrayNode = (ArrayNode) jsonNode;
            ArrayList<T> result = new ArrayList<>(arrayNode.size());
            if (!arrayNode.isEmpty()) {
                for (JsonNode node : arrayNode) {
                    result.add(objectMapper.treeToValue(node, this.tClass));
                }
            }
            return result;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected String toJson(List<T> objs) {
        try {
            return objectMapper.writeValueAsString(objs);
        } catch (JsonProcessingException var3) {
            throw new RuntimeException(var3);
        }
    }

}

