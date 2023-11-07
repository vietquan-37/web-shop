package com.vietquan.security.entity;

import com.vietquan.security.request.CartItemsRequest;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartItems {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer cartId;
    private Double price;
    private Integer quantity;
    @ManyToOne(
            fetch = FetchType.LAZY,
            optional = false

    )
    @JoinColumn(
            name = "product_id",
            nullable = false
    )
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Product product;
    @ManyToOne(
            fetch = FetchType.LAZY
    )
    @JoinColumn(
            name = "order_id"
    )
    private Order order;
    @ManyToOne(
            fetch = FetchType.LAZY
    )
    @JoinColumn(
            name = "user_id"
    )
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_size_id")

    private ProductSize productSize;
    public CartItemsRequest getCartDto(){
        CartItemsRequest dto=new CartItemsRequest();
        dto.setCartId(cartId);
        dto.setPrice(price);
        dto.setProductId(product.getProductId());
        dto.setQuantity(quantity);
        dto.setUserId(user.getId());
        dto.setProductName(product.getName());
        dto.setImg(product.getImage());
        dto.setSize(productSize.getSize());

        return dto;
    }
}
