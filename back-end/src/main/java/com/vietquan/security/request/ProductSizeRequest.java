package com.vietquan.security.request;

import com.vietquan.security.enumPackage.Size;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ProductSizeRequest {
    @NotNull
    @Enumerated(
            EnumType.STRING
    )
    private Size size;
    @Min(value = 1)
    private int quantity;
}
