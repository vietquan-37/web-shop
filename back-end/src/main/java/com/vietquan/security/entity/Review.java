package com.vietquan.security.entity;

import com.vietquan.security.request.ReviewRequest;
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
public class Review {
    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private Integer reviewId;
    private Integer star;
    private String comment;
    @ManyToOne(
            fetch = FetchType.LAZY,
            optional = false

    )
    @JoinColumn(
            name = "product_id",
            nullable = false
    )
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Product productReview;

    @ManyToOne(
            fetch = FetchType.LAZY,
            optional = false

    )
    @JoinColumn(
            name = "user_id",
            nullable = false
    )
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User users;
    @ManyToOne(
            fetch = FetchType.LAZY,
            optional = false

    )
    @JoinColumn(
            name = "order_id",
            nullable = false
    )
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Order orders;

    public ReviewRequest getDto(){
        ReviewRequest request=new ReviewRequest();
        request.setReviewId(reviewId);
        request.setStar(star);
        request.setComment(comment);
        request.setUsername(users.getUsername());
        request.setProductId(productReview.getProductId());
        request.setImg(productReview.getImage());
        request.setProductName(productReview.getName());
        request.setAvatar(users.getAvatar());
        return request;
    }
}
