create table carts
(
    id          binary(16)
        primary key,
    dateCreated date       default (curdate())         not null
);
create table cart_items
(
    id         bigint auto_increment
        primary key,
    cart_id    binary(16)    not null,
    product_id bigint        not null,
    quantity   int default 1 not null,
    constraint cart_items_cart_product_unique
        unique (cart_id, product_id),
    constraint cart_items___fk
        foreign key (product_id) references products (id)
            on delete cascade,
    constraint cart_items_carts_id_fk
        foreign key (cart_id) references carts (id)
            on delete cascade
);
CREATE TRIGGER before_insert_carts
    BEFORE INSERT ON carts
    FOR EACH ROW
    SET NEW.id = IFNULL(
    NEW.id,
    UNHEX(REPLACE(UUID(), '-', ''))
);
ALTER TABLE carts CHANGE COLUMN dateCreated date_created DATE NOT NULL DEFAULT CURDATE();







