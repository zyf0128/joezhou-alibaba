package com.joezhou.app.controller;


import com.joezhou.app.entity.Product;
import com.joezhou.app.service.ProductService;
import com.joezhou.app.util.JacksonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 商品表 前端控制器
 * </p>
 *
 * @author ${author}
 * @since 2021-09-15
 */
@Slf4j
@RestController
@RequestMapping("/api/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @RequestMapping("/select-by-id")
    public String selectById(@RequestParam("id") Integer id) {
        log.info("接收到一个查询请求，根据商品ID查询商品信息");
        Product product = productService.getById(id);
        if (product == null) {
            return JacksonUtil.build(0, "查询商品失败");
        }
        log.info("查询请求执行成功，数据为 {}", product);
        return JacksonUtil.build(product);
    }

    @RequestMapping("/reduce-inventory")
    public String reduceInventory(
            @RequestParam("product-id") Integer productId,
            @RequestParam("number") Integer number) {
        log.info("接收到一个扣减库存的请求，商品ID为 {}，扣减数量为 {}", productId, number);
        int result = productService.reduceInventory(productId, number);
        if (result <= 0) {
            return JacksonUtil.build(0, "扣减库存失败");
        }
        log.info("扣减库存成功");
        return JacksonUtil.build(1, "扣减库存成功");
    }

}

