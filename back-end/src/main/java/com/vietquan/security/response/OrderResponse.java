package com.vietquan.security.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.vietquan.security.request.OrderRequest;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderResponse {
    private String approvalUrl;
    private OrderRequest request;

}
