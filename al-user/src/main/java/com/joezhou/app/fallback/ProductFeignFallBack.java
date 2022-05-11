package com.joezhou.app.fallback;

import com.joezhou.app.feign.ProductFeign;
import com.joezhou.app.util.JsonResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author JoeZhou
 */
@Slf4j
@Component
public class ProductFeignFallBack implements ProductFeign {

    @Override
    public JsonResult selectById(Integer id) {
        log.info("远程调用商品接口失败，进行熔断降级处理");
        return JsonResult.fail(0, "远程调用商品接口失败，进行熔断降级处理");
    }
}
