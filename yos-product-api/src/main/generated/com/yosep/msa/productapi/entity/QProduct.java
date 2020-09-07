package com.yosep.msa.productapi.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QProduct is a Querydsl query type for Product
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QProduct extends EntityPathBase<Product> {

    private static final long serialVersionUID = 1255021249L;

    public static final QProduct product = new QProduct("product");

    public final ListPath<ProductDescription, QProductDescription> productDescriptions = this.<ProductDescription, QProductDescription>createList("productDescriptions", ProductDescription.class, QProductDescription.class, PathInits.DIRECT2);

    public final StringPath productDetail = createString("productDetail");

    public final EnumPath<ProductDetailType> productDetailType = createEnum("productDetailType", ProductDetailType.class);

    public final StringPath productId = createString("productId");

    public final StringPath productName = createString("productName");

    public final NumberPath<Integer> productPrice = createNumber("productPrice", Integer.class);

    public final ListPath<ProductProfileFile, QProductProfileFile> productProfileImageURLs = this.<ProductProfileFile, QProductProfileFile>createList("productProfileImageURLs", ProductProfileFile.class, QProductProfileFile.class, PathInits.DIRECT2);

    public final NumberPath<Integer> productQuantity = createNumber("productQuantity", Integer.class);

    public final DateTimePath<java.time.LocalDateTime> productRdate = createDateTime("productRdate", java.time.LocalDateTime.class);

    public final NumberPath<Integer> productSale = createNumber("productSale", Integer.class);

    public final EnumPath<ProductType> productType = createEnum("productType", ProductType.class);

    public final DateTimePath<java.time.LocalDateTime> productUdate = createDateTime("productUdate", java.time.LocalDateTime.class);

    public QProduct(String variable) {
        super(Product.class, forVariable(variable));
    }

    public QProduct(Path<? extends Product> path) {
        super(path.getType(), path.getMetadata());
    }

    public QProduct(PathMetadata metadata) {
        super(Product.class, metadata);
    }

}

