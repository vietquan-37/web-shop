package com.vietquan.security.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vietquan.security.request.ProductRequest;
import com.vietquan.security.request.ProductSizeRequest;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.List;
import java.util.stream.Collectors;


@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer productId;

    private String name;
    private Double price;

    @Lob
    private String description;

    @Lob
    @Column(columnDefinition = "longblob")
    private byte[] image;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Category category;
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductSize> productSizes;
    @OneToMany(mappedBy = "products", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductImage> images;
    @OneToMany(
            mappedBy = "productReview",cascade = CascadeType.ALL,orphanRemoval = true
    )
    private List<Review> reviews;

    public ProductRequest getDto() {
        ProductRequest request = new ProductRequest();
        request.setId(productId);
        request.setName(name);
        request.setPrice(price);
        request.setImage(image);
        request.setDescription(description);
        request.setCategoryId(category.getCategoryId());
        request.setCategoryName(category.getName());
        if (productSizes != null) {
            List<ProductSizeRequest> productSizeRequests = productSizes.stream().map(productSize -> {
                        ProductSizeRequest sizeRequest = new ProductSizeRequest();
                        sizeRequest.setSize(productSize.getSize());
                        sizeRequest.setQuantity(productSize.getQuantity());
                        return sizeRequest;
                    })
                    .collect(Collectors.toList());
            request.setProductSizes(productSizeRequests);
        }
        if (images != null) {
            request.setByteImages(images.stream().map(ProductImage::getImage).collect(Collectors.toList()));
        }

        return request;
    }

}
