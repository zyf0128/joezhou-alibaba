package com.joezhou.app.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.joezhou.app.entity.Product;

/**
 * <p>
 * 商品表 服务类
 * </p>
 *
 * @author ${author}
 * @since 2021-09-15
 */
public interface ProductService extends IService<Product> {

    /**
     * 扣减库存
     *
     * @param productId 扣减哪个商品的库存
     * @param number    扣减多少个库存
     * @return 正数表示成功，零或负数表示失败
     */
    int reduceInventory(Integer productId, Integer number);

}
