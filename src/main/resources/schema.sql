create table if not exists product
(
    id          bigint unsigned not null auto_increment comment '商品ID',
    title       varchar(100)    not null unique comment '商品タイトル',
    description varchar(500)    not null comment '商品説明',
    price       int unsigned    not null comment '商品価格',
    image_path  text comment '商品画像',
    created_at  datetime        not null default current_timestamp comment '登録時間',
    updated_at  datetime        not null default current_timestamp on update current_timestamp comment '更新時間',
    PRIMARY KEY (id)
) comment '商品テーブル' engine = innodb
                   charset = utf8mb4;
