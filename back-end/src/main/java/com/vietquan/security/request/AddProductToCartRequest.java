package com.vietquan.security.request;

import com.vietquan.security.enumPackage.Size;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NonNull;

@Data
public class AddProductToCartRequest {
    @NotNull
    private Integer userId;
    @NotNull
    private Integer productId;
    @NotNull
    private Size size;


}
