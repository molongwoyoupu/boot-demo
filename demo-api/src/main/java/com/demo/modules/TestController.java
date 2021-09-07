package com.demo.modules;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 测试controller
 *
 * @author molong
 * @date 2021/9/6
 */
@RestController
@Slf4j
@Api(tags = "测试测试")
public class TestController {

    @ApiOperation("测试")
    @GetMapping("/test")
    public void dailyStatisticOfPassenger() {
        log.debug("hello world ！");
    }
}
