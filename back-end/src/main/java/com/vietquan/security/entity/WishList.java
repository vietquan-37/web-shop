    package com.vietquan.security.entity;

    import jakarta.persistence.*;
    import lombok.AllArgsConstructor;
    import lombok.Data;
    import lombok.NoArgsConstructor;
    import org.hibernate.annotations.OnDelete;
    import org.hibernate.annotations.OnDeleteAction;

    @Entity
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public class WishList {
        @Id
        @GeneratedValue(
                strategy = GenerationType.IDENTITY
        )
        private Integer id;
        @ManyToOne(
                fetch = FetchType.LAZY,
                optional = false
        )
        @JoinColumn(
                name = "product_id",
                nullable = false
        )
        @OnDelete(
                action = OnDeleteAction.CASCADE
        )
        private Product product;
        @ManyToOne(
                fetch = FetchType.LAZY,
                optional = false
        )
        @JoinColumn(
                name = "user_id",
                nullable = false
        )
        @OnDelete(
                action = OnDeleteAction.CASCADE
        )
        private User user;

    }
