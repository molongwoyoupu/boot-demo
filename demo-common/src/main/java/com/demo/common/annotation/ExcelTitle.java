package com.demo.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 导出excel自定义注解
 *
 * @author molong
 * @date 2021/9/6
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ExcelTitle {
    /**
     * 对应的表格标题
     * @return  表格标题
     */
    String value();

    /**
     * 是否是枚举类型
     * @return  枚举类型
     */
    boolean hasEnum() default false;

    /**
     * 枚举的类是什么
     * @return  枚举的类
     */
    Class<?> enumClass() default Void.class;

    /**
     * 需要执行的枚举方法是什么
     * 用来获取枚举对应的中文
     * @return  枚举方法
     */
    String enumMethod() default "";

    /**
     * 执行的枚举方法是否有参数,参数类型是什么
     * @return  参数类型
     */
    Class<?> enumMethodArgClass() default Void.class;
}
