package com.joezhou.app.feign;

import com.joezhou.app.fallback.ProductFeignFallBack;
import com.joezhou.app.util.JsonResult;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author JoeZhou
 */
@Qualifier("productFeign")
@FeignClient(value = "al-product", fallback = ProductFeignFallBack.class)
public interface ProductFeign {

    /**
     * 根据商品主键查询一条商品信息
     *
     * @param id 商品主键
     * @return 响应数据
     */
    @RequestMapping("/api/product/select-by-id")
    JsonResult selectById(@RequestParam("id") Integer id);
}
