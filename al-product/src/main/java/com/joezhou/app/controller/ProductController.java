package com.joezhou.app.controller;


import com.joezhou.app.entity.Product;
import com.joezhou.app.service.ProductService;
import com.joezhou.app.util.JsonResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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

    /**
     * 添加商品记录
     * cli: http://localhost:8082/api/product/insert?username=a&
     *
     * @param product 商品实体
     * @return 响应操作成功/失败信息
     */
    @RequestMapping("insert")
    public JsonResult insert(Product product) {
        log.info("接收到添加商品记录请求，参数product：{}", product);
        return productService.save(product) ? JsonResult.ok() : JsonResult.fail();
    }

    /**
     * 按主键查询商品记录
     * cli: http://localhost:8082/api/product/select-by-id?id=1
     *
     * @param id 商品记录主键
     * @return 成功返回对应商品记录，失败返回失败信息
     */
    @RequestMapping("select-by-id")
    public JsonResult selectById(@RequestParam("id") Integer id) {
        log.info("接收到按主键查询商品记录请求，参数id：{}", id);
        Product product = productService.getById(id);
        log.info("根据主键查询到商品记录：{}", product);
        return product != null ? JsonResult.ok(product) : JsonResult.fail();
    }

    /**
     * 按主键修改商品记录
     * cli: http://localhost:8082/api/product/update-by-id?id=1&...
     *
     * @param product 商品实体
     * @return 响应操作成功/失败信息
     */
    @RequestMapping("update-by-id")
    public JsonResult updateById(Product product) {
        log.info("接收到按主键修改商品记录请求，参数product：{}", product);
        return productService.updateById(product) ? JsonResult.ok() : JsonResult.fail();
    }

    /**
     * 按主键删除商品记录
     * cli: http://localhost:8082/api/product/delete-by-id?id=1
     *
     * @param id 商品记录主键
     * @return 响应操作成功/失败信息
     */
    @RequestMapping("delete-by-id")
    public JsonResult deleteById(@RequestParam Integer id) {
        log.info("接收到按主键删除商品记录请求，参数id：{}", id);
        return productService.removeById(id) ? JsonResult.ok() : JsonResult.fail();
    }

    /**
     * 全查商品记录
     * cli: http://localhost:8082/api/user/list
     *
     * @return 成功返回全部商品记录，失败返回失败信息
     */
    @RequestMapping("list")
    public JsonResult list() {
        log.info("接收到按全查商品记录的请求");
        List<Product> products = productService.list();
        log.info("全查到全部商品记录：{}", products);
        return products != null && !products.isEmpty() ? JsonResult.ok(products) : JsonResult.fail();
    }

    @RequestMapping("/reduce-inventory")
    public JsonResult reduceInventory(
            @RequestParam("product-id") Integer productId,
            @RequestParam("number") Integer number) {
        log.info("接收到扣减库存的请求，参数productId为：{}，参数扣减数量为 {}", productId, number);
        return productService.reduceInventory(productId, number) > 0 ?
                JsonResult.ok() : JsonResult.fail();
    }

}

