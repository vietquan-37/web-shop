package com.vietquan.security.request;

import com.vietquan.security.entity.Review;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class ProductRequest {

    private Integer id;
    @NotNull
    private String name;
    @NotNull
    @Min(value = 1)
    private Double price;

    @NotNull
    private String description;


    private byte[] image;
    @NotNull
    private Integer categoryId;

    private String categoryName;
    @NotNull
    private MultipartFile img;

    private List<ProductSizeRequest> productSizes;
    private List<byte[]> byteImages;
    private List<MultipartFile> additionalImages;

}
