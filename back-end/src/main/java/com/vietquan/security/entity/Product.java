package com.vietquan.security.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vietquan.security.request.ProductRequest;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

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


    public ProductRequest getDto(){
        ProductRequest request=new ProductRequest();
        request.setId(productId);
        request.setName(name);
        request.setPrice(price);
        request.setImage(image);
        request.setDescription(description);
        request.setCategoryId(category.getCategoryId());
        request.setCategoryName(category.getName());
        return request;
    }
}
