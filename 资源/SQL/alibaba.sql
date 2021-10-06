# database

create database alibaba character set utf8mb4;
use alibaba;
grant all on alibaba.* to 'alibaba'@'localhost' identified by 'alibaba';
flush privileges;

# table

create table user
(
    id       int(11) auto_increment comment '主键',
    username varchar(50) not null comment '账号',
    password varchar(50) not null comment '密码',
    phone    varchar(20) comment '电话',
    primary key (id)
)
    comment '用户表';

insert into user
values (1, 'admin', '123', '18888888888');
commit;

create table product
(
    id            int(11) auto_increment comment '主键',
    product_name  varchar(50)    not null comment '商品名',
    product_price decimal(10, 2) not null comment '商品价格',
    product_stock int(11) comment '商品库存',
    primary key (id)
)
    comment '商品表';

insert into product
values (1, '海尔冰箱', 3500.00, 5);
commit;

create table `order`
(
    id           int(11) auto_increment comment '主键',
    username     varchar(50) not null comment '下单账户',
    product_id   int(11)     not null comment '商品表外键',
    product_name varchar(50) not null comment '商品名（冗余）',
    number       int(11) comment '购买数量',
    primary key (id),
    foreign key (product_id) references product (id)
)
    comment '订单表';

insert into `order`
values (1, 'admin', 1, '海尔冰箱', 5);
commit;