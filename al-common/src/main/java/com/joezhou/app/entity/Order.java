package com.joezhou.app.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 * 订单表
 * </p>
 *
 * @author ${author}
 * @since 2022-05-01
 */
@TableName("`order`")
@Data
@EqualsAndHashCode(callSuper = false)
public class Order implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 下单账户
     */
    private String username;

    /**
     * 商品表外键
     */
    private Integer productId;

    /**
     * 商品名（冗余）
     */
    private String productName;

    /**
     * 购买数量
     */
    private Integer number;


}
