package com.vietquan.security.entity;

import com.vietquan.security.enumPackage.Size;
import com.vietquan.security.request.ProductSizeRequest;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(
        name = "product_size"
)
@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductSize {
    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private Integer sizeId;
    @Enumerated(
            EnumType.STRING
    )
    private Size size;
    private Integer quantity;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

}
