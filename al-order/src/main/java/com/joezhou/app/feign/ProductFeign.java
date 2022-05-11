package com.joezhou.app.feign;

import com.joezhou.app.util.JsonResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author JoeZhou
 */
@FeignClient("al-product")
public interface ProductFeign {

    /**
     * 根据商品主键查询一条商品信息
     *
     * @param id 商品主键
     * @return 响应数据
     */
    @RequestMapping("/api/product/select-by-id")
    JsonResult selectById(@RequestParam("id") Integer id);

    /**
     * 扣减库存
     *
     * @param productId 扣减哪个商品的库存
     * @param number    扣减多少个库存
     * @return 正数表示成功，零或负数表示失败
     */
    @RequestMapping("/api/product/reduce-inventory")
    JsonResult reduceInventory(@RequestParam("product-id") Integer productId,
                           @RequestParam("number") Integer number);
}
