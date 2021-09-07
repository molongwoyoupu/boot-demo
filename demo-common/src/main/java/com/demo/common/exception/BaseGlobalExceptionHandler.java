package com.demo.common.exception;

import cn.hutool.json.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.internal.engine.path.NodeImpl;
import org.hibernate.validator.internal.engine.path.PathImpl;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Objects;

/**
 * 全局异常处理
 *
 * @author molong
 * @date 2021/9/6
 */
@Slf4j
public class BaseGlobalExceptionHandler {

    @ResponseBody
    @ExceptionHandler(value = ApiException.class)
    public String handle(ApiException e) {
        log.error("api异常:", e);
        return e.getMsg();
    }

    @ResponseBody
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public String handMethodArgumentNotValidException(MethodArgumentNotValidException e){
        log.error("参数异常:", e);
        return getArgumentsCommonResult(e);
    }

    @ResponseBody
    @ExceptionHandler(IllegalArgumentException.class)
    public String handIllegalArgumentException(IllegalArgumentException e){
        log.error("非法参数:", e);
        String message = e.getMessage();
        if(!StringUtils.hasText(message)){
            message = "非法参数";
        }
        return message;
    }

    @ResponseBody
    @ExceptionHandler(ConstraintViolationException.class)
    public String handConstraintViolationException(ConstraintViolationException e){
        log.error("非法参数：", e);
        JSONObject jsonObject = new JSONObject();
        for (ConstraintViolation<?> constraintViolation : e.getConstraintViolations()) {
            String message = constraintViolation.getMessage();
            PathImpl propertyPath = (PathImpl) constraintViolation.getPropertyPath();
            NodeImpl leafNode = propertyPath.getLeafNode();
            String paramName = leafNode.getName();
            jsonObject.set(paramName, message);
        }
        return "非法参数：" + jsonObject.toString();
    }

    @ResponseBody
    @ExceptionHandler(BindException.class)
    public String handBindException(BindException e){
        log.error("参数错误：", e);
        if(CollectionUtils.isEmpty(e.getAllErrors())){
            return "参数错误：" + e.getMessage();
        }
        return getArgumentsCommonResult(e);
    }

    private String getArgumentsCommonResult(BindException e) {
        JSONObject jsonObject = new JSONObject();
        for (ObjectError allError : e.getBindingResult().getAllErrors()) {
            //提取需要打印的数据
            jsonObject.set(((DefaultMessageSourceResolvable) Objects.requireNonNull(allError.getArguments())[0]).getDefaultMessage(), allError.getDefaultMessage());
        }
        return "参数不正确：" + jsonObject.toString();
    }
}
