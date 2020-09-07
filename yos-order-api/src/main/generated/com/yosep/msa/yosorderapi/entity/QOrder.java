package com.yosep.msa.yosorderapi.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QOrder is a Querydsl query type for Order
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QOrder extends EntityPathBase<Order> {

    private static final long serialVersionUID = -841621170L;

    public static final QOrder order = new QOrder("order1");

    public final StringPath detailAddr = createString("detailAddr");

    public final StringPath extraAddr = createString("extraAddr");

    public final BooleanPath isBuy = createBoolean("isBuy");

    public final StringPath jibunAddr = createString("jibunAddr");

    public final StringPath orderId = createString("orderId");

    public final DateTimePath<java.time.LocalDateTime> orderRdate = createDateTime("orderRdate", java.time.LocalDateTime.class);

    public final DateTimePath<java.time.LocalDateTime> orderUdate = createDateTime("orderUdate", java.time.LocalDateTime.class);

    public final StringPath phone = createString("phone");

    public final StringPath postCode = createString("postCode");

    public final StringPath productId = createString("productId");

    public final StringPath receiverName = createString("receiverName");

    public final StringPath roadAddr = createString("roadAddr");

    public final StringPath senderId = createString("senderId");

    public final StringPath senderName = createString("senderName");

    public final NumberPath<Integer> state = createNumber("state", Integer.class);

    public QOrder(String variable) {
        super(Order.class, forVariable(variable));
    }

    public QOrder(Path<? extends Order> path) {
        super(path.getType(), path.getMetadata());
    }

    public QOrder(PathMetadata metadata) {
        super(Order.class, metadata);
    }

}

