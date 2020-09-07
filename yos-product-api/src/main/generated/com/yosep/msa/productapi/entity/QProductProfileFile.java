package com.yosep.msa.productapi.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QProductProfileFile is a Querydsl query type for ProductProfileFile
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QProductProfileFile extends EntityPathBase<ProductProfileFile> {

    private static final long serialVersionUID = -836374204L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QProductProfileFile productProfileFile = new QProductProfileFile("productProfileFile");

    public final StringPath fileId = createString("fileId");

    public final QProduct product;

    public final DateTimePath<java.time.LocalDateTime> rdate = createDateTime("rdate", java.time.LocalDateTime.class);

    public final DateTimePath<java.time.LocalDateTime> udate = createDateTime("udate", java.time.LocalDateTime.class);

    public final StringPath url = createString("url");

    public QProductProfileFile(String variable) {
        this(ProductProfileFile.class, forVariable(variable), INITS);
    }

    public QProductProfileFile(Path<? extends ProductProfileFile> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QProductProfileFile(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QProductProfileFile(PathMetadata metadata, PathInits inits) {
        this(ProductProfileFile.class, metadata, inits);
    }

    public QProductProfileFile(Class<? extends ProductProfileFile> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.product = inits.isInitialized("product") ? new QProduct(forProperty("product")) : null;
    }

}

