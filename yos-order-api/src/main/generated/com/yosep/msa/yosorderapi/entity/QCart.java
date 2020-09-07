package com.yosep.msa.yosorderapi.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QCart is a Querydsl query type for Cart
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QCart extends EntityPathBase<Cart> {

    private static final long serialVersionUID = 1773592864L;

    public static final QCart cart = new QCart("cart");

    public final StringPath orderId = createString("orderId");

    public final DateTimePath<java.time.LocalDateTime> orderRdate = createDateTime("orderRdate", java.time.LocalDateTime.class);

    public final DateTimePath<java.time.LocalDateTime> orderUdate = createDateTime("orderUdate", java.time.LocalDateTime.class);

    public final StringPath productId = createString("productId");

    public final StringPath userName = createString("userName");

    public QCart(String variable) {
        super(Cart.class, forVariable(variable));
    }

    public QCart(Path<? extends Cart> path) {
        super(path.getType(), path.getMetadata());
    }

    public QCart(PathMetadata metadata) {
        super(Cart.class, metadata);
    }

}

