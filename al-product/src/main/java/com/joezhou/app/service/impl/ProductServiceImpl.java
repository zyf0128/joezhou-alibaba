package com.joezhou.app.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.joezhou.app.entity.Product;
import com.joezhou.app.mapper.ProductMapper;
import com.joezhou.app.service.ProductService;
import io.seata.core.context.RootContext;
import io.seata.tm.api.GlobalTransactionContext;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 商品表 服务实现类
 * </p>
 *
 * @author ${author}
 * @since 2021-09-15
 */
@Slf4j
@Service
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements ProductService {

    @Autowired
    private ProductMapper productMapper;

    @SneakyThrows
    @Transactional(rollbackFor = Exception.class)
    @Override
    public int reduceInventory(Integer productId, Integer number) {

        // 根据商品ID查询商品信息
        Product product = productMapper.selectById(productId);
        log.info("根据商品ID查询商品信息：{}", product);

        // 获取商品的剩余库存
        Integer productStock = product.getProductStock();
        log.info("查询到当前商品剩余库存为：{}", productStock);

        // 判断商品剩余库存是否充足
        if (productStock < number) {
            log.error("扣减库存失败：当前库存不足");

            // 分布式事务：向TC提交rollback信息
            GlobalTransactionContext.reload(RootContext.getXID()).rollback();
            return 0;
        }

        // 在内存中扣减库存
        product.setProductStock(productStock - number);

        // 真正在数据库中完成扣减库存操作
        int updateResult = productMapper.updateById(product);
        if (updateResult <= 0) {
            log.error("扣减库存失败：update操作失败");
            return 0;
        }
        return 1;
    }
}
