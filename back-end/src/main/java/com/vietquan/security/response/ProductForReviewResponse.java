package com.vietquan.security.response;

import com.vietquan.security.request.ProductRequest;
import lombok.Data;

import java.util.List;
@Data
public class ProductForReviewResponse {
    List<ProductRequest> productList;

}
