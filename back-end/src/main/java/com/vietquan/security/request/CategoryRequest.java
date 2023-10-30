package com.vietquan.security.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CategoryRequest {
    private  Integer id;
    @NotNull
    private String name;
    @NotNull
    private String description;
}
