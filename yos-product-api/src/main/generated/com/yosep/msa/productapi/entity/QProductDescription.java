package com.yosep.msa.productapi.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QProductDescription is a Querydsl query type for ProductDescription
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QProductDescription extends EntityPathBase<ProductDescription> {

    private static final long serialVersionUID = 1556774523L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QProductDescription productDescription = new QProductDescription("productDescription");

    public final StringPath description = createString("description");

    public final StringPath id = createString("id");

    public final QProduct product;

    public final DateTimePath<java.time.LocalDateTime> productDescriptionRdate = createDateTime("productDescriptionRdate", java.time.LocalDateTime.class);

    public final DateTimePath<java.time.LocalDateTime> productDescriptionUdate = createDateTime("productDescriptionUdate", java.time.LocalDateTime.class);

    public QProductDescription(String variable) {
        this(ProductDescription.class, forVariable(variable), INITS);
    }

    public QProductDescription(Path<? extends ProductDescription> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QProductDescription(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QProductDescription(PathMetadata metadata, PathInits inits) {
        this(ProductDescription.class, metadata, inits);
    }

    public QProductDescription(Class<? extends ProductDescription> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.product = inits.isInitialized("product") ? new QProduct(forProperty("product")) : null;
    }

}

