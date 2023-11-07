package com.vietquan.security.request;

import com.vietquan.security.enumPackage.Size;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;

@Data
public class ProductSizeRequest {
    @Enumerated(
            EnumType.STRING
    )
    private Size size; // Enum representing size (e.g., Size.XL, Size.L)
    private int quantity;
}
